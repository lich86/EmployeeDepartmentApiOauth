package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.dto.UserDTO;
import com.chervonnaya.employeedepartmentapi.model.User;
import com.chervonnaya.employeedepartmentapi.repository.UserRepository;
import com.chervonnaya.employeedepartmentapi.service.mappers.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, UserDTO, UserRepository> implements UserDetailsService {

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        super(repository, User.class, mapper);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(email)
            .map(user -> new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }

}
