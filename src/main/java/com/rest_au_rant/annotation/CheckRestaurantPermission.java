package com.rest_au_rant.annotation;

import com.rest_au_rant.model.IdType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRestaurantPermission {
    IdType value();
}