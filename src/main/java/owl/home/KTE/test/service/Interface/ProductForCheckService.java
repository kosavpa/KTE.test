package owl.home.KTE.test.service.Interface;
/**
 * Сервисный слой товара для чека
 */

import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.product.ProductForCheck;

import java.util.List;


@Service
public interface ProductForCheckService {
    /**
     * Подсчет количество чеков в которых присуствует данный товар
     * @param productId - идентификатор товара
     * @return - количество чеков
     */
    int countByproductId(long productId);

    /**
     * Поиск чеков в которых присуствует данный товар
     * @param productId - идентификатор товара
     * @return - список товаров ассоциированных с чеком
     */
    List<ProductForCheck> productList(long productId);
}
