package sample.cafekiosk.spring.api.service.product;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductResponse {

    private Long id;

    private String productNumber;

    private ProductType type;

    private SellingStatus sellingStatus;

    private String name;

    private int price;

    public static ProductResponse of(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType type, SellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
