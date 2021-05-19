package com.redditclone.service;

import com.redditclone.dto.CommentsDto;
import com.redditclone.exception.PostNotFoundException;
import com.redditclone.model.User;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentsService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    public void save(CommentsDto commentsDto) {
        postRepository.findById(commentsDto.getPostId()).orElseThrow(()->new PostNotFoundException(commentsDto.getPostId()+" Post not found."));
        User user=authService.getCurrentUser();
    }

}
