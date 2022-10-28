package com.epam.fedunkiv.periodicals.dto.publishers;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UpdatePublisherDto {
    @NotBlank
    private String title;
    @NotBlank
    private String topic;
    @NotBlank
    private String price;
    private String description;
    @NotBlank
    private String oldTitle;
}
