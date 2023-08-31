package com.masterproject.ddd.article.service.model.dtos;

import javax.validation.Valid;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;

import lombok.Data;

@Data
public class UpdateArticleRequestDTO {

    @Valid
    private ArticleDetails articleDetails;

}
