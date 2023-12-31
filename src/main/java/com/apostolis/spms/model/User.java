package com.apostolis.spms.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
                                          @UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username",nullable = false)  
    private String username;
    
    @Column(name="email",nullable = false)  
    private String email;

    @Column(name="password",nullable = false)  
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 

    private Set<Role> roles;
}

