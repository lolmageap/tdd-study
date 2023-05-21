package sample.cafekiosk.spring.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime now) {
        List<String> productNumber = request.getProductNumber();

        List<Product> products = productRepository.findAllByProductNumberIn(productNumber);
        Order order = Order.create(products, now);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);
    }

}
