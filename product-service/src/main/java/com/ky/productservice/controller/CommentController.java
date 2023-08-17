package com.ky.productservice.controller;

import com.ky.productservice.dto.comment.CommentDto;
import com.ky.productservice.dto.comment.CreateCommentRequest;
import com.ky.productservice.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequest createCommentRequest){
        return ResponseEntity.ok(commentService.createComment(createCommentRequest));
    }
}
