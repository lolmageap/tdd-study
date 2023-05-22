package sample.cafekiosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.SellingStatus.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockRepository stockRepository;


    @Test
    @DisplayName("상품 번호 리스트로 재고를 조회한다.")
    void test(){
        //given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);
        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        //when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        Assertions.assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        new Tuple("001", 1),
                        new Tuple("002", 2)
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