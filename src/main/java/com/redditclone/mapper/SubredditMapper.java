package com.redditclone.mapper;

import com.redditclone.dto.SubredditDto;
import com.redditclone.model.Post;
import com.redditclone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPost", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default  Integer mapPosts(List<Post> numbersOfPost){return numbersOfPost.size();}

    @InheritInverseConfiguration
    @Mapping(target="posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
