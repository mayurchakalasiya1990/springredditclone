package com.redditclone.controller;

import com.redditclone.dto.CommentsDto;
import com.redditclone.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private CommentsService commentsService;

    @PostMapping
    public void createComment(@RequestBody CommentsDto commentsDto){
        commentsService.save(commentsDto);
    }
}
