package com.gestionstock.produit_service.Controller;

import com.gestionstock.produit_service.entity.Produit;
import com.gestionstock.produit_service.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public List<Produit> listerTous() {
        return produitService.listerTous();
    }

    @GetMapping("/{id}")
    public Produit trouver(@PathVariable Long id) {
        return produitService.trouverParId(id);
    }

    @GetMapping("/recherche")
    public List<Produit> rechercher(@RequestParam String nom) {
        return produitService.rechercherParNom(nom);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produit ajouter(@RequestBody @Valid Produit produit) {
        return produitService.ajouter(produit);
    }

    @PutMapping("/{id}")
    public Produit modifier(@PathVariable Long id, @RequestBody @Valid Produit produit) {
        return produitService.modifier(id, produit);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        produitService.supprimer(id);
    }

    @GetMapping("/{id}/prix-reduit")
    public ResponseEntity<String> prixReduit(
            @PathVariable Long id,
            @RequestParam String strategie) {
        double prix = produitService.calculerPrixReduit(id, strategie);
        return ResponseEntity.ok("Prix après réduction: " + prix + " MAD");
    }


}