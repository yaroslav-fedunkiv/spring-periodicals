package com.epam.fedunkiv.periodicals.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@DynamicInsert
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "publisher_id")
    private Long publisherId;
    @Column
    private Integer subscriptionPeriod;
    @Column
    private String address;
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;
}
