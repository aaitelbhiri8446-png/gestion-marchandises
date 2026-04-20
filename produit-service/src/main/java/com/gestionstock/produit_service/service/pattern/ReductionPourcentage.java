package com.gestionstock.produit_service.service.pattern;

import org.springframework.stereotype.Component;

@Component("pourcentage")
public class ReductionPourcentage implements ReductionStrategy {

    @Override
    public double calculer(double prixOriginal) {
        return prixOriginal * 0.10;  // 10% de réduction
    }

    @Override
    public String getNom() { return "Réduction 10%"; }
}