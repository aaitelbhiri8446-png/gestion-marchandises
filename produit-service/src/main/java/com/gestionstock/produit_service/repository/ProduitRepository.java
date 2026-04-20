package com.gestionstock.produit_service.repository;

import com.gestionstock.produit_service.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Spring génère automatiquement le SQL pour ces méthodes
    List<Produit> findByNomContainingIgnoreCase(String nom);
    List<Produit> findByQuantiteStockLessThan(int seuil);
    List<Produit> findByCategorieId(Long categorieId);
}