package com.gestionstock.fournisseur_service.repository;
import com.gestionstock.fournisseur_service.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    List<Fournisseur> findByNomContainingIgnoreCase(String nom);
}