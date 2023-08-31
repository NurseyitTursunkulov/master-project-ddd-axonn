package com.masterproject.ddd.core.api.user.utils;

import com.masterproject.ddd.core.api.user.model.ProfileDetails;
import com.masterproject.ddd.core.api.user.model.UserStatus;
import com.masterproject.ddd.grpc.api.userprofile.CreateUserProfileRequest;
import com.masterproject.ddd.grpc.api.userprofile.UPStatus;

public enum UserModelMapper {
    INSTANCE;

    public UPStatus convertToGRPCModelType(UserStatus status) {
        switch (status) {
            case VERIFIED:
                return UPStatus.UP_VERIFIED;
            case NEW:
                return UPStatus.UP_NEW;
            case REJECTED:
                return UPStatus.UP_REJECTED;
            case ACTIVATED:
                return UPStatus.UP_ACTIVATED;
            case DEACTIVATED:
                return UPStatus.UP_DEACTIVATED;
            default:
                return UPStatus.UP_NEW;

        }
    }

    public UserStatus convertToCoreAPIModelType(UPStatus status) {
        switch (status) {
            case UP_VERIFIED:
                return UserStatus.VERIFIED;
            case UP_NEW:
                return UserStatus.NEW;
            case UP_REJECTED:
                return UserStatus.REJECTED;
            case UP_ACTIVATED:
                return UserStatus.ACTIVATED;
            case UP_DEACTIVATED:
                return UserStatus.DEACTIVATED;
            default:
                return UserStatus.NEW;

        }
    }

    public ProfileDetails convertToCoreAPIModelType(CreateUserProfileRequest request) {
        ProfileDetails details = new ProfileDetails();
        details.setUserName(request.getUserName());
        details.setEmail(request.getEmail());
        details.setName(request.getName());
        return details;
    }
}
