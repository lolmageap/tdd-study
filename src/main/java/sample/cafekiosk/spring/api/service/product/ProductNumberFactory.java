package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String createNextCreateNumber(){
        Optional<String> productNumber = productRepository.findLatestProductNumber();

        return productNumber.map(s -> {
            Integer latestProductNumberInt = Integer.valueOf(s);
            Integer nextProductNumber = latestProductNumberInt + 1;
            return String.format("%03d", nextProductNumber);
        }).orElse("001");
    }

}
