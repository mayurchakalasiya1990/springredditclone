package com.redditclone.repository;

import com.redditclone.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Container
    MySQLContainer mySQLContainer=new MySQLContainer("mysql:lastest")
            .withDatabaseName("spring-reddit-db")
            .withUsername("testuser")
            .withPassword("pass");

    @Autowired
    private PostRepository postRepository;

    @Test
    public void shouldSavePost(){
        Post expectedPostObject= Post.builder().postId(123L).postName("first Post").url("http://url.site").description("test").createdDate(Instant.now()).voteCount(0).build();
        Post actualPostObject =postRepository.save(expectedPostObject);
        assertThat(actualPostObject).usingRecursiveComparison().ignoringFields("postId").isEqualTo(expectedPostObject);
    }
}
