package com.epam.fedunkiv.periodicals.dto.publishers;

import com.epam.fedunkiv.periodicals.validation.TopicValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreatePublisherDto {
//    @ApiModelProperty(notes = "Publisher title", example = "The Economist", required = true)
    @Pattern(regexp = "^.{1,50}$", message = "{publisher.title.length}")
    private String title;

//    @ApiModelProperty(notes = "Publisher topic", example = "ECONOMY", required = true)
    @TopicValid(message = "{publisher.wrong.topic}")
    private String topic;

//    @ApiModelProperty(notes = "Publisher price", example = "95.25", required = true)
    @Pattern(regexp = "^\\d{1,3}\\.\\d{2}", message = "{publisher.price}")
    private String price;

//    @ApiModelProperty(notes = "Publisher description", example = "Some description about 'The Economist'")
    @Pattern(regexp = "^.{0,500}$", message = "{publisher.description}")
    private String description;
}
