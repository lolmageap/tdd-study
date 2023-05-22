package sample.cafekiosk.spring.domain.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumbers;

    @Builder
    private OrderCreateServiceRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

}
