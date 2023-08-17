package com.ky.productservice.service;

import com.ky.productservice.dto.comment.CommentDto;
import com.ky.productservice.dto.comment.CommentMapper;
import com.ky.productservice.dto.comment.CreateCommentRequest;
import com.ky.productservice.model.Comment;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ProductService productService;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, ProductService productService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.productService = productService;
    }

    public CommentDto createComment(CreateCommentRequest request){
        Product product = productService.getProductById(request.getProductId());

        Comment comment = Comment.builder()
                .product(product)
                .text(request.getText())
                .createdDate(LocalDateTime.now())
                .creator(request.getCreator())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.commentToCommentDto(savedComment);

    }
}
