package com.epam.fedunkiv.periodicals.dto.publishers;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FullPublisherDto {
    @ApiModelProperty(notes = "Publisher id", example = "1")
    private String id;
    @ApiModelProperty(notes = "Publisher title", example = "The Economist")
    private String title;
    @ApiModelProperty(notes = "Publisher topic", example = "ECONOMY")
    private String topic;
    @ApiModelProperty(notes = "Publisher price", example = "98.99")
    private String price;
    @ApiModelProperty(notes = "Publisher description", example = "Some description about 'The Economist'")
    private String description;
    @ApiModelProperty(notes = "Publisher status", example = "true")
    private String isActive;
    @ApiModelProperty(notes = "Publisher created date", example = "2022-10-31T11:46:11")
    private String created;
    @ApiModelProperty(notes = "Publisher updated date", example = "2022-10-31T11:46:11")
    private String updated;
}
