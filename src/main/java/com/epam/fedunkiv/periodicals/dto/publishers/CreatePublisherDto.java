package com.epam.fedunkiv.periodicals.dto.publishers;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreatePublisherDto {
    @Pattern(regexp = "^.{1,50}$", message = "size of publisher's title must be between 1 and 50 symbols")
    private String title;
    @Pattern(regexp = "(^FASHION$)?(^NEWS$)?(^SCIENCE$)?(^MUSIC$)?(^ECONOMY$)?(^NATURE$)?(^OTHER$)?",
            message = "topic of publisher can't be empty and must chosen from list of topics (FASHION, SCIENCE, ECONOMY, NEWS, MUSIC, NATURE, OTHER)")
    private String topic;
    @NotBlank(message = "price of publisher can't be empty")
    @Pattern(regexp = "^\\d{1,3}\\.\\d{2}", message = "the price must follow the pattern: X.XX, XX.XX, XXX.XX")
    private String price;
    @Pattern(regexp = "^.{0,500}$", message = "description size can't be more than 500 symbols")
    private String description;
}
