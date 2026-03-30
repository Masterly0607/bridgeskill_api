package com.aditi_final.bridgeskill_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
// + What is RegisterResponse file?
// This class is used to store the response data for register.
// So after register success, backend wants to return something like: { "message": "User registered successfully"}
// That response data is stored inside a RegisterResponse object.
// + Why Getter and Setter? You control how data is read or changed through methods.Instead of letting other classes touch variable directly, we let them go through methods.
// Example
@Getter
@Setter
@AllArgsConstructor // create constructor with all fields
public class RegisterResponse {
    private String message;
}