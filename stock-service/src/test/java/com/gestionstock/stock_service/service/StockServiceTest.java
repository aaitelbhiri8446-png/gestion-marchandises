package com.gestionstock.stock_service.service;

import com.gestionstock.stock_service.client.ProduitClient;
import com.gestionstock.stock_service.client.ProduitClient.ProduitResponse;
import com.gestionstock.stock_service.entity.MouvementStock;
import com.gestionstock.stock_service.repository.MouvementStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private MouvementStockRepository mouvementRepository;

    @Mock
    private ProduitClient produitClient;

    @InjectMocks
    private StockService stockService;

    @Test
    void entreeStock_devraitCreerMouvementEntree() {
        ProduitResponse produit = new ProduitResponse(1L, "Sucre", 100, 10, 50.0);
        when(produitClient.getProduit(1L)).thenReturn(produit);
        when(mouvementRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MouvementStock result = stockService.entreeStock(1L, 20, "réception");

        assertThat(result.getStockApres()).isEqualTo(120);
        assertThat(result.getType()).isEqualTo(MouvementStock.TypeMouvement.ENTREE);
        verify(produitClient).mettreAJourStock(1L, 20);
    }

    @Test
    void sortieStock_devraitLancerExceptionSiStockInsuffisant() {
        ProduitResponse produit = new ProduitResponse(1L, "Sucre", 5, 2, 50.0);
        when(produitClient.getProduit(1L)).thenReturn(produit);

        assertThatThrownBy(() -> stockService.sortieStock(1L, 10, "vente"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("insuffisant");
    }

    @Test
    void sortieStock_devraitCalculerStockApresCorrectement() {
        ProduitResponse produit = new ProduitResponse(1L, "Farine", 50, 5, 30.0);
        when(produitClient.getProduit(1L)).thenReturn(produit);
        when(mouvementRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MouvementStock result = stockService.sortieStock(1L, 15, "vente");

        assertThat(result.getStockApres()).isEqualTo(35);
        assertThat(result.getType()).isEqualTo(MouvementStock.TypeMouvement.SORTIE);
    }
}