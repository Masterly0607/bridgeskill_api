package com.aditi_final.bridgeskill_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeResponse {
    private String fullName;
    private String email;
    private Long roleId;
}