package com.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.registration.controller.form.CreateUserForm;
import com.registration.controller.resource.UserResource;
import com.registration.exception.ResourceNotFoundException;
import com.registration.model.Gender;
import com.registration.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void testCreateUser() throws Exception {
        final UserResource userResource = new UserResource();
        userResource.setUserName("userName");
        userResource.setBirthday(LocalDate.of(2000, 1, 1));
        userResource.setCountryCode("FR");
        userResource.setPhoneNumber("123456789");
        userResource.setGender(Gender.M);


        final CreateUserForm form = new CreateUserForm();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2000, 1, 1));
        form.setCountryCode("FR");
        form.setPhoneNumber("123456789");
        form.setGender(Gender.M);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String formAsString = objectMapper.writeValueAsString(form);

        when(mockUserService.createUser(form)).thenReturn(userResource);

        final MockHttpServletResponse response = mockMvc.perform(post("/private/api/users/create")
                .content(formAsString).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testGetUser() throws Exception {
        final UserResource userResource = new UserResource();
        userResource.setUserName("userName");
        userResource.setBirthday(LocalDate.of(2000, 1, 1));
        userResource.setCountryCode("FR");
        userResource.setPhoneNumber("phoneNumber");
        userResource.setGender(Gender.M);
        when(mockUserService.getUser(0L)).thenReturn(userResource);

        final MockHttpServletResponse response = mockMvc.perform(get("/private/api/users/{id}", 0)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
    }

    @Test
    void testGetUser_UserServiceThrowsResourceNotFoundException() throws Exception {

        when(mockUserService.getUser(0L)).thenThrow(ResourceNotFoundException.class);

        final MockHttpServletResponse response = mockMvc.perform(get("/private/api/users/{id}", 0)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
