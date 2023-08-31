package com.masterproject.ddd.user.service.boundary;

import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.lognet.springboot.grpc.GRpcService;

import com.masterproject.ddd.core.api.core.TimeUtils;
import com.masterproject.ddd.core.api.user.command.CreateUserProfile;
import com.masterproject.ddd.core.api.user.command.VerifyUserProfile;
import com.masterproject.ddd.core.api.user.model.ProfileDetails;
import com.masterproject.ddd.core.api.user.query.GetAllUserProfiles;
import com.masterproject.ddd.core.api.user.query.GetUserProfileById;
import com.masterproject.ddd.core.api.user.query.GetUserProfilesByStatus;
import com.masterproject.ddd.core.api.user.utils.UserModelMapper;
import com.masterproject.ddd.grpc.api.commons.EmptyMessageParam;
import com.masterproject.ddd.grpc.api.commons.StringMessageParam;
import com.masterproject.ddd.grpc.api.userprofile.CreateUserProfileRequest;
import com.masterproject.ddd.grpc.api.userprofile.GetAllUserProfilesResponse;
import com.masterproject.ddd.grpc.api.userprofile.GetUserProfileResponse;
import com.masterproject.ddd.grpc.api.userprofile.GetUserProfilesByStatusRequest;
import com.masterproject.ddd.grpc.api.userprofile.UserGrpc;
import com.masterproject.ddd.user.service.model.UserProfile;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@GRpcService
public class UserProfileGRPC extends UserGrpc.UserImplBase {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Override
    public void createUserProfile(CreateUserProfileRequest request,
            StreamObserver<StringMessageParam> responseObserver) {
        UUID newId = UUID.randomUUID();
        ProfileDetails details = UserModelMapper.INSTANCE.convertToCoreAPIModelType(request);
        commandGateway
                .send(CreateUserProfile.builder().Id(newId.toString()).userProfileDetails(details).build())
                .thenAcceptAsync((e) -> {
                    responseObserver.onNext(StringMessageParam.newBuilder().setValue(newId.toString()).build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void verifyProfile(StringMessageParam request, StreamObserver<StringMessageParam> responseObserver) {
        commandGateway
                .send(VerifyUserProfile.builder().Id(request.getValue()).build())
                .thenAcceptAsync((e) -> {
                    responseObserver.onNext(
                            StringMessageParam.newBuilder().setValue("User Verification Process stated.").build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getUserProfileById(StringMessageParam request,
            StreamObserver<GetUserProfileResponse> responseObserver) {
        queryGateway.query(GetUserProfileById.builder().id(request.getValue()).build(),
                ResponseTypes.instanceOf(UserProfile.class))
                .thenAcceptAsync((p) -> {
                    var profile = convertToGRPCModelType(p);
                    responseObserver.onNext(profile);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getUserProfiles(EmptyMessageParam request,
            StreamObserver<GetAllUserProfilesResponse> responseObserver) {
        queryGateway.query(GetAllUserProfiles.builder().build(),
                ResponseTypes.multipleInstancesOf(UserProfile.class))
                .thenAcceptAsync((p) -> {
                    var userprofiles = p.stream().map(up -> convertToGRPCModelType(up))
                            .collect(Collectors.toList());
                    var response = GetAllUserProfilesResponse.newBuilder().addAllUserProfiles(userprofiles).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getUserProfilesByStatus(GetUserProfilesByStatusRequest request,
            StreamObserver<GetAllUserProfilesResponse> responseObserver) {
        queryGateway
                .query(GetUserProfilesByStatus.builder()
                        .status(UserModelMapper.INSTANCE.convertToCoreAPIModelType(request.getStatus())).build(),
                        ResponseTypes.multipleInstancesOf(com.masterproject.ddd.user.service.model.UserProfile.class))
                .thenAcceptAsync((p) -> {
                    var userprofiles = p.stream().map(up -> convertToGRPCModelType(up))
                            .collect(Collectors.toList());
                    var response = GetAllUserProfilesResponse.newBuilder().addAllUserProfiles(userprofiles).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                });
    }

    private GetUserProfileResponse convertToGRPCModelType(UserProfile p) {
        GetUserProfileResponse profile = GetUserProfileResponse.newBuilder()
                .setId(p.getId())
                .setEmail(p.getEmail())
                .setUsername(p.getUsername())
                .setName(p.getName())
                .setStatus(UserModelMapper.INSTANCE.convertToGRPCModelType(p.getStatus()))
                .setRegisteredOn(TimeUtils.INSTANCE.convertToGRPCModelType(p.getRegisteredOn()))
                .build();
        return profile;
    }

}
