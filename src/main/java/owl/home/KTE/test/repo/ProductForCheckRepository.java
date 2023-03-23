package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.List;


@Repository
public interface ProductForCheckRepository extends JpaRepository<ProductForCheck, Long> {
    int countByProductId(long productId);
    List<ProductForCheck> findProductForCheckByproductId(long productId);
}
