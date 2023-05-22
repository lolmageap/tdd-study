package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBySellingStatusIn(List<SellingStatus> sellingStatuses);
    List<Product> findAllByProductNumberIn(List<String> productNumber);


    @Query(value = "SELECT p.productNumber FROM Product p " +
            "WHERE p.productNumber = " +
            " ( SELECT MAX(p2.productNumber) FROM Product p2 )")
    Optional<String> findLatestProductNumber();
}
