package com.redditclone.service;

import com.redditclone.dto.RefreshTokenRequest;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.RefreshToken;
import com.redditclone.repository.RefreshTokenRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private RefreshTokenRespository refreshTokenRespository;
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRespository.save(refreshToken);
    }

    void validateRefershToken(String token){
        refreshTokenRespository.findByToken(token).orElseThrow(()->new SpringRedditException("Invalid Refresh token."));
    }

    public void deleteTokenRefreshToken(String token){
        refreshTokenRespository.deleteByToken(token);
    }
}
