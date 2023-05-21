package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static sample.cafekiosk.spring.domain.product.SellingStatus.*;

@SpringBootTest
class ProductRepositoryTest {

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

    private static Product createProduct(String name, String productNumber, SellingStatus sellingStatus, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .type(ProductType.HANDMADE)
                .sellingStatus(sellingStatus)
                .price(price)
                .build();
    }

}