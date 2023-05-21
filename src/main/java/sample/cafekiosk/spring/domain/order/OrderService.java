package sample.cafekiosk.spring.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime now) {
        List<String> productNumber = request.getProductNumbers();
        List<Product> duplicateProducts = findProductsBy(productNumber);

        Order order = Order.create(duplicateProducts, now);
        Order saveOrder = orderRepository.save(order);
        return OrderResponse.of(saveOrder);
    }

    private List<Product> findProductsBy(List<String> productNumber) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumber);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicateProducts = productNumber.stream()
                .map(p -> productMap.get(p))
                .collect(Collectors.toList());

        return duplicateProducts;
    }

}
