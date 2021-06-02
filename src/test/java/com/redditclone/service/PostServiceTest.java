package com.redditclone.service;

import com.redditclone.dto.PostRequest;
import com.redditclone.dto.PostResponse;
import com.redditclone.mapper.PostMapper;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import com.redditclone.model.User;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.SubredditRepository;
import com.redditclone.repository.UserRepository;
import com.redditclone.service.AuthService;
import com.redditclone.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private SubredditRepository subredditRepositor;
    @Mock
    private AuthService authService;
    @Mock
    private PostMapper postMapper ;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    PostService postService;
    @BeforeEach
    public void setup(){
        postService=new PostService(subredditRepositor,authService, postMapper, postRepository, userRepository);
    }

    @Test
    @DisplayName("should Find post by id")
    void shouldFindById() {

        Post post= Post.builder().postId(123L).postName("first Post").url("http://url.site").description("test").createdDate(Instant.now()).voteCount(0).build();
        PostResponse expectedPostResponse=new PostResponse(123l,"Fist post","http://url.site","Test","Test user","Test Subreddit",0,0,"1 hour ago",false,false);
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);
        PostResponse actualPostResponse= postService.getPost(123L);
        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should Save Posts")
    void shouldSavePost(){
        postService=new PostService(subredditRepositor,authService, postMapper, postRepository, userRepository);
        Post post= Post.builder().postId(123L).postName("first Post").url("http://url.site").description("test").createdDate(Instant.now()).voteCount(0).build();
        User currentUser= User.builder().userId(123L).username("test user").password("secret password").email("user@email.com").created(Instant.now()).enabled(true).build();
        Subreddit subreddit=Subreddit.builder().id(123L).name("First Subreddit").description("Subreddit descirption").user(currentUser).posts(Collections.emptyList()).build();
        PostRequest postRequest=new PostRequest(null,"First Subreddit","First Post","http://url.site","Test");
        Mockito.when(subredditRepositor.findByname("First Subreddit")).thenReturn(Optional.of(subreddit));
        Mockito.when(postMapper.map(postRequest,subreddit,currentUser)).thenReturn(post);
        Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
        postService.save(postRequest);
        //Mockito.verify(postRepository,Mockito.times(1)).save(ArgumentMatchers.any(Post .class));
        Mockito.verify(postRepository,Mockito.times(1)).save(postArgumentCaptor.capture());
        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("first Post");
    }

}