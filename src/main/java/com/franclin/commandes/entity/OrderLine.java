package com.franclin.commandes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite;

    // on fige le prix au moment de la commande : si Product.prix change après,
    // l'historique de cette commande ne doit pas être réécrit rétroactivement
    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    // ce champ "order" est celui que le mappedBy = "order" de Order.java
    // vient chercher -- le nom doit correspondre EXACTEMENT (casse comprise)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // relation vers Product SANS cascade : le cycle de vie d'un OrderLine
    // n'a aucune raison d'affecter le catalogue de produits
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}