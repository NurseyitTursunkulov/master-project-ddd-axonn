package com.masterproject.ddd.comment.service.boundary;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterproject.ddd.comment.service.model.Comment;
import com.masterproject.ddd.comment.service.model.dtos.CreateCommentRequestDTO;
import com.masterproject.ddd.core.api.comment.command.CreateComment;
import com.masterproject.ddd.core.api.comment.query.GetCommentById;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByArticleId;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByAuthorId;

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
                        .build())
                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> newId.toString()));
    }

    @GetMapping("/{commentId:.+}")
    public CompletableFuture<Comment> getCommentsById(@PathVariable @NotEmpty final String commentId) {
        return queryGateway.query(GetCommentById.builder().id(commentId).build(),
                ResponseTypes.instanceOf(Comment.class));
    }

    @GetMapping("/article/{articleId:.+}")
    public CompletableFuture<List<Comment>> getCommentsByArticle(@PathVariable @NotEmpty final String articleId) {
        return queryGateway.query(GetCommentsByArticleId.builder().articleId(articleId).build(),
                ResponseTypes.multipleInstancesOf(Comment.class));
    }

    @GetMapping("/author/{authorId:.+}")
    public CompletableFuture<List<Comment>> getCommentsByAuthor(@PathVariable @NotEmpty final String authorId) {
        return queryGateway.query(GetCommentsByAuthorId.builder().authorId(authorId).build(),
                ResponseTypes.multipleInstancesOf(Comment.class));
    }

}
