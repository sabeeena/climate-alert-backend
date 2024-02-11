package kz.kazgeowarning.authgateway.security.service.impl;

import kz.kazgeowarning.authgateway.dto.AdminEmployeeDTO;
import kz.kazgeowarning.authgateway.dto.ClientDetailDTO;
import kz.kazgeowarning.authgateway.dto.ClientsDTO;
import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.enums.UserLoginType;
import kz.kazgeowarning.authgateway.model.AdminEmployee;
import kz.kazgeowarning.authgateway.model.RegisteredEmployee;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.AdminEmployeeRepository;
import kz.kazgeowarning.authgateway.repository.ClientDetailDTORepository;
import kz.kazgeowarning.authgateway.repository.RegisteredEmployeeRepository;
import kz.kazgeowarning.authgateway.repository.UserRepository;
import kz.kazgeowarning.authgateway.security.service.IUsersService;
import kz.kazgeowarning.authgateway.util.PageableCustom;
import kz.kazgeowarning.authgateway.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService implements IUsersService {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ClientDetailDTORepository clientDetailDTORepository;
    @Autowired
    private AdminEmployeeRepository adminEmployeeRepository;

    @Autowired
    private RegisteredEmployeeRepository registeredEmployeeRepository;

    @Override
    public List<User> findAll() throws DataAccessException {
        return usersRepository.findAll();
    }

    @Override
    public Long getSizeEmployee() {
        return this.usersRepository.getTotalSizeEmployee();
    }

    @Override
    public Page<User> findAllUsersPaged(Map<String, String> params) {
        var pageable = pageable(params);


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> iRoot = cq.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.and(
                cb.equal(iRoot.get("role"), Role.ROLE_USER)
        ));

        Predicate[] predArray = new Predicate[predicates.size()];
        predicates.toArray(predArray);

        cq.where(predArray);
        cq.orderBy(QueryUtils.toOrders(pageable.getSort(), iRoot, cb));

        TypedQuery<User> query = em.createQuery(cq);

        int totalRows = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, totalRows);
    }

    @Override
    public Pageable pageable(Map<String, String> params) {
        Sort dir = Sort.by("id").ascending();
        int pageSize = 1;
        int page = 0;

        if (params.containsKey("sorting")) {
            dir = params.get("sorting").charAt(0) == '-' ?
                    Sort.by(params.get("sorting").substring(1)).descending() :
                    Sort.by(params.get("sorting")).ascending();
        }
        if (params.containsKey("pageSize")) {
            pageSize = Integer.parseInt((params.get("pageSize")));
        }
        if (params.containsKey("pageNumber")) {
            page = Integer.parseInt((params.get("pageNumber")));
        }

        return PageRequest.of(page, pageSize, dir);
    }


    @Override
    public User createOrUpdateUser(ClientsDTO userDto) {
        User user;
        if (userDto.getId() == null) {
            user = User
                    .builder()
                    .email(userDto.getEmail())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .middleName(userDto.getMiddleName())
                    .birthDate(userDto.getBirthDate())
                    .phoneNumber(userDto.getPhoneNumber())
                    .city(userDto.getCity())
                    .role(userDto.getRole())
                    .imageUrl(userDto.getImageUrl())
                    .notifyEmail(userDto.isNotifyEmail())
                    .notifySms(userDto.isNotifySms())
                    .loginType(UserLoginType.origin)
                    .active(true)
                    .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .approved(true)
                    .registerDate(new Timestamp(new Date().getTime()))
                    .build();
        } else {
            user = findUserById(userDto.getId());
            assert user != null;
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setMiddleName(userDto.getMiddleName());
            user.setBirthDate(userDto.getBirthDate());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setImageUrl(userDto.getImageUrl());
            user.setNotifyEmail(userDto.isNotifyEmail());
            user.setNotifySms(userDto.isNotifySms());
            user.setRole(userDto.getRole());
            user.setLoginType(user.getLoginType());
            user.setActive(true);
            user.setCity(userDto.getCity());
            user.setPhoneNumber(userDto.getPhoneNumber());
        }

        return usersRepository.saveAndFlush(user);
    }

    @Override
    public User findUserByUsername(String username) throws DataAccessException {
        return usersRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException(User.class, username));
    }

    @Override
    public User findBySignupToken(String token) throws DataAccessException {
        return usersRepository.findBySignupToken(token);
    }

    @Override
    public User findUserById(Long id) throws DataAccessException {
        return usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws DataAccessException {
        return usersRepository.findByEmail(email);
    }

    @Override
    public User create(User user) {
        return usersRepository.save(user);
    }

    @Override
    public User editClient(ClientsDTO userDto) {
        User client = findUserById(userDto.getId());
        if (client != null) {
            client.setCity(userDto.getCity());
            client.setPhoneNumber(userDto.getPhoneNumber());
            client.setBirthDate(userDto.getBirthDate());
            client.setFirstName(userDto.getFirstName());
            client.setLastName(client.getLastName());
            client.setEmail(userDto.getEmail());
            client.setImageUrl(userDto.getImageUrl());
            return usersRepository.save(client);
        } else {
            return null;
        }
    }

    @Override
    public Boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = findUserById(id);
        if (user != null) {
            if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                this.usersRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(User user) {
        usersRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = findUserById(id);
        usersRepository.delete(user);
    }

    @Override
    public Boolean createPassword(Long id, String newPassword) {
        User user = findUserById(id);
        if (user != null) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setLoginType(null);
            this.usersRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public PageableCustom findAllUsersDetail(Map<String, String> params) {
        //TODO add filter
        int pageNumber = 0;
        int pageSize = 10;
        String searchKey = "";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case "pageNumber":
                    pageNumber = Integer.parseInt(entry.getValue());
                    break;
                case "pageSize":
                    pageSize = Integer.parseInt(entry.getValue());
                    break;
                case "search":
                    searchKey = entry.getValue();
                    break;
            }
        }


        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ClientDetailDTO> resultPage;

        resultPage =  clientDetailDTORepository.findAll(pageable) ;

        PageableCustom pageableCustom = new PageableCustom();
        pageableCustom.setTotal(resultPage.getTotalElements());
        pageableCustom.setPage(resultPage.getNumber());
        pageableCustom.setSize(resultPage.getSize());
        pageableCustom.setContent(resultPage.getContent());

        return pageableCustom;
    }


    public List<User> findByEmployeeRole() {
        return usersRepository.getTotalEmployee();
    }

    @Transactional
    public AdminEmployee registerEmployeeToAdmin(AdminEmployeeDTO adminEmployee){
        AdminEmployee existingAdmin = adminEmployeeRepository.findByAdminEmail(adminEmployee.getAdminEmail());
        Set<RegisteredEmployee> registeredEmployees;

        if (existingAdmin == null) {
            existingAdmin = new AdminEmployee();
            existingAdmin.setAdminEmail(adminEmployee.getAdminEmail());
            registeredEmployees = new HashSet<>();
        } else {
            registeredEmployees = existingAdmin.getEmployees();

            // Удалить сотрудников, которых нет в новом списке
            registeredEmployees.removeIf(existingEmployee ->
                    adminEmployee.getEmployees().stream()
                            .noneMatch(newEmployee -> newEmployee.getEmail().equals(existingEmployee.getEmployeeEmail()))
            );
        }

        // Добавить новых сотрудников
        for (Object obj : adminEmployee.getEmployees()) {
            if (obj instanceof User) {
                User employee = (User) obj;

                RegisteredEmployee registeredEmployee = new RegisteredEmployee();
                registeredEmployee.setFirstName(employee.getFirstName());
                registeredEmployee.setEmployeeEmail(employee.getEmail());
                registeredEmployee.setLastName(employee.getLastName());
                registeredEmployee.setAdmin(existingAdmin);
                registeredEmployees.add(registeredEmployee);
                registeredEmployeeRepository.save(registeredEmployee);
            }
        }

        existingAdmin.setEmployees(registeredEmployees);
        return adminEmployeeRepository.save(existingAdmin);

    }

    public Set<RegisteredEmployee> getEmployeesByAdminEmail(String adminEmail) {
        AdminEmployee admin = adminEmployeeRepository.findByAdminEmail(adminEmail);

        if (admin != null) {
            return admin.getEmployees();
        }

        return Collections.emptySet();
    }

    public Set<RegisteredEmployee> getRegisteredEmployeesByEmployee(String email) {
        Set<RegisteredEmployee>  employees = registeredEmployeeRepository.findAllEmployeeByEEmail(email);

        if (employees != null) {
            return employees;
        }

        return Collections.emptySet();
    }
}
