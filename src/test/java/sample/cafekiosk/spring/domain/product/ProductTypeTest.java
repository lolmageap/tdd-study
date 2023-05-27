package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ProductTypeTest {

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

    @ParameterizedTest(name = "{index} ==> the rank of ''{0}'' is {1}")
    @CsvSource({"HANDMADE,false", "BOTTLE,true", "BAKERY,true"})
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    void parameterizedTestContainsStockType(ProductType productType, Boolean expected){
        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        Assertions.assertThat(expected).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("provideProductTypesForCheckingStockType")
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    void parameterizedTestContainsStockTypeArgIsStream(ProductType productType, Boolean expected){
        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        Assertions.assertThat(expected).isEqualTo(result);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType(){
        return Stream.of(
                Arguments.arguments(ProductType.BAKERY, false),
                Arguments.arguments(ProductType.BOTTLE, true),
                Arguments.arguments(ProductType.HANDMADE, true)
        );
    }

}