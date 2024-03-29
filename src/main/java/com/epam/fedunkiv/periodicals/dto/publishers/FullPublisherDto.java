package com.epam.fedunkiv.periodicals.dto.publishers;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FullPublisherDto {
    private String id;
    private String title;
    private String issue;
    private String topic;
    private String price;
    private String image;
    private String description;
    private String isActive;
    private String created;
    private String updated;
}
