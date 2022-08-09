package com.registration.controller;

import com.registration.annotation.LogExecutionTime;
import com.registration.controller.resource.UserResource;
import com.registration.exception.ResourceNotFoundException;
import com.registration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User api controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/private/api/users")
public class UserController {

    private final UserService userService;

    /**
     * create user from createUserForm
     *
     * @param form form with user parameters
     * @return userResource
     * @throws IllegalArgumentException ex => if user parameters invalid
     */
    @RequestMapping(method = RequestMethod.POST)
    @LogExecutionTime
    public ResponseEntity<UserResource> createUser(@RequestBody UserResource form) throws IllegalArgumentException {

        return new ResponseEntity<>(this.userService.createUser(form), HttpStatus.CREATED);

    }

    /**
     * get user by id
     *
     * @param id id of user
     * @return userResource
     * @throws ResourceNotFoundException ex => if user with id not found
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @LogExecutionTime
    public ResponseEntity<UserResource> getUser(@PathVariable Long id) throws ResourceNotFoundException {

        return new ResponseEntity<>(this.userService.getUser(id), HttpStatus.OK);

    }
}
