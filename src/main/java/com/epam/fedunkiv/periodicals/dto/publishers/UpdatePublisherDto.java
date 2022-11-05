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

    private String topic;

    private String price;
    private String description;
    private String oldTitle;
}
