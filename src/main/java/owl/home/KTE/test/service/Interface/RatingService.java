package owl.home.KTE.test.service.Interface;
/**
 * Сервисный слой рейтинга
 */

import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.product.Rating;
import owl.home.KTE.test.model.util.AdditionalProductInfo;

import java.util.List;
import java.util.Optional;


@Service
public interface RatingService {
    /**
     * Поиск рейтинга по id
     * @param ratingId - идентификатор рейтинга
     * @return - рейтинг
     */
    Rating getRatingById(long ratingId);

    /**
     * Сохранение рейтинга
     * @param rating - рейтинг товара, который оценил клиент
     * @return - рейтинг
     */
    Rating saveRating(Rating rating);

    /**
     * Удаление рейтинга по id
     * @param id - идентификатор рейтинга
     * @return статус операции (true - успех, false - провал)
     */
    boolean deleteRatingById(long id);

    /**
     * Поиск рейтинга по id товара
     * @param productId - идентификатор товара
     * @return список рейтингов
     */
    List<Rating> findByProductId(long productId);

    /**
     * Поиск рейтинга ассоциированный с товаром клиентом
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @return Optinal содержащий рейтинг
     */
    Optional<Rating> findByProductIdAndClientId(long productId, long clientId);

    /**
     * Создает рейтинг товара на основании отзыва клиента
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @param amountStar - отзыв о товаре (количество звезд)
     */
    void saveFeedbackProduct(long productId, long clientId, int amountStar);

    /**
     * Создает информацию о товаре с "отозванной" оценкой пользователя
     * @param productId - идентификатор товара
     * @param clientId - идентификатор клиента
     * @return информация с "отозванной" оценкой
     */
    AdditionalProductInfo createEmptyAdditonalProductInfo(long productId, long clientId);
}
