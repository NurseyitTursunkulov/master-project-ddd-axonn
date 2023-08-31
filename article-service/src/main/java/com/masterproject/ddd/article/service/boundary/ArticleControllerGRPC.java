package com.masterproject.ddd.article.service.boundary;

import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.lognet.springboot.grpc.GRpcService;

import com.masterproject.ddd.article.service.model.Article;
import com.masterproject.ddd.core.api.article.command.CloseArticle;
import com.masterproject.ddd.core.api.article.command.CreateArticle;
import com.masterproject.ddd.core.api.article.command.UpdateArticle;
import com.masterproject.ddd.core.api.article.query.GetArticleByID;
import com.masterproject.ddd.core.api.article.query.GetArticlesByStatus;
import com.masterproject.ddd.core.api.article.utils.ArticleModelMapper;
import com.masterproject.ddd.core.api.core.TimeUtils;
import com.masterproject.ddd.grpc.api.article.ArticleGrpc;
import com.masterproject.ddd.grpc.api.article.ArticleResponse;
import com.masterproject.ddd.grpc.api.article.CreateArticleRequest;
import com.masterproject.ddd.grpc.api.article.GetAllArticlesResponse;
import com.masterproject.ddd.grpc.api.article.GetArticlesByStatusRequest;
import com.masterproject.ddd.grpc.api.article.UpdateArticleRequest;
import com.masterproject.ddd.grpc.api.commons.StringMessageParam;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@GRpcService
public class ArticleControllerGRPC extends ArticleGrpc.ArticleImplBase {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Override
    public void createArticle(CreateArticleRequest request, StreamObserver<StringMessageParam> responseObserver) {
        UUID newId = UUID.randomUUID();
        commandGateway
                .send(CreateArticle.builder().Id(newId.toString()).authorID(request.getAuthorID())
                        .articleDetails(
                                ArticleModelMapper.INSTANCE.convertToCoreAPIModelType(request.getArticleDetails()))
                        .build())
                .thenAcceptAsync((e) -> {
                    responseObserver.onNext(StringMessageParam.newBuilder().setValue(newId.toString()).build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void updateArticle(UpdateArticleRequest request, StreamObserver<StringMessageParam> responseObserver) {
        commandGateway
                .send(UpdateArticle.builder().Id(request.getArticleID())
                        .articleDetails(
                                ArticleModelMapper.INSTANCE.convertToCoreAPIModelType(request.getArticleDetails())))
                .thenAcceptAsync((e) -> {
                    responseObserver
                            .onNext(StringMessageParam.newBuilder().setValue("Article is been process for update")
                                    .build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void closeArticle(StringMessageParam request, StreamObserver<StringMessageParam> responseObserver) {
        commandGateway
                .send(CloseArticle.builder().Id(request.getValue()).build())
                .thenAcceptAsync((e) -> {
                    responseObserver
                            .onNext(StringMessageParam.newBuilder().setValue("Article is been process for close")
                                    .build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getArticleById(StringMessageParam request, StreamObserver<ArticleResponse> responseObserver) {
        queryGateway.query(GetArticleByID.builder().Id(request.getValue()).build(),
                ResponseTypes.instanceOf(Article.class))
                .thenAcceptAsync((a) -> {
                    responseObserver.onNext(convertToGRPCModelType(a));
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getAllArticleByStatus(GetArticlesByStatusRequest request,
            StreamObserver<GetAllArticlesResponse> responseObserver) {
        queryGateway.query(
                GetArticlesByStatus.builder()
                        .status(ArticleModelMapper.INSTANCE.convertToCoreAPIModelType(request.getStatus()))
                        .build(),
                ResponseTypes.multipleInstancesOf(Article.class))
                .thenAcceptAsync((al) -> {
                    var articles = al.stream().map(up -> convertToGRPCModelType(up))
                            .collect(Collectors.toList());
                    var response = GetAllArticlesResponse.newBuilder().addAllArticles(articles).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                });
    }

    private ArticleResponse convertToGRPCModelType(Article article) {
        ArticleResponse response = ArticleResponse.newBuilder()
                .setId(article.getId())
                .setAuthorID(article.getAuthorID())
                .setTitle(article.getTitle())
                .setDescription(article.getDescription())
                .setBody(article.getBody())
                .setStatus(ArticleModelMapper.INSTANCE.convertToGRPCModelType(article.getStatus()))
                .setCreatedOn(TimeUtils.INSTANCE.convertToGRPCModelType(article.getCreatedOn()))
                .setUpdatedOn(TimeUtils.INSTANCE.convertToGRPCModelType(article.getUpdatedOn()))
                .build();
        return response;
    }

}
