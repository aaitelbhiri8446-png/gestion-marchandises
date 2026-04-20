package com.gestionstock.produit_service.service;

import com.gestionstock.produit_service.entity.Produit;
import com.gestionstock.produit_service.service.pattern.ReductionStrategy;
import com.gestionstock.produit_service.repository.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;

    // Spring injecte automatiquement toutes les implémentations de ReductionStrategy
    // dans cette Map : clé = nom du @Component, valeur = l'objet
    private final Map<String, ReductionStrategy> strategies;

    public Produit ajouter(Produit produit) {
        return produitRepository.save(produit);
    }

    public List<Produit> listerTous() {
        return produitRepository.findAll();
    }

    public Produit trouverParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé avec id: " + id));
    }

    public Produit modifier(Long id, Produit produit) {
        Produit existant = trouverParId(id);
        existant.setNom(produit.getNom());
        existant.setDescription(produit.getDescription());
        existant.setPrix(produit.getPrix());
        existant.setSeuilAlerte(produit.getSeuilAlerte());
        existant.setCategorie(produit.getCategorie());
        return produitRepository.save(existant);
    }

    public void supprimer(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> rechercherParNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }

    // Applique une stratégie de réduction
    // strategyName = "pourcentage" ou "fixe"
    public double calculerPrixReduit(Long produitId, String strategyName) {
        Produit produit = trouverParId(produitId);
        ReductionStrategy strategy = strategies.get(strategyName);

        if (strategy == null) {
            throw new IllegalArgumentException("Stratégie inconnue: " + strategyName);
        }

        double reduction = strategy.calculer(produit.getPrix());
        return produit.getPrix() - reduction;
    }
}