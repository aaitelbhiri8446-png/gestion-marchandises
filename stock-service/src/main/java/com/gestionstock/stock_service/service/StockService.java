package com.gestionstock.stock_service.service;

import com.gestionstock.stock_service.client.ProduitClient;
import com.gestionstock.stock_service.client.ProduitClient.ProduitResponse;
import com.gestionstock.stock_service.entity.MouvementStock;
import com.gestionstock.stock_service.entity.MouvementStock.TypeMouvement;
import com.gestionstock.stock_service.repository.MouvementStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j  // permet d'utiliser log.info(), log.warn()
public class StockService {

    private final MouvementStockRepository mouvementRepository;
    private final ProduitClient produitClient;  // appelle produit-service

    public MouvementStock entreeStock(Long produitId, int quantite, String motif) {
        // 1. Récupère les infos du produit via Feign
        ProduitResponse produit = produitClient.getProduit(produitId);

        int stockAvant = produit.quantiteStock();
        int stockApres = stockAvant + quantite;

        // 2. Enregistre le mouvement
        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduitId(produitId);
        mouvement.setNomProduit(produit.nom());
        mouvement.setType(TypeMouvement.ENTREE);
        mouvement.setQuantite(quantite);
        mouvement.setStockAvant(stockAvant);
        mouvement.setStockApres(stockApres);
        mouvement.setDate(LocalDateTime.now());
        mouvement.setMotif(motif);

        // 3. Met à jour le stock dans produit-service
        produitClient.mettreAJourStock(produitId, quantite);

        log.info("Entrée stock: +{} pour {} (total: {})", quantite, produit.nom(), stockApres);
        return mouvementRepository.save(mouvement);
    }

    public MouvementStock sortieStock(Long produitId, int quantite, String motif) {
        ProduitResponse produit = produitClient.getProduit(produitId);

        int stockAvant = produit.quantiteStock();

        if (stockAvant < quantite) {
            throw new RuntimeException("Stock insuffisant. Disponible: " + stockAvant
                    + ", Demandé: " + quantite);
        }

        int stockApres = stockAvant - quantite;

        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduitId(produitId);
        mouvement.setNomProduit(produit.nom());
        mouvement.setType(TypeMouvement.SORTIE);
        mouvement.setQuantite(quantite);
        mouvement.setStockAvant(stockAvant);
        mouvement.setStockApres(stockApres);
        mouvement.setDate(LocalDateTime.now());
        mouvement.setMotif(motif);

        produitClient.mettreAJourStock(produitId, -quantite);

        // Observer Pattern : si stock faible → log d'alerte
        if (stockApres < produit.seuilAlerte()) {
            log.warn("ALERTE STOCK FAIBLE: {} — Stock actuel: {}, Seuil: {}",
                    produit.nom(), stockApres, produit.seuilAlerte());
        }

        return mouvementRepository.save(mouvement);
    }

    public List<MouvementStock> historiqueProduit(Long produitId) {
        return mouvementRepository.findByProduitIdOrderByDateDesc(produitId);
    }
}