package com.aditi_final.bridgeskill_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String fullName;
    private String email;
    private Long roleId;
}