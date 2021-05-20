package com.redditclone.service;

import com.redditclone.dto.VoteDto;
import com.redditclone.exception.PostNotFoundException;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.model.Post;
import com.redditclone.model.Vote;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
import static  com.redditclone.model.VoteType.*;

@AllArgsConstructor
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {

        Post post=postRepository.findById(voteDto.getPostId()).orElseThrow(()->new PostNotFoundException(voteDto.getPostId()+" Post not found."));
        Optional<Vote> voteByUserAndPost= voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());

        if (voteByUserAndPost.isPresent() && voteByUserAndPost.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already "+voteDto.getVoteType()+"'d for this post");
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }else{
            post.setVoteCount(post.getVoteCount()-1);
        }
        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto,Post post){

        return  Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser()).build();
    }



}
