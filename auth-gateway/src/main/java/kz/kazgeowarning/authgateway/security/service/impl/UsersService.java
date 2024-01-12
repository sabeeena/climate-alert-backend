package kz.kazgeowarning.authgateway.security.service.impl;

import kz.kazgeowarning.authgateway.dto.ClientDetailDTO;
import kz.kazgeowarning.authgateway.dto.ClientsDTO;
import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.ClientDetailDTORepository;
import kz.kazgeowarning.authgateway.repository.UserRepository;
import kz.kazgeowarning.authgateway.security.service.IUsersService;
import kz.kazgeowarning.authgateway.util.PageableCustom;
import kz.kazgeowarning.authgateway.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public User createOrUpdateUser(ClientsDTO clientDTO) {
        User client;
        if (clientDTO.getId() == null) {
            client = User
                    .builder()
                    .email(clientDTO.getEmail())
                    .firstName(clientDTO.getName())
                    .city(clientDTO.getCity())
                    .role(Role.ROLE_USER)
                    .loginType(clientDTO.getLoginType())
                    .active(true)
                    .password(bCryptPasswordEncoder.encode(clientDTO.getPassword()))
                    .registerDate(new Timestamp(new Date().getTime()))
                    .build();
        } else {
            client = findUserById(clientDTO.getId());
            assert client != null;
            client.setEmail(clientDTO.getEmail());
            client.setFirstName(clientDTO.getName());
            client.setLastName(clientDTO.getSurname());
            client.setRole(Role.ROLE_USER);
            client.setLoginType(client.getLoginType());
            client.setActive(true);
            client.setPassword(client.getPassword());
            client.setCity(clientDTO.getCity());
            client.setPhoneNumber(clientDTO.getPhoneNumber());
        }

        return usersRepository.saveAndFlush(client);
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
    public User editClient(ClientsDTO clientsDTO) {
        User client = findUserById(clientsDTO.getId());
        if (client != null) {
            client.setCity(clientsDTO.getCity());
            client.setPhoneNumber(clientsDTO.getPhoneNumber());
            client.setBirthDate(clientsDTO.getBirthDate());
            client.setStorageUrl(clientsDTO.getStorageUrl());
            client.setFirstName(clientsDTO.getName());
            client.setLastName(client.getLastName());
            client.setEmail(clientsDTO.getEmail());
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


}
