package com.redditclone.service;

import com.redditclone.dto.RegisterRequest;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.NotificationEmail;
import com.redditclone.model.User;
import com.redditclone.model.VerificationToken;
import com.redditclone.repository.UserRepository;
import com.redditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
}
