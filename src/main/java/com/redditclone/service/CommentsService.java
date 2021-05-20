package com.redditclone.service;

import com.redditclone.dto.CommentsDto;
import com.redditclone.exception.PostNotFoundException;
import com.redditclone.mapper.CommentMapper;
import com.redditclone.model.Comment;
import com.redditclone.model.NotificationEmail;
import com.redditclone.model.Post;
import com.redditclone.model.User;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentsService {

    private final String POST_URL="";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public void save(CommentsDto commentsDto) {
        Post post=postRepository.findById(commentsDto.getPostId()).orElseThrow(()->new PostNotFoundException(commentsDto.getPostId()+" Post not found."));
        User user =authService.getCurrentUser();
        Comment comment=commentMapper.map(commentsDto,post,user);
        commentRepository.save(comment);
        String message =mailContentBuilder.build(user.getUsername()+" posted a comment on your post"+ POST_URL);
        sendCommentNotification(message,user);
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername()+" Commented on your post ",user.getEmail(),message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new PostNotFoundException(postId+" Post not Found."));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());

    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user=userRepository.findByUsername(userName).orElseThrow(()->new UsernameNotFoundException(userName));
        return  commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
