package com.gestionstock.produit_service.service.pattern;

import org.springframework.stereotype.Component;

@Component("fixe")
public class ReductionFixe implements ReductionStrategy {

    @Override
    public double calculer(double prixOriginal) {
        return 20.0;  // 20 MAD de réduction fixe
    }

    @Override
    public String getNom() { return "Réduction fixe 20 MAD"; }
}