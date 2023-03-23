package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.product.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query(value = "update product_table set product_discount = 0", nativeQuery = true)
    void clearDiscountColumn();
}
