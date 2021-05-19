package com.redditclone.service;

import com.redditclone.dto.SubredditDto;
import com.redditclone.exception.SpringRedditException;
import com.redditclone.mapper.SubredditMapper;
import com.redditclone.model.Subreddit;
import com.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private SubredditRepository subredditRepository;
    private SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto saveSubreddit(SubredditDto subredditDto){
        Subreddit  subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    /*private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName()).description(subredditDto.getDescription()).build();
    }*/

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
        
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit= subredditRepository.findById(id).orElseThrow(()->new SpringRedditException("No Subreddit found with id:"+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

    /*private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().name(subreddit.getName()).
                description(subreddit.getDescription()).build();
    }*/
}
