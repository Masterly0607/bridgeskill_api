package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.client_profile.ClientProfileResponse;
import com.aditi_final.bridgeskill_api.dto.client_profile.CreateClientProfileRequest;
import com.aditi_final.bridgeskill_api.dto.client_profile.UpdateClientProfileRequest;
import com.aditi_final.bridgeskill_api.entity.ClientProfile;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.repository.ClientProfileRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    public ClientProfileResponse createProfile(CreateClientProfileRequest request) {
        User currentUser = getCurrentUser();

        if (clientProfileRepository.existsByUserId(currentUser.getId())) {
            throw new IllegalArgumentException("Client profile already exists");
        }

        ClientProfile clientProfile = ClientProfile.builder()
                .userId(currentUser.getId())
                .companyName(request.getCompanyName())
                .companyDescription(request.getCompanyDescription())
                .phone(request.getPhone())
                .build();

        ClientProfile savedProfile = clientProfileRepository.save(clientProfile);
        return mapToResponse(savedProfile);
    }

    public ClientProfileResponse getMyProfile() {
        User currentUser = getCurrentUser();

        ClientProfile clientProfile = clientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client profile not found"));

        return mapToResponse(clientProfile);
    }

    public ClientProfileResponse updateMyProfile(UpdateClientProfileRequest request) {
        User currentUser = getCurrentUser();

        ClientProfile clientProfile = clientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client profile not found"));

        clientProfile.setCompanyName(request.getCompanyName());
        clientProfile.setCompanyDescription(request.getCompanyDescription());
        clientProfile.setPhone(request.getPhone());

        ClientProfile updatedProfile = clientProfileRepository.save(clientProfile);
        return mapToResponse(updatedProfile);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ClientProfileResponse mapToResponse(ClientProfile clientProfile) {
        return ClientProfileResponse.builder()
                .id(clientProfile.getId())
                .userId(clientProfile.getUserId())
                .companyName(clientProfile.getCompanyName())
                .companyDescription(clientProfile.getCompanyDescription())
                .phone(clientProfile.getPhone())
                .createdAt(clientProfile.getCreatedAt())
                .updatedAt(clientProfile.getUpdatedAt())
                .build();
    }
}