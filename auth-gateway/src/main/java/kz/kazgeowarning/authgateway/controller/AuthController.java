package kz.kazgeowarning.authgateway.controller;

import io.swagger.annotations.ApiOperation;
import kz.kazgeowarning.authgateway.dto.*;
import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.model.AdminEmployee;
import kz.kazgeowarning.authgateway.model.Session;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.SessionRepository;
import kz.kazgeowarning.authgateway.repository.UserRepository;
import kz.kazgeowarning.authgateway.security.service.TokenService;
import kz.kazgeowarning.authgateway.security.service.impl.UsersService;
import kz.kazgeowarning.authgateway.service.SMSService;
import kz.kazgeowarning.authgateway.service.SMTPService;
import kz.kazgeowarning.authgateway.util.PageableCustom;
import kz.kazgeowarning.authgateway.util.error.ErrorCode;
import kz.kazgeowarning.authgateway.util.error.InternalException;
import kz.kazgeowarning.authgateway.util.exception.ResourceNotFoundException;
import kz.kazgeowarning.authgateway.util.validators.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final TokenService tokenService;
    private final UsersService usersService;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository usersRepository;
    private final SMTPService iEmailService;
    private final SMSService smsService;
    public static final String PRIVATE_URL = "/private/user";
    public static final String PUBLIC_URL = "/public/user";

    @Value("${approve.url}")
    private String url;
//    @CrossOrigin(origins = "*")
    @PostMapping(value = PUBLIC_URL + "/v1/login")
    public ResponseEntity<?> authenticate(@RequestBody final LoginDTO loginDTO) throws InternalException {
        try {
            User user = usersService.findUserByUsername(loginDTO.getEmail());

            if (!(bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword()))) {
                throw new InternalException(ErrorCode.ErrorCodes.AUTH_ERROR, "Неправильный пароль");
            }

            if (!user.isApproved()) {
                throw new InternalException(ErrorCode.ErrorCodes.AUTH_ERROR, "Пользователь не подтвердил регистрацию");
            }

            var tokenDto = tokenService.getToken(loginDTO.getEmail(), loginDTO.getPassword());

            return ResponseEntity.ok(LoginResponse.builder()
                    .token(tokenDto.getToken())
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber()!=null?user.getPhoneNumber():"")
                    .birthDate(user.getBirthDate()!=null?new Date(user.getBirthDate().getTime()):null)
                    //.city(user.getCity()!=null?user.getCity():"Almaty")
                    .email(user.getEmail())
                    .role(user.getRole())
                    .loginType(user.getLoginType())
                    .build());
        } catch (InternalException | ResourceNotFoundException exception) {
            if (exception instanceof ResourceNotFoundException)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Пользователь с таким логином не найден");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }


    @GetMapping(PUBLIC_URL + "/v1/users/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping(PUBLIC_URL + "/v1/users/emailRecipients")
    public ResponseEntity<List<User>> getAllNotified() {
        return ResponseEntity.ok(usersService.findAllEmailRecipients());
    }

    @GetMapping(PUBLIC_URL + "/v1/users/role-employee")
    public ResponseEntity<List<User>> getUserEmployee() {
        return ResponseEntity.ok(usersService.findByEmployeeRole());
    }
    @GetMapping(PUBLIC_URL + "/v1/users/role-admin")
    public ResponseEntity<List<User>> getUserAdmin() {
        return ResponseEntity.ok(usersService.getAdmins());
    }

    @PostMapping(PUBLIC_URL + "/v1/users/register-employee")
    public ResponseEntity<AdminEmployee> registerEmployeeToAdmin(@RequestBody AdminEmployeeDTO adminEmployee) {
        return ResponseEntity.ok(usersService.registerEmployeeToAdmin(adminEmployee));
    }

    @GetMapping(PUBLIC_URL + "/v1/users/get/admin-employees")
    public ResponseEntity<?> getEmployeesByAdminEmail(@RequestParam String email) {
        return ResponseEntity.ok(usersService.getEmployeesByAdminEmail(email));
    }

    @GetMapping(PUBLIC_URL + "/v1/users/get/admin-employees/by-employee")
    public ResponseEntity<?> getRegisteredEmployeesByEmployee(@RequestParam String email) {
        return ResponseEntity.ok(usersService.getRegisteredEmployeesByEmployee(email));
    }


    @GetMapping(PUBLIC_URL + "/v1/users/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usersService.findUserById(id));
    }

    @GetMapping(PUBLIC_URL + "/v1/users/email/{email}")
    public ResponseEntity<Optional<User>> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(usersService.findUserByEmail(email));
    }

    @GetMapping(PUBLIC_URL + "/v1/employee/count")
    public ResponseEntity<Long> getSizeEmployee() {
        return ResponseEntity.ok(usersService.getSizeEmployee());
    }

    @GetMapping(PUBLIC_URL + "/v1/employee/pagination")
    public ResponseEntity<Page<User>> getEmployeePaged(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(usersService.findAllUsersPaged(params));
    }


    @PostMapping(PUBLIC_URL + "/v1/users/update")
    public ResponseEntity<User> createOrUpdateUser(@RequestBody ClientsDTO user) {
        return ResponseEntity.ok(usersService.createOrUpdateUser(user));
    }

    @DeleteMapping(PRIVATE_URL + "/v1/users/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        usersService.deleteById(id);
        return ResponseEntity.ok("OK");
    }

   // @CrossOrigin(origins = "*", methods = {RequestMethod.GET})
    @GetMapping(PUBLIC_URL + "/v1/token/{token}")
    public ResponseEntity<TokenDTO> getUserByToken(@PathVariable(value = "token") String token) {
        Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);
        final TokenDTO response = new TokenDTO();
        response.setToken(session.getToken());
        User user = usersRepository.getById(session.getUserId());
        response.setUser(user);
        return ResponseEntity.ok(response);
    }

   // @CrossOrigin(origins = "*")
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws InternalException {
        return ResponseEntity.ok(this.tokenService.deactivateToken(request));
    }

    @ApiOperation(value = "Обновление пароля", tags = {"Auth"})
    @PutMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws InternalException {
        User user = usersRepository.getById(resetPasswordDTO.getUserId());
        if (!bCryptPasswordEncoder.matches(resetPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new InternalException(ErrorCode.ErrorCodes.PASSWORDS_NOT_MATCH, "Пароли не совпадают");
        }

        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDTO.getNewPassword()));
        usersRepository.save(user);
        return ResponseEntity.ok("SUCCESS");
    }

    @ApiOperation(value = "Обновление пароля без старого", tags = {"Auth"})
    @PutMapping("/reset2/")
    public ResponseEntity<String> resetPasswordWithoutOld(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        User user = usersRepository.getById(resetPasswordDTO.getUserId());
        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDTO.getNewPassword()));
        usersRepository.save(user);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping(PUBLIC_URL + "/v1/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid User newUser) throws InternalException, MessagingException {
        try {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.validate(newUser.getEmail())) {
                throw new InternalException(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Неверный e-mail формат.");
            }

            if (usersRepository.getByEmailAndActive(newUser.getEmail(), true) != null) {
                throw new InternalException(ErrorCode.ErrorCodes.EMAIL_EXIST, "Такой e-mail уже существует.");
            }
            // String signupToken = usersService.sendEmailConfirmLink(new UserDTO(newUser));
            String signupToken = "testSignup";

            String subject = "Подтверждение регистрации пользователя " + newUser.getEmail();
            String message = "<h2>Добрый день, " + newUser.getFirstName() + " " + newUser.getLastName() + "!</h2>" + "<br>" +
                    "<h3>Благодарим за использавания нашим сайтом!<br>" +
                    "Вам необходимо подтвердить аккаунт, " +
                    "пройдя по ссылке "+url+"/internal/api/public/user/v1/confirm/" + newUser.getEmail() + "<h3><br>" +
                    "<h3>С уважением, команда KazGeoWarning<h3>";
            iEmailService.sendMail(newUser.getEmail(), subject, message);

            newUser.setSignupToken(signupToken);
            // шифровать пароль
            newUser.setPassword(newUser.getPassword() == null ? bCryptPasswordEncoder.encode("12345")
                    : bCryptPasswordEncoder.encode(newUser.getPassword()));

            // Активность пользователя
            newUser.setActive(true);
            newUser.setApproved(false);
            newUser.setRegisterDate(new Timestamp(new Date().getTime()));
            usersService.create(newUser);
            return ResponseEntity.ok(newUser);
        } catch (InternalException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody final String email) throws InternalException {
        User user = usersRepository.getByEmailAndActive(email, true);
        if (user == null) {
            throw new InternalException(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Email не найден");
        }
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setMiddlename(user.getMiddleName());
        dto.setName(user.getFirstName());
        dto.setPassword(user.getPassword());
        dto.setSurname(user.getLastName());
//            dto.setSignupToken(usersService.sendMailResetPassword(dto));
        user.setSignupToken(dto.getSignupToken());
        usersRepository.save(user);
        return ResponseEntity.ok("Password successfully changed");
    }

    @GetMapping(PUBLIC_URL + "/v1/confirm/{email}")
    public RedirectView confirmRegistration(@PathVariable("email") String email) throws InternalException {
        User user = usersRepository.getByEmailAndActive(email, true);
        if (user == null) {
            throw new InternalException(ErrorCode.ErrorCodes.INVALID_EMAIL_FORMAT, "Email не найден");
        }
        user.setApproved(true);
        usersRepository.save(user);
        RedirectView redirectView = new RedirectView();
        String redirectUrl = "http://localhost:4200/auth/login";
        redirectView.setUrl(redirectUrl);
        return redirectView;
    }

    @GetMapping("/signup/confirm/{token}")
    public ResponseEntity<User> confirmSignUp(@PathVariable(value = "token") String token) throws InternalException {
        User signUpUser = usersService.findBySignupToken(token);

        if (signUpUser == null || token == null) {
            throw new InternalException(ErrorCode.ErrorCodes.INVALID_TOKEN, "Неверный токен.");
        }
        signUpUser.setSignupToken("");
        signUpUser.setActive(true);

        return ResponseEntity.ok(usersService.create(signUpUser));

    }

    @PostMapping(PUBLIC_URL + "/v1/verify/phone")
    public ResponseEntity<String> verifyPhone(@RequestParam String phone, String code) {
        return smsService.verifyEmail(phone, code);
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<User> resetPassword(@PathVariable(value = "token") String token, @RequestBody String password) throws InternalException {
        User signUpUser = usersService.findBySignupToken(token);

        if (signUpUser == null) {
            throw new InternalException(ErrorCode.ErrorCodes.INVALID_TOKEN, "Неверный токен.");
        }
        signUpUser.setPassword(bCryptPasswordEncoder.encode(password));
        signUpUser.setSignupToken("");
        return ResponseEntity.ok(usersService.create(signUpUser));
    }

    @PutMapping(value = PRIVATE_URL + "/v1/edit")
    public ResponseEntity<User> updateUser(@RequestBody ClientsDTO user) {
        return ResponseEntity.ok(usersService.editClient(user));
    }

    @PostMapping(value = PRIVATE_URL + "/v1/changePassword")
    public ResponseEntity<Object> changePassword(@RequestParam Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return ResponseEntity.ok(usersService.changePassword(id, oldPassword, newPassword));
    }

    @PostMapping(value = PRIVATE_URL + "/v1/createNewPassword")
    public ResponseEntity<Object> createPassword(@RequestParam Long id, @RequestParam String newPassword) {
        return ResponseEntity.ok(usersService.createPassword(id, newPassword));
    }

    @GetMapping(PUBLIC_URL + "/v1/users/details")
    public ResponseEntity<PageableCustom> getUserDetailsList(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(usersService.findAllUsersDetail(params));
    }
}
