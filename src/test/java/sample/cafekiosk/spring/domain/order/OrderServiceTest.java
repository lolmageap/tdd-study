package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductType.*;
import static sample.cafekiosk.spring.domain.product.SellingStatus.SELLING;

class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    OrderService orderService;

    @AfterEach
    void tearDown(){
        /**
         * deleteAllInBatch == 하나의 테이블을 깔끔하게 한번에 다 날릴 수 있다.
         * 하지만 Foreign Key 제약을 받기 때문에 삭제시 순서에 영향을 받는다
         *
         * deleteAll == 객체를 한번에 delete 하는 것이 아닌
         * FindAll 해와서 객체를 하나씩 건단위로 삭제를 해서 성능에 영향이 있다. -> foreach(delete<?>)
         * 하지만 Foreign Key 제약된 객체를 같이 delete해서 deleteAllInBatch에 비해 순서에 영향을 비교적 적게 받는다.
         * 그리고 하나의 객체를 지우기만 해도 CASCADE Type이 ALL, REMOVE 상태인 객체들을 모두 지워준다.
         */

        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    void testOrderByProductNumberList(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        //then
        Assertions.assertThat(orderResponse).isNotNull();

        Assertions.assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(
                        registeredDateTime, 3000
                );

        Assertions.assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 2000)
                );
    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 없다.")
    void testCreateOrderWithDuplicateProductNumber(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        //then
        Assertions.assertThat(orderResponse).isNotNull();

        Assertions.assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(
                        registeredDateTime, 2000
                );

        Assertions.assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    void testCreateOrderWithStock(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        //then
        Assertions.assertThat(orderResponse).isNotNull();

        Assertions.assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(
                        registeredDateTime, 9000
                );

        Assertions.assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 2000),
                        tuple("003", 5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        Assertions.assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @Test
    @DisplayName("재고가 없는 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    void testCreateOrderWithNoStock(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stock1.deductQuantity(1); //todo
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when //then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    private static Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(price)
                .build();
    }

}