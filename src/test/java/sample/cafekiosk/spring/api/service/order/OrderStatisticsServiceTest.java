package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static sample.cafekiosk.spring.domain.product.SellingStatus.SELLING;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;

    /**
     * Test 통합 환경을 위해 MailSendClient를 상위 객체로 올리고 공용으로 사용한다.
     * 상위 테스트 객체에서 상속을 받았어도 테스트 환경이 달라지면 새로 서버를 띄우기 떄문에 테스트 시간이 늘어난다.
     */

//    @MockBean
//    MailSendClient mailSendClient;

    @AfterEach
    void tearDown(){
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void testSendSalesStatisticsEmail(){
        //given
        LocalDateTime registerDate = LocalDateTime.of(2023, 5, 23, 0, 0);

        Product product1 = createProduct("001", HANDMADE, 1000);
        Product product2 = createProduct("002", HANDMADE, 2000);
        Product product3 = createProduct("003", HANDMADE, 3000);

        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 5, 22, 23, 59, 59));
        Order order2 = createPaymentCompletedOrder(products, registerDate);
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 5, 23, 23, 59, 59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 5, 24, 0, 0));

        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 5, 23), "test@test.com");
        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();

        //then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime registerDate) {
        Order order = Order.builder()
                .products(products)
                .registeredDateTime(registerDate)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(String productNumber, ProductType type, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name("아메리카노")
                .type(type)
                .sellingStatus(SELLING)
                .price(price)
                .build();
    }

}