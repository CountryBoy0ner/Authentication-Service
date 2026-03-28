package com.innowise.Authentication_Service.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;


@Entity
@Table(name = "roles")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;



    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
