package com.gestionstock.stock_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// "PRODUIT-SERVICE" = le nom du service dans Eureka
@FeignClient(name = "PRODUIT-SERVICE")
public interface ProduitClient {

    @GetMapping("/api/produits/{id}")
    ProduitResponse getProduit(@PathVariable Long id);

    @PutMapping("/api/produits/{id}/stock")
    void mettreAJourStock(@PathVariable Long id,
                          @RequestParam int delta);

    // Classe interne pour mapper la réponse JSON
    record ProduitResponse(
            Long id,
            String nom,
            Integer quantiteStock,
            Integer seuilAlerte,
            Double prix
    ) {}
}