package com.gestionstock.stock_service.controller;

import com.gestionstock.stock_service.entity.MouvementStock;
import com.gestionstock.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/entree")
    public MouvementStock entree(
            @RequestParam Long produitId,
            @RequestParam int quantite,
            @RequestParam(defaultValue = "réception") String motif) {
        return stockService.entreeStock(produitId, quantite, motif);
    }

    @PostMapping("/sortie")
    public MouvementStock sortie(
            @RequestParam Long produitId,
            @RequestParam int quantite,
            @RequestParam(defaultValue = "vente") String motif) {
        return stockService.sortieStock(produitId, quantite, motif);
    }

    @GetMapping("/historique/{produitId}")
    public List<MouvementStock> historique(@PathVariable Long produitId) {
        return stockService.historiqueProduit(produitId);
    }
}