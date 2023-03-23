package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.repo.ShopingListRepository;
import owl.home.KTE.test.service.Interface.ProductForCheckService;

import java.util.List;


@Component
@Transactional(readOnly = true)
public class ProductForCheckServiceImpl implements ProductForCheckService {
    @Autowired
    private ShopingListRepository repository;

    @Override
    public int countByproductId(long productId) {
        return repository.countByProductId(productId);
    }

    @Override
    public List<ProductForCheck> productList(long productId) {
        return repository.findShopingListByproductId(productId);
    }
}
