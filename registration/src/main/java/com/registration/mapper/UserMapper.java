package com.registration.mapper;


import com.registration.controller.resource.UserResource;
import com.registration.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for the User entity
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    /**
     * map userResource to User entity
     *
     * @param form the UserResource to map
     * @return User entity
     */
    User toEntity(UserResource form);

    /**
     * map User entity to userResource
     *
     * @param user the User entity to map
     * @return UserResource
     */
    UserResource toResource(User user);
}
