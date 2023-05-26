package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.SellingStatus;

import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static sample.cafekiosk.spring.domain.product.SellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.SellingStatus.SELLING;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        // before method

        // 각 테스트 입장에서 봤을 때 : 아예 몰라도 테스트 내용을 이해하는 데에 문제가 없는가?
        // 수정해도 모든 테스트에 영향을 주지 않는가?
    }

    @AfterEach
    void tearDown(){
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    void testCreateProduct(){
        //given
        Product product = createProduct("아메리카노", "001", SELLING, 4000);
        Product product2 = createProduct("카페라떼", "002", HOLD, 4500);
        productRepository.saveAll(List.of(product, product2));

        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("카푸치노")
                .price(5000)
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        //then
        Assertions.assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("003", HANDMADE, SELLING, 5000, "카푸치노");

        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(3)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, 4000, "아메리카노"),
                               tuple("002", HANDMADE, HOLD, 4500, "카페라떼"),
                               tuple("003", HANDMADE, SELLING, 5000, "카푸치노")
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    void testCreateProductWhenNoProduct(){
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("카푸치노")
                .price(5000)
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .build();

        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        //then
        Assertions.assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("001", HANDMADE, SELLING, 5000, "카푸치노");
    }

    private Product createProduct(String name, String productNumber, SellingStatus sellingStatus, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .type(HANDMADE)
                .sellingStatus(sellingStatus)
                .price(price)
                .build();
    }

}