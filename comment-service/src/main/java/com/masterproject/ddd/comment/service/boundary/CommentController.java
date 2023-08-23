package com.masterproject.ddd.comment.service.boundary;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterproject.ddd.comment.service.model.dtos.CreateCommentRequestDTO;
import com.masterproject.ddd.core.api.comment.command.CreateComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/comments")
@Tag(name = "Comment Controller", description = "Provides article comment details")
public class CommentController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/")
    @Operation(summary = "Create new article Command.", description = "Create a new article with the provided details")
    public CompletableFuture<String> createComment(@Valid @RequestBody CreateCommentRequestDTO requestDTO) {
        UUID newId = UUID.randomUUID();
        return commandGateway
                .send(CreateComment.builder().Id(newId.toString())
                        .message(requestDTO.getMessage())
                        .articleId(requestDTO.getArticleId())
                        .authorId(requestDTO.getAuthorID())
                        .authorName(requestDTO.getAuthorName())
                        .build())
                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> newId.toString()));
    }
}
