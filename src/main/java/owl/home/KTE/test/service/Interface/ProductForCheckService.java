package owl.home.KTE.test.service.Interface;


import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.List;


@Service
public interface ProductForCheckService {
    int countByproductId(long productId);
    List<ProductForCheck> productList(long productId);
}
