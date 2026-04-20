package com.gestionstock.fournisseur_service.service;

import com.gestionstock.fournisseur_service.entity.Fournisseur;
import com.gestionstock.fournisseur_service.repository.FournisseurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    
    public Fournisseur ajouter(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public List<Fournisseur> listerTous() {
        return fournisseurRepository.findAll();
    }

    public Fournisseur trouverParId(Long id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fournisseur non trouvé avec id: " + id));
    }

    public Fournisseur modifier(Long id, Fournisseur fournisseur) {
        Fournisseur existant = trouverParId(id);
        existant.setNom(fournisseur.getNom());
        return fournisseurRepository.save(existant);
    }

    public void supprimer(Long id) {
        fournisseurRepository.deleteById(id);
    }

    public List<Fournisseur> rechercherParNom(String nom) {
        return fournisseurRepository.findByNomContainingIgnoreCase(nom);
    }
}