package com.gestionstock.stock_service.repository;

import com.gestionstock.stock_service.entity.MouvementStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    List<MouvementStock> findByProduitIdOrderByDateDesc(Long produitId);
}