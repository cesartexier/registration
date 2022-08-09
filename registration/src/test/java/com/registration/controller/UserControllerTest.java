package com.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.registration.controller.resource.UserResource;
import com.registration.exception.ResourceNotFoundException;
import com.registration.mapper.UserMapper;
import com.registration.mapper.UserMapperImpl;
import com.registration.model.Gender;
import com.registration.model.User;
import com.registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserMapper mockUserMapper;

    @MockBean
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        this.mockUserMapper = new UserMapperImpl();
    }

    @Test
    void testCreateUser() throws Exception {
        final UserResource form = new UserResource();
        form.setUserName("userName");
        form.setBirthday(LocalDate.of(2000, 1, 1));
        form.setCountryCode("FR");
        form.setPhoneNumber("123456789");
        form.setGender(Gender.M);

        var user = this.mockUserMapper.toEntity(form);
        user.setId(1L);

        when(mockUserRepository.save(user)).thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String formAsString = objectMapper.writeValueAsString(form);

        final MockHttpServletResponse response = mockMvc.perform(post("/private/api/users")
                .content(formAsString).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testGetUser() throws Exception {
        final User user = new User();
        user.setUserName("userName");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setCountryCode("FR");
        user.setPhoneNumber("phoneNumber");
        user.setGender(Gender.M);

        when(mockUserRepository.findById(0L)).thenReturn(Optional.of(user));

        final MockHttpServletResponse response = mockMvc.perform(get("/private/api/users/{id}", 0)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetUser_UserServiceThrowsResourceNotFoundException() throws Exception {

        when(mockUserRepository.findById(0L)).thenThrow(ResourceNotFoundException.class);

        final MockHttpServletResponse response = mockMvc.perform(get("/private/api/users/{id}", 0)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
