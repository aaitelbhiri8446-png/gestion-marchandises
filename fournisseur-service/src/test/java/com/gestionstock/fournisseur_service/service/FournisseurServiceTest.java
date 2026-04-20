package com.gestionstock.fournisseur_service.service;

import com.gestionstock.fournisseur_service.entity.Fournisseur;
import com.gestionstock.fournisseur_service.repository.FournisseurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @InjectMocks
    private FournisseurService fournisseurService;

    @Test
    void ajouter_devraitRetournerFournisseurSauvegarde() {
        Fournisseur f = new Fournisseur();
        f.setNom("Maroc Import");

        when(fournisseurRepository.save(f)).thenReturn(f);

        Fournisseur result = fournisseurService.ajouter(f);

        assertThat(result.getNom()).isEqualTo("Maroc Import");
    }

    @Test
    void trouverParId_devraitLancerExceptionSiInexistant() {
        when(fournisseurRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fournisseurService.trouverParId(5L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void modifier_devraitMettreAJourLeNom() {
        Fournisseur existant = new Fournisseur();
        existant.setId(1L);
        existant.setNom("Ancien Nom");

        Fournisseur modif = new Fournisseur();
        modif.setNom("Nouveau Nom");

        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(existant));
        when(fournisseurRepository.save(existant)).thenReturn(existant);

        Fournisseur result = fournisseurService.modifier(1L, modif);

        assertThat(result.getNom()).isEqualTo("Nouveau Nom");
    }
}