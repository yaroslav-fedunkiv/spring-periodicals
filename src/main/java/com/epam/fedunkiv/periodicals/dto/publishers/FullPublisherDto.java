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
    private String topic;
    private String price;
    private String description;
    private String isActive;
    private String created;
    private String updated;
}
