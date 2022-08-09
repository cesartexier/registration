package com.registration.service;

import com.registration.controller.resource.UserResource;
import com.registration.exception.ResourceNotFoundException;
import com.registration.mapper.UserMapper;
import com.registration.model.User;
import com.registration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;

import static java.lang.String.format;
import static java.time.LocalDate.now;

/**
 * User Service
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * create user
     *
     * @param form the user creation form
     * @return userResource
     */
    @Transactional
    public UserResource createUser(UserResource form) {

        if (!form.getCountryCode().equals("FR")) {
            throw new IllegalArgumentException(format("Country [%s], forbidden", form.getCountryCode()));
        }

        var age = Period.between(form.getBirthday(), now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException(format("User under 18 can't create an account current is only %s, forbidden", age));
        }

        var user = this.userMapper.toEntity(form);
        this.userRepository.save(user);

        return this.userMapper.toResource(user);
    }

    /**
     * get user by id
     *
     * @param id the user id
     * @return userResource
     * @throws ResourceNotFoundException ex
     */
    @Transactional(readOnly = true)
    public UserResource getUser(Long id) throws ResourceNotFoundException {

        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(format("User with ID [%s], not found", id)));

        return this.userMapper.toResource(user);
    }

}
