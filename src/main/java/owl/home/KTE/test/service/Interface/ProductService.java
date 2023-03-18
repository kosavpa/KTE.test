package owl.home.KTE.test.service.Interface;


import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public interface ProductService {
    Product productById(long productId);
    boolean deleteProductById(long productId);
    Product saveProduct(Product product);
    List<Product> allProduct();
    AdditionalProductInfo additionalProductInfo(long productId, long clientId);
    TotalPriceShopingListResponse totalPriceResponse(HttpServletRequest request);
    void saveFeedbackProduct(long productId, long clientId, int amountStar);
    StatisticProductResponse statisticProduct(long productId);
    long generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList);
}
