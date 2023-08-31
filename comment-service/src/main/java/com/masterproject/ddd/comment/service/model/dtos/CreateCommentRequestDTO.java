package com.masterproject.ddd.comment.service.model.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequestDTO {

    @NotBlank(message = "Comment Message can not be empty")
    private String message;
    @NotBlank(message = "Article Id can not be empty")
    private String articleId;
    @NotBlank(message = "Author Id can not be empty")
    private String authorID;

}
