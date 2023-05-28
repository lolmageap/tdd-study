package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;
import java.util.Optional;

import static sample.cafekiosk.spring.domain.product.SellingStatus.*;

class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown(){
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void testFindAllBySellingStatusIn(){
        //given
        Product product = createProduct("아메리카노", "001", SELLING, 4000);
        Product product2 = createProduct("카페라떼", "002", HOLD, 4500);
        Product product3 = createProduct("팥빙수", "003", STOP_SELLING, 7000);

        productRepository.saveAll(List.of(product, product2, product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        new Tuple("001", "아메리카노", SELLING),
                        new Tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("상품 번호 리스트를 가진 상품들을 조회한다.")
    void testFindAllByProductNumberIn(){
        //given
        Product product = createProduct("아메리카노", "001", SELLING, 4000);
        Product product2 = createProduct("카페라떼", "002", HOLD, 4500);
        Product product3 = createProduct("팥빙수", "003", STOP_SELLING, 7000);

        productRepository.saveAll(List.of(product, product2, product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        new Tuple("001", "아메리카노", SELLING),
                        new Tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품번호를 가져온다.")
    void testFindLatestProductNumber(){
        //given
        String targetProductNumber = "003";
        Product product = createProduct("아메리카노", "001", SELLING, 4000);
        Product product2 = createProduct("카페라떼", "002", HOLD, 4500);
        Product product3 = createProduct("팥빙수", targetProductNumber, STOP_SELLING, 7000);

        productRepository.saveAll(List.of(product, product2, product3));

        //when
        String latestProductNumber = productRepository.findLatestProductNumber().get();

        //then
        Assertions.assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품번호를 가져올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void testFindLatestProductNumberWhenProductIsEmpty(){
        //given //when
        Optional<String> latestProductNumber = productRepository.findLatestProductNumber();

        //then
        Assertions.assertThat(latestProductNumber).isEmpty();
    }

    private Product createProduct(String name, String productNumber, SellingStatus sellingStatus, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .type(ProductType.HANDMADE)
                .sellingStatus(sellingStatus)
                .price(price)
                .build();
    }

}