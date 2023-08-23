package com.masterproject.ddd.core.api.user.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDetails {

    @NotBlank(message = "Username may not be empty")
    private String userName;
    private String name;
    @NotBlank(message = "E-Mail can not be empty")
    private String email;

}