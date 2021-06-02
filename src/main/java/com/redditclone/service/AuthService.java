package com.redditclone.service;

import com.redditclone.dto.AuthenticationResponse;
import com.redditclone.dto.LoginRequest;
import com.redditclone.dto.RefreshTokenRequest;
import com.redditclone.dto.RegisterRequest;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.NotificationEmail;
import com.redditclone.model.User;
import com.redditclone.model.VerificationToken;
import com.redditclone.repository.UserRepository;
import com.redditclone.repository.VerificationTokenRepository;
import com.redditclone.security.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    /*Constructor Injection. Constructors will be created by @AllArgsConstructor (lombok)
    * */
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Encrypt Password
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token=generateVerificationToken(user);
    }

    private String generateVerificationToken(User user){
        String generatedToken= UUID.randomUUID().toString();
        VerificationToken verificationToken =new VerificationToken();
        verificationToken.setToken(generatedToken);
        verificationToken.setUser(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account",user.getEmail(),"Thank you for signing up to spring reddit, " +
                "Please click on the below url to activate your account:" +
                "http://localhost:8080/api/auth/accountVerification/"+ generatedToken) );
        verificationTokenRepository.save(verificationToken);

        return generatedToken;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
            String username=verificationToken.getUser().getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User not found!!!"+username));
            user.setEnabled(true);
            userRepository.save(user);

    }

    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String s = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(s)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefershToken(refreshTokenRequest.getRefreshToken());
        String token=jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();

    }
}
