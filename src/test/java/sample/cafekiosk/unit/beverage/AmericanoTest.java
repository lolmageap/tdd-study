package sample.cafekiosk.unit.beverage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    public void testGetName() {
        Americano americano = new Americano();
        Assertions.assertThat(americano.getName()).isEqualTo("아메리카노");
    }

}