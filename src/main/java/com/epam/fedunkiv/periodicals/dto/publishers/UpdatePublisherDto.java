package com.epam.fedunkiv.periodicals.dto.publishers;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UpdatePublisherDto {
    private String newTitle;
    private String topic;
    private String price;
    private String description;
}
