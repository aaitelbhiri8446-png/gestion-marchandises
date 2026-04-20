package com.gestionstock.produit_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @Positive(message = "Le prix doit être positif")
    private Double prix;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantiteStock;

    @Min(value = 1, message = "Le seuil doit être au moins 1")
    private Integer seuilAlerte;   // si quantiteStock < seuilAlerte → alerte

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Category categorie;
}