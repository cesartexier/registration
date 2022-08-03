package com.registration.controller.resource;

import com.registration.model.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserResource {

    private String userName;

    private LocalDate birthday;

    private String countryCode;

    private String phoneNumber;

    private Gender gender;
}
