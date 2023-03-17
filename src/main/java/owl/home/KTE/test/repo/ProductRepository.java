package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.product.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
