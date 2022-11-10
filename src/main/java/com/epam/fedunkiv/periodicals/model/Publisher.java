package com.epam.fedunkiv.periodicals.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@DynamicInsert
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Topics topic;
    @Column(nullable = false)
    private Double price;
    @Column(length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(name = "publisher_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<User> subscribers;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;


}
