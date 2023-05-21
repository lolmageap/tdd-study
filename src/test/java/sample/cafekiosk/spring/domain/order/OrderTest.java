package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static sample.cafekiosk.spring.domain.product.SellingStatus.SELLING;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    void testCalculateTotalPrice(){
        //given
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, now);

        //then
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @Test
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 상태를 확인한다.")
    void testOrderStatus(){
        //given
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, now);

        //then
        Assertions.assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @Test
    @DisplayName("주문 생성 주문 등록 시간을 기록한다.")
    void testRegisteredDateTime(){
        //given
        LocalDateTime now = LocalDateTime.now();

        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, now);

        //then
        Assertions.assertThat(order.getRegisteredDateTime()).isEqualTo(now);
    }


    private static Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(price)
                .build();
    }

}