package com.redditclone.mapper;

import com.redditclone.dto.CommentsDto;
import com.redditclone.model.Comment;
import com.redditclone.model.Post;
import com.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "text",source = "commentsDto.text")
    @Mapping(target = "post",source = "post")
    @Mapping(target = "createdDate" ,expression = "java(java.time.Instant.now())")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId",expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName",expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}
