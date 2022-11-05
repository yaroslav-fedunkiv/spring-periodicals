package com.epam.fedunkiv.periodicals.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'CLIENT'")
    private Role role;
    @Column(nullable = false)
    private String email;
    @Column
    private String address;
    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="publisher_id")
    )
    private List<Publisher> subscription;
    @Column(columnDefinition = "double default 0.00")
    private double balance;
    @Column(nullable = false)
    private String password;
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime created;
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime updated;
}
