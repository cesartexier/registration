package com.registration.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.registration.model.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class CreateUserForm {

    @NotNull
    @Valid
    private String userName;

    @NotNull
    @Valid
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull
    @Valid
    private String countryCode;

    @Valid
    private String phoneNumber;

    @Valid
    private Gender gender;
}
