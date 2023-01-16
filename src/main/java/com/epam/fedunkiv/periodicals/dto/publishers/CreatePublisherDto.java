package com.epam.fedunkiv.periodicals.dto.publishers;

import com.epam.fedunkiv.periodicals.validation.ExistedTitle;
import com.epam.fedunkiv.periodicals.validation.TopicValid;
import lombok.*;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreatePublisherDto {
    @Pattern(regexp = "^.{1,50}$", message = "{publisher.title.length}")
    @ExistedTitle
    private String title;

    @TopicValid(message = "{publisher.wrong.topic}")
    private String topic;

    private String price;

    private String image;

    @Pattern(regexp = "^.{0,500}$", message = "{publisher.description}")
    private String description;
}
