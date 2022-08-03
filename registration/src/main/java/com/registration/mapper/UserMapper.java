package com.registration.mapper;


import com.registration.controller.form.CreateUserForm;
import com.registration.controller.resource.UserResource;
import com.registration.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserForm form);

    UserResource toResource(User user);
}
