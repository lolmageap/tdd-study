package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.domain.product.request.ProductCreateServiceRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;

    public ProductResponse createProduct(ProductCreateServiceRequest request){
        String nextCreateNumber = productNumberFactory.createNextCreateNumber();

        Product product = request.toEntity(nextCreateNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts(){
        List<Product> products = productRepository.findAllBySellingStatusIn(SellingStatus.forDisplay());

        return products.stream().map(ProductResponse::of)
                .collect(Collectors.toList());
    }

}
