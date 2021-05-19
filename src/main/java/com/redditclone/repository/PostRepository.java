package com.redditclone.repository;

import com.redditclone.dto.PostResponse;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import com.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUser(User user);

    List<Post> findAllBySubreddit(Subreddit subreddit);
}
