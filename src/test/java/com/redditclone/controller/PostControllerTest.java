package com.redditclone.controller;

import com.redditclone.dto.PostResponse;
import com.redditclone.security.JWTProvider;
import com.redditclone.service.PostService;
import com.redditclone.service.UserdetailsServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private UserdetailsServiceImpl userDetailsService;
    @MockBean
    private JWTProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts")
    public void shouldListAllPost() throws Exception{
        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User 1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);
        PostResponse postRequest2 = new PostResponse(2L, "Post Name 2", "http://url2.site2", "Description2", "User 2",
                "Subreddit Name 2", 0, 0, "2 days ago", false, false);

        Mockito.when(postService.getAllPost()).thenReturn(asList(postRequest1, postRequest2));

        mockMvc.perform(get("/api/posts/"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].postName", Matchers.is("Post Name")))
                .andExpect(jsonPath("$[0].url", Matchers.is("http://url.site")))
                .andExpect(jsonPath("$[1].url", Matchers.is("http://url2.site2")))
                .andExpect(jsonPath("$[1].postName", Matchers.is("Post Name 2")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("Should find Post When making GET request to endpoint - /api/posts/{1}")
    public void shouldFindPost() throws Exception{
        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User 1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);

        Long postId=1L;
        Mockito.when(postService.getPost(postId)).thenReturn(postRequest1);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.postName", Matchers.is("Post Name")))
                .andExpect(jsonPath("$.url", Matchers.is("http://url.site")));
    }

    @Test
    @DisplayName("Should List All Posts by username When making GET request to endpoint - /api/posts/by-user/{name}")
    public void shouldAllPostByUsername() throws Exception{
        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User_1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);
        PostResponse postRequest2 = new PostResponse(2L, "Post Name 2", "http://url2.site2", "Description2", "User_1",
                "Subreddit Name 2", 0, 0, "2 days ago", false, false);

        Mockito.when(postService.getPostByUsername("User_1")).thenReturn(asList(postRequest1, postRequest2));

        mockMvc.perform(get("/api/posts/by-user/User_1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].postName", Matchers.is("Post Name")))
                .andExpect(jsonPath("$[0].url", Matchers.is("http://url.site")))
                .andExpect(jsonPath("$[1].url", Matchers.is("http://url2.site2")))
                .andExpect(jsonPath("$[1].postName", Matchers.is("Post Name 2")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }


    @Test
    @DisplayName("Should List All Posts by SubredditName When making GET request to endpoint - /api/posts/by-user/{name}")
    public void shouldAllPostBySubredditName() throws Exception{
        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User_1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);
        PostResponse postRequest2 = new PostResponse(2L, "Post Name 2", "http://url2.site2", "Description2", "User_1",
                "Subreddit Name 2", 0, 0, "2 days ago", false, false);

        Mockito.when(postService.getPostBySubreddit(1L)).thenReturn(asList(postRequest1, postRequest2));

        mockMvc.perform(get("/api/posts/by-subreddit/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].postName", Matchers.is("Post Name")))
                .andExpect(jsonPath("$[0].url", Matchers.is("http://url.site")))
                .andExpect(jsonPath("$[1].url", Matchers.is("http://url2.site2")))
                .andExpect(jsonPath("$[1].postName", Matchers.is("Post Name 2")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }
}
