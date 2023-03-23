package owl.home.KTE.test.service.Interface;
/**
 * Сервисный слой товара
 */

import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;

import java.util.List;


@Service
public interface ProductService {
    /**
     * Посик товара по id
     * @param productId - идентификатор товара
     * @return - товар
     */
    Product productById(long productId);

    /**
     * Удаление товара по id
     * @param productId - идентификатор товара
     * @return - статус операции (true - успех, false - провал)
     */
    boolean deleteProductById(long productId);

    /**
     * Сохранение товара
     * @param product - идентификатор товара
     * @return - товар
     */
    Product saveProduct(Product product);

    /**
     * Поиск всех товаров
     * @return - Список продуктов
     */
    List<Product> allProduct();

    /**
     * Создание информации о товаре
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @return - информация о товаре
     */
    AdditionalProductInfo additionalProductInfo(long productId, long clientId);

    /**
     * Итоговая сумма товара в копейках
     * @param request - список товаров в запросе для подсчёта итогой суммы
     * @return - итоговую стоимость в копейках
     */
    TotalPriceShopingListResponse totalPriceResponse(List<TotalPriceShopingListRequest> request);

    /**
     * Создание статистики о товаре
     * @param productId - идентификатор товара
     * @return - статистика о товаре
     */
    StatisticProductResponse statisticProduct(long productId);
}
