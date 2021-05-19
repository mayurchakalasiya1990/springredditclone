package com.redditclone.service;

import com.redditclone.dto.PostRequest;
import com.redditclone.dto.PostResponse;
import com.redditclone.exception.PostNotFoundException;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.exception.SubredditNotFoundException;
import com.redditclone.mapper.PostMapper;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import com.redditclone.model.User;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.SubredditRepository;
import com.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {


    private SubredditRepository subredditRepository;
    private AuthService authService;
    private PostMapper postMapper;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Transactional
    public void save(PostRequest postRequest) {
        Subreddit subreddit=subredditRepository.findByname(postRequest.getSubredditName())
                .orElseThrow(()->new SubredditNotFoundException(postRequest.getSubredditName()+" not found"));
        User user=authService.getCurrentUser();
        postRepository.save(postMapper.map(postRequest,subreddit,user));
    }

    @Transactional(readOnly=true)
    public PostResponse getPost(Long id){
        Post post=postRepository.findById(id).orElseThrow(()->new PostNotFoundException(id.toString()+" Post not found!!!"));
        return  postMapper.mapToDto(post);
    }

    @Transactional(readOnly=true)
    public List<PostResponse> getAllPost(){
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostByUsername(String username){
        User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostBySubreddit(Long id){
        Subreddit subreddit=subredditRepository.findById(id).orElseThrow(()->new SubredditNotFoundException(id.toString()+" Subreddit not found."));
        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
