package com.gestionstock.produit_service.service.pattern;

public interface ReductionStrategy {
    double calculer(double prixOriginal);
    String getNom();
}