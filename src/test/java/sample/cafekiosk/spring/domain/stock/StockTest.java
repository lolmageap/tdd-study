package sample.cafekiosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    @DisplayName("재고의 수량이 제공된 수량보다 작은지 확인한다.")
    void testIsQuantityLessThen(){
        //given
        int quantity = 3;

        Stock stock = Stock.create("001", 2);

        //when
        boolean stockIsTrue = stock.isQuantityLessThen(quantity);

        //then
        Assertions.assertThat(stockIsTrue).isTrue();
    }

    @Test
    @DisplayName("재고의 수량이 제공된 수량보다 크거나 같은지 확인한다.")
    void testIsQuantityGreaterThen(){
        //given
        int quantity = 2;

        Stock stock = Stock.create("001", 2);

        //when
        boolean stockIsFalse = stock.isQuantityLessThen(quantity);

        //then
        Assertions.assertThat(stockIsFalse).isFalse();
    }

    @Test
    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    void testDeductQuantity(){
        //given
        int quantity = 2;
        Stock stock = Stock.create("001", 2);

        //when
        stock.deductQuantity(quantity);

        //then
        Assertions.assertThat(stock.getQuantity()).isZero();
    }

    @Test
    @DisplayName("재고보다 많은수의 수량으로 차감 시도하는 경우 예외가 발생한다.")
    void testDeductQuantityThrow() {
        //given
        int quantity = 5;
        Stock stock = Stock.create("001", 2);

        //when //then
        Assertions.assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고 수량이 없습니다.");
    }

}