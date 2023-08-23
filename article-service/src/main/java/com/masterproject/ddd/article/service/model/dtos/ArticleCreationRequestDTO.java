package com.masterproject.ddd.article.service.model.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;

import lombok.Data;

@Data
public class ArticleCreationRequestDTO {

    @Valid
    private ArticleDetails articleDetails;

    @NotBlank(message = "Author Id can not be empty")
    private String authorID;
    private String authorName;

}
