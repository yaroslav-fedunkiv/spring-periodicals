package com.epam.fedunkiv.periodicals.dto.publishers;

import lombok.*;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreatePublisherDto {
    @Pattern(regexp = "^.{1,50}$",
            message = "{publisher.title.length}")
    private String title;
    @Pattern(regexp = "(^FASHION$)?(^NEWS$)?(^SCIENCE$)?(^MUSIC$)?(^ECONOMY$)?(^NATURE$)?(^OTHER$)?",
            message = "topic of publisher can't be empty and must chosen from list of topics (FASHION, SCIENCE, ECONOMY, NEWS, MUSIC, NATURE, OTHER)")
    private String topic;
    @Pattern(regexp = "^\\d{1,3}\\.\\d{2}",
            message = "{publisher.price}")
    private String price;
    @Pattern(regexp = "^.{0,500}$", message = "{publisher.description}")
    private String description;
}
