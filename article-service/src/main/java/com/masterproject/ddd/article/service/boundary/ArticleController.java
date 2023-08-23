package com.masterproject.ddd.article.service.boundary;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterproject.ddd.article.service.model.Article;
import com.masterproject.ddd.article.service.model.dtos.ArticleCreationRequestDTO;
import com.masterproject.ddd.article.service.model.dtos.UpdateArticleRequestDTO;
import com.masterproject.ddd.core.api.article.command.CloseArticle;
import com.masterproject.ddd.core.api.article.command.CreateArticle;
import com.masterproject.ddd.core.api.article.command.UpdateArticle;
import com.masterproject.ddd.core.api.article.model.ArticleStatus;
import com.masterproject.ddd.core.api.article.query.GetArticleByID;
import com.masterproject.ddd.core.api.article.query.GetArticlesByStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/articles")
@Tag(name = "Article Controller", description = "Provides article details")
public class ArticleController {

        private final CommandGateway commandGateway;
        private final QueryGateway queryGateway;

        @PostMapping("/")
        @Operation(summary = "Create new article Command.", description = "Create a new article with the provided details")
        public CompletableFuture<String> createArticle(@Valid @RequestBody ArticleCreationRequestDTO requestDTO) {
                UUID newId = UUID.randomUUID();
                return commandGateway
                                .send(CreateArticle.builder().Id(newId.toString()).authorID(requestDTO.getAuthorID())
                                                .authorName(requestDTO.getAuthorName())
                                                .articleDetails(requestDTO.getArticleDetails()).build())
                                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> newId.toString()));
        }

        @PutMapping("/{articleId:.+}/update")
        @Operation(summary = "Update article Command.", description = "Update an article with the provided details")
        public CompletableFuture<String> updateArticle(@PathVariable @NotEmpty final String articleId,
                        @Valid @RequestBody UpdateArticleRequestDTO requestDTO) {
                return commandGateway
                                .send(UpdateArticle.builder().Id(articleId)
                                                .authorName(requestDTO.getAuthorName())
                                                .articleDetails(requestDTO.getArticleDetails()).build())
                                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> "Article has been updated"));
        }

        @PostMapping("/{articleId:.+}/close")
        @Operation(summary = "Close article Command.", description = "Close an article, which can later be published.")
        public CompletableFuture<String> closeArticle(@PathVariable @NotEmpty final String articleId) {
                return commandGateway
                                .send(CloseArticle.builder().Id(articleId).build())
                                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> "Article has been updated"));
        }

        @GetMapping("/{articleId:.+}")
        @Operation(summary = "Get a specific Article Query", description = "Get an article by ID")
        public CompletableFuture<Article> getArticleById(@PathVariable @NotEmpty final String articleId) {
                return queryGateway.query(GetArticleByID.builder().Id(articleId).build(),
                                ResponseTypes.instanceOf(Article.class));
        }

        @GetMapping("/status/{status:.+}")
        @Operation(summary = "Get all Article by status Query", description = "Get all system created articles with the given status")
        public CompletableFuture<List<Article>> getAllArticleByStatus(@PathVariable final ArticleStatus status) {
                return queryGateway.query(GetArticlesByStatus.builder().status(status).build(),
                                ResponseTypes.multipleInstancesOf(Article.class));
        }

}
