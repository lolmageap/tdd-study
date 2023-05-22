package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id @GeneratedValue
    private Long id;

    private String productNumber;

    private Integer quantity;

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    public boolean isQuantityLessThen(Integer quantity) {
        return this.quantity < quantity;
    }

    public void deductQuantity(Integer quantity) {
        if (this.quantity < quantity)
            throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");

        this.quantity -= quantity;
    }
}
