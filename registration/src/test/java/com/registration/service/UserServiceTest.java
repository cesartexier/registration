package com.registration.service;

import com.registration.controller.resource.UserResource;
import com.registration.exception.ResourceNotFoundException;
import com.registration.mapper.UserMapper;
import com.registration.mapper.UserMapperImpl;
import com.registration.model.Gender;
import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserMapper mockUserMapper;

    @Mock
    private UserRepository mockUserRepository;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        this.mockUserMapper = new UserMapperImpl();
        userServiceUnderTest = new UserService(mockUserMapper, mockUserRepository);
    }


    @Test
    void testCreateUser() {
        // GIVEN
        final UserResource form = new UserResource();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2000, 1, 1));
        form.setCountryCode("FR");
        form.setPhoneNumber("123456789");
        form.setGender(Gender.M);

        // WHEN
        final UserResource result = userServiceUnderTest.createUser(form);

        // THEN
        assertEquals(result.getUserName(), form.getUserName());
    }

    @Test
    void testCreateUser_without_phoneNumber_and_gender() {
        // GIVEN
        final UserResource form = new UserResource();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2000, 1, 1));
        form.setCountryCode("FR");

        // WHEN
        final UserResource result = userServiceUnderTest.createUser(form);

        // THEN
        assertEquals(result.getUserName(), form.getUserName());
    }

    @Test
    void testCreateUser_minor_throw_IllegalArgumentException() {
        final UserResource form = new UserResource();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2020, 1, 1));
        form.setCountryCode("FR");
        form.setPhoneNumber("123456789");
        form.setGender(Gender.M);

        assertThatThrownBy(() -> userServiceUnderTest.createUser(form)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateUser_not_french_throw_IllegalArgumentException() {
        final UserResource form = new UserResource();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2000, 1, 1));
        form.setCountryCode("EN");
        form.setPhoneNumber("123456789");
        form.setGender(Gender.M);

        assertThatThrownBy(() -> userServiceUnderTest.createUser(form)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void testGetUser() {
        // GIVEN
        final User user1 = new User();
        user1.setId(0L);
        user1.setUserName("userName");
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setCountryCode("FR");
        user1.setPhoneNumber("123456789");
        user1.setGender(Gender.M);
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // WHEN
        final UserResource result = userServiceUnderTest.getUser(0L);

        // THEN
        assertEquals(result.getUserName(), this.mockUserMapper.toResource(user1).getUserName());

    }

    @Test
    void testGetUser_ThrowsResourceNotFoundException() {
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceUnderTest.getUser(0L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
