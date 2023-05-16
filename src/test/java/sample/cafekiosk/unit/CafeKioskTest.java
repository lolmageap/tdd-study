package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

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
    void testAddSameBeverage() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano, 2);

        // then
        Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);

        Assertions.assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);

        Assertions.assertThat(cafeKiosk.getBeverages().get(0))
                .isSameAs(cafeKiosk.getBeverages().get(0));
    }

    @Test
    void testAddZeroBeverage() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        // then
        Assertions.assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문해주세요");
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

    @Test
    void testCalculateTotalPrice() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // then
        Integer totalPrice = cafeKiosk.calculateTotalPrice();
        Assertions.assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    void testOrder() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano);
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2022,5,16,22,0));

        // then
        Assertions.assertThat(order.getBeverages()).hasSize(1);
        Assertions.assertThat(order.getBeverages().get(0)).isSameAs(americano);
    }

    @Test
    void testNotOrderingTime() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano);

        // then
        Assertions.assertThatThrownBy(() ->
                cafeKiosk.createOrder(LocalDateTime.of(2022,5,16,9,59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요");

        Assertions.assertThatThrownBy(() ->
                cafeKiosk.createOrder(LocalDateTime.of(2022,5,16,22,1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요");
    }

}