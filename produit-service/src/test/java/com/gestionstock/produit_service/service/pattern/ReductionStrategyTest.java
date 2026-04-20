package com.gestionstock.produit_service.service.pattern;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReductionStrategyTest {

    @Test
    void reductionFixe_devraitToujoursDonner20() {
        ReductionFixe fixe = new ReductionFixe();
        assertThat(fixe.calculer(100.0)).isEqualTo(20.0);
        assertThat(fixe.calculer(500.0)).isEqualTo(20.0); // toujours 20 MAD
    }

    @Test
    void reductionPourcentage_devraitCalculer10Pourcent() {
        ReductionPourcentage pct = new ReductionPourcentage();
        assertThat(pct.calculer(200.0)).isEqualTo(20.0);
        assertThat(pct.calculer(100.0)).isEqualTo(10.0);
    }
}