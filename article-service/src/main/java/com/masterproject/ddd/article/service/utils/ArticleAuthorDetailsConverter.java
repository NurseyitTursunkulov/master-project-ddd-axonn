package com.masterproject.ddd.article.service.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.gson.Gson;
import com.masterproject.ddd.core.api.user.model.ProfileDetails;

@Converter
public class ArticleAuthorDetailsConverter implements AttributeConverter<ProfileDetails, String> {

    private final Gson gsonConverter = new Gson();

    @Override
    public String convertToDatabaseColumn(ProfileDetails attribute) {
        return gsonConverter.toJson(attribute);
    }

    @Override
    public ProfileDetails convertToEntityAttribute(String dbData) {
        return gsonConverter.fromJson(dbData, ProfileDetails.class);
    }

}
