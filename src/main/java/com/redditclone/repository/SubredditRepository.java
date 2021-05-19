package com.redditclone.repository;

import com.redditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository <Subreddit,Long>{
    Optional<Subreddit> findByname(String subredditName);
}
