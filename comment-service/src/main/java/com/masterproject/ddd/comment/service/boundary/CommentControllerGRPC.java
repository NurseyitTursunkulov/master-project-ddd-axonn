package com.masterproject.ddd.comment.service.boundary;

import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.lognet.springboot.grpc.GRpcService;

import com.masterproject.ddd.comment.service.model.Comment;
import com.masterproject.ddd.core.api.comment.command.CreateComment;
import com.masterproject.ddd.core.api.comment.query.GetCommentById;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByArticleId;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByAuthorId;
import com.masterproject.ddd.core.api.comment.utils.CommentModelMapper;
import com.masterproject.ddd.core.api.core.TimeUtils;
import com.masterproject.ddd.grpc.api.comment.CommentGrpc;
import com.masterproject.ddd.grpc.api.comment.CommentResponse;
import com.masterproject.ddd.grpc.api.comment.CreateCommentRequest;
import com.masterproject.ddd.grpc.api.comment.MultipleCommentResonse;
import com.masterproject.ddd.grpc.api.commons.StringMessageParam;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@GRpcService
public class CommentControllerGRPC extends CommentGrpc.CommentImplBase {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Override
    public void createComment(CreateCommentRequest request, StreamObserver<StringMessageParam> responseObserver) {
        UUID newId = UUID.randomUUID();
        commandGateway
                .send(CreateComment.builder().Id(newId.toString())
                        .message(request.getMessage())
                        .articleId(request.getArticleId())
                        .authorId(request.getAuthorID())
                        .build())
                .thenAcceptAsync((e) -> {
                    responseObserver.onNext(StringMessageParam.newBuilder().setValue(newId.toString()).build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getCommentsById(StringMessageParam request, StreamObserver<CommentResponse> responseObserver) {
        queryGateway.query(GetCommentById.builder().id(request.getValue()).build(),
                ResponseTypes.instanceOf(Comment.class))
                .thenAcceptAsync((e) -> {
                    responseObserver.onNext(convertToGRPCModelType(e));
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getCommentsByArticle(StringMessageParam request,
            StreamObserver<MultipleCommentResonse> responseObserver) {
        queryGateway.query(GetCommentsByArticleId.builder().articleId(request.getValue()).build(),
                ResponseTypes.multipleInstancesOf(Comment.class))
                .thenAcceptAsync((c) -> {
                    var comments = c.stream().map(up -> convertToGRPCModelType(up))
                            .collect(Collectors.toList());
                    var response = MultipleCommentResonse.newBuilder().addAllComments(comments).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getCommentsByAuthor(StringMessageParam request,
            StreamObserver<MultipleCommentResonse> responseObserver) {
        queryGateway.query(GetCommentsByAuthorId.builder().authorId(request.getValue()).build(),
                ResponseTypes.multipleInstancesOf(Comment.class)).thenAcceptAsync((c) -> {
                    var comments = c.stream().map(up -> convertToGRPCModelType(up))
                            .collect(Collectors.toList());
                    var response = MultipleCommentResonse.newBuilder().addAllComments(comments).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                });
    }

    private CommentResponse convertToGRPCModelType(Comment comment) {
        CommentResponse data = CommentResponse.newBuilder()
                .setId(comment.getId())
                .setArticleID(comment.getArticleID())
                .setAuthorID(comment.getAuthorID())
                .setMessage(comment.getMessage())
                .setStatus(CommentModelMapper.INSTANCE.convertToGRPCModelType(comment.getStatus()))
                .setCreatedOn(TimeUtils.INSTANCE.convertToGRPCModelType(comment.getCreatedOn()))
                .setUpdatedOn(TimeUtils.INSTANCE.convertToGRPCModelType(comment.getUpdatedOn()))
                .build();
        return data;
    }

}
