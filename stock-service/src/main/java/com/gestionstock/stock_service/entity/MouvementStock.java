package com.gestionstock.stock_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long produitId;       // référence vers produit-service
    private String nomProduit;    // copie locale du nom

    @Enumerated(EnumType.STRING)
    private TypeMouvement type;   // ENTREE ou SORTIE

    private Integer quantite;
    private Integer stockAvant;
    private Integer stockApres;

    private LocalDateTime date;

    private String motif;         // ex: "vente", "réception commande"

    public enum TypeMouvement {
        ENTREE, SORTIE
    }
}