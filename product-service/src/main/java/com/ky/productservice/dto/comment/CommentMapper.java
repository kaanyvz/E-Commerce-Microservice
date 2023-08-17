package com.ky.productservice.dto.comment;

import com.ky.productservice.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDto commentToCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .createdBy(comment.getCreator())
                .text(comment.getText())
                .build();
    }
}
