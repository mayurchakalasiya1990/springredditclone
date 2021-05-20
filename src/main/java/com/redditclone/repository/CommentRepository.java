package com.redditclone.repository;

import com.redditclone.model.Comment;
import com.redditclone.model.Post;
import com.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

public interface CommentRepository extends JpaRepository<Comment,Long> {
     List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
