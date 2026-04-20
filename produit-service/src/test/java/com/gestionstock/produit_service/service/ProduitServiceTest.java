package com.gestionstock.produit_service.service;

import com.gestionstock.produit_service.entity.Produit;
import com.gestionstock.produit_service.repository.ProduitRepository;
import com.gestionstock.produit_service.service.pattern.ReductionStrategy;
import com.gestionstock.produit_service.service.pattern.ReductionFixe;
import com.gestionstock.produit_service.service.pattern.ReductionPourcentage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    // On crée le service manuellement avec les stratégies
    private ProduitService produitService;

    @BeforeEach
    void setUp() {
        Map<String, ReductionStrategy> strategies = Map.of(
                "fixe", new ReductionFixe(),
                "pourcentage", new ReductionPourcentage()
        );
        produitService = new ProduitService(produitRepository, strategies);
    }

    @Test
    void ajouter_devraitSauvegarderEtRetournerProduit() {
        Produit produit = new Produit();
        produit.setNom("Café");
        produit.setPrix(50.0);

        when(produitRepository.save(produit)).thenReturn(produit);

        Produit result = produitService.ajouter(produit);

        assertThat(result.getNom()).isEqualTo("Café");
        verify(produitRepository).save(produit);
    }

    @Test
    void trouverParId_devraitLancerExceptionSiInexistant() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.trouverParId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void calculerPrixReduit_avecStrategieFixe_devraitSoustraire20() {
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setPrix(100.0);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        double prixReduit = produitService.calculerPrixReduit(1L, "fixe");

        assertThat(prixReduit).isEqualTo(80.0);
    }

    @Test
    void calculerPrixReduit_avecStrategiePourcentage_devraitSoustraire10Pourcent() {
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setPrix(200.0);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        double prixReduit = produitService.calculerPrixReduit(1L, "pourcentage");

        assertThat(prixReduit).isEqualTo(180.0);
    }

    @Test
    void calculerPrixReduit_avecStrategieInconnue_devraitLancerException() {
        Produit produit = new Produit();
        produit.setId(1L);
        produit.setPrix(100.0);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        assertThatThrownBy(() -> produitService.calculerPrixReduit(1L, "inexistante"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inexistante");
    }

    @Test
    void supprimer_devraitAppelerDeleteById() {
        produitService.supprimer(1L);
        verify(produitRepository).deleteById(1L);
    }
}