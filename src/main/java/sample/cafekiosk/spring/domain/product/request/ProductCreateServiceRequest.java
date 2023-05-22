package sample.cafekiosk.spring.domain.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor
public class ProductCreateServiceRequest {

    private ProductType type;

    private SellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private ProductCreateServiceRequest(ProductType type, SellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextCreateNumber) {
        return Product.builder()
                .name(this.name)
                .productNumber(nextCreateNumber)
                .sellingStatus(this.sellingStatus)
                .type(this.type)
                .price(this.price)
                .build();
    }
}
