package com.epam.fedunkiv.periodicals.dto.subscriptions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SubscribeDto {
    private String subscriptionPeriod;
    private String userId;
    private String publisherId;
    private String address;
}
