package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

class CafeKioskTest {

    @Test
    void testAddAmericano() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano);

        // then
        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);
        Assertions.assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void testAddBeverage() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // then
        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);
        Assertions.assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("라떼");
    }

    @Test
    void testRemove() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.remove(americano);
        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);

        // then
        Assertions.assertThat(cafeKiosk.getBeverages().get(0)
                .getName()).isEqualTo("라떼");
    }

    @Test
    void testClear() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();

        // then
        Assertions.assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

}