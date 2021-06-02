package com.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String authenticationToken;
    private String username;
    private String refreshToken;
    private Instant expiresAt;


}
