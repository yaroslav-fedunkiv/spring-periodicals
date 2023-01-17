package com.epam.fedunkiv.periodicals.dto.publishers;

import com.epam.fedunkiv.periodicals.validation.ExistedTitle;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UpdatePublisherDto {
    private String title;
    private String issue;
    private String topic;
    private String price;
    private String description;
    private String image;
}
