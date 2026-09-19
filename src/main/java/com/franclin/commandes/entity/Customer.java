package com.franclin.commandes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// @Entity + @Table : ce qui manquait, et qui causait le crash au démarrage
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motsDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // renommé "commandes" : une liste d'Order appartenant à ce Customer
    @OneToMany(mappedBy = "customer")
    private List<Order> commandes = new ArrayList<>();

    // ---- méthodes exigées par l'interface UserDetails ----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        // retourne le VRAI champ, pas une chaîne vide -- sinon aucune
        // vérification de mot de passe ne peut fonctionner correctement
        return motsDePasse;
    }

    @Override
    public String getUsername() {
        // retourne le VRAI email -- sinon Spring Security ne peut identifier
        // aucun utilisateur de façon unique
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}