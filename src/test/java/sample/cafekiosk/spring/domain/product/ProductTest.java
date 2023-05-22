package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    void testContainsStockType(){
        //given
        ProductType bakery = ProductType.BAKERY;
        ProductType bottle = ProductType.BOTTLE;
        ProductType handmade = ProductType.HANDMADE;

        //when
        boolean resultBakery = ProductType.containsStockType(bakery);
        boolean resultBottle = ProductType.containsStockType(bottle);
        boolean resultHandmade = ProductType.containsStockType(handmade);

        //then
        Assertions.assertThat(resultBakery).isTrue();
        Assertions.assertThat(resultBottle).isTrue();
        Assertions.assertThat(resultHandmade).isFalse();
    }


}