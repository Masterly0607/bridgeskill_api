package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.client_profile.ClientProfileResponse;
import com.aditi_final.bridgeskill_api.dto.client_profile.CreateClientProfileRequest;
import com.aditi_final.bridgeskill_api.dto.client_profile.UpdateClientProfileRequest;
import com.aditi_final.bridgeskill_api.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client-profile")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @PostMapping
    public ResponseEntity<ClientProfileResponse> createProfile(
            @Valid @RequestBody CreateClientProfileRequest request
    ) {
        ClientProfileResponse response = clientProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ClientProfileResponse> getMyProfile() {
        ClientProfileResponse response = clientProfileService.getMyProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ClientProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateClientProfileRequest request
    ) {
        ClientProfileResponse response = clientProfileService.updateMyProfile(request);
        return ResponseEntity.ok(response);
    }
}