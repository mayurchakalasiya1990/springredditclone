package com.redditclone.repository;

import com.redditclone.model.User;
import com.redditclone.repository.UserRepository;
import jdk.jfr.Description;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbedded {


    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser(){
        User user = User.builder().userId(null).enabled(true).username("testUser").password("secret password").email("user@email.com").build();
        User savedUser=userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }

    @Test
    @Sql("classpath:test-data.sql")
    @Description("Disable user to test updateUser method")
    public void shouldUpdateUsers(){
        Optional<User> test=userRepository.findByUsername("testuser_sql");
        User user=test.get();
        user.setEnabled(false);
        User updatedUser=userRepository.save(user);
        assertThat(updatedUser.isEnabled()).isEqualTo(true);
    }

    @Test
    @Sql("classpath:test-data.sql")
    @Description("Delete user to test deleteUser method")
    public void shouldDeletUsers(){
        User user=userRepository.findByUsername("testuser_sql").get();
        userRepository.delete(user);
        boolean deletedUser=userRepository.findByUsername("testuser_sql").isPresent();
        assertThat(deletedUser).isEqualTo(false);
    }

    @Test
    @Sql("classpath:test-data.sql")
    public void shouldSaveUsersThroughSqlFile(){
        Optional<User> test=userRepository.findByUsername("testuser_sql");
        assertThat(test).isNotEmpty();
    }


}
