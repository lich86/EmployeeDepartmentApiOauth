package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.dto.UserDTO;
import com.chervonnaya.employeedepartmentapi.exception.EntityNotFoundException;
import com.chervonnaya.employeedepartmentapi.exception.SaveEntityException;
import com.chervonnaya.employeedepartmentapi.model.User;
import com.chervonnaya.employeedepartmentapi.model.enums.Role;
import com.chervonnaya.employeedepartmentapi.repository.UserRepository;
import com.chervonnaya.employeedepartmentapi.service.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
public class UserServiceImpl extends CrudServiceImpl<User, UserDTO, UserRepository> implements UserDetailsService {

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper, LoginAttemptService loginAttemptService) {
        super(repository, User.class, mapper);
        this.loginAttemptService = loginAttemptService;
    }

    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new RuntimeException("blocked");
        }
        return repository.findByEmailIgnoreCase(email)
            .map(user -> new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return user.getRole().getAuthority();
                    }
                })
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }

    @Transactional
    @Override
    public User update(Long id, UserDTO d) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(this.genericType.getSimpleName(), id));
        Role role = user.getRole();
        try {
            d.setId(id);
            Role newRole = (mapper.map(d)).getRole();
            user = repository.save(mapper.map(d));
            String message = String.format("Updated %s, id: %d%s",
                this.genericType.getSimpleName().toLowerCase(), user.getId(),
                !newRole.equals(role) ? String.format(", user role changed to %s", newRole) : "");
            log.info(message);
            return user;
        } catch (Exception ex) {
            log.error(String.format("Unable to update user %d", id));
            throw new SaveEntityException(this.genericType.getSimpleName(), id, ex.getMessage());
        }

    }

}
