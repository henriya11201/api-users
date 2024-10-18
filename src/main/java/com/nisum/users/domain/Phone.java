package com.nisum.users.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "country_code")
    private String countryCode;

    // Join ManyToOne con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
