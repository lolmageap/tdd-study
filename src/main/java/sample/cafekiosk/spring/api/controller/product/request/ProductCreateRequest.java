package sample.cafekiosk.spring.api.controller.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.domain.product.request.ProductCreateServiceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private SellingStatus sellingStatus;

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @Positive(message = "상품 가격은 0보다 커야합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, SellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest(){
        return ProductCreateServiceRequest.builder()
                .name(this.name)
                .price(this.price)
                .sellingStatus(this.sellingStatus)
                .type(this.type)
                .build();
    }

}
