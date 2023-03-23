package owl.home.KTE.test.service.Interface;
/**
 * Сервисный слой для чеков
 */

import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.util.CheckForResponce;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;

import java.util.List;


@Service
public interface CheckService {
    /**
     * Поиск чеков по id
     * @param checkId - идентификатор чека
     * @return - чек
     */
    Check checkById(long checkId);

    /**
     * Поиск всех чеков связанных с клиентом
     * @param clientId - идентификатор клииента
     * @return - список чеков
     */
    List<Check> checksByClientId(long clientId);

    /**
     * Удаление чека по id
     * @param checkId - идентификатор чека
     * @return - статус операции (true - успех, false - провал)
     */
    boolean deleteCheckById(long checkId);

    /**
     * Сохранение чека
     * @param check - чек
     * @return - чек
     */
    Check saveCheck(Check check);

    /**
     * Создание чека(регистраци покупки)
     * @param clientId - идентификатор клиента
     * @param totalPrice - итоговая сумма (в копейках)
     * @param shopingList - список покупок
     * @return - чек для ответа
     */
    CheckForResponce generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList);
    /**
     * Обновляет sequencе(сбрасывает в первоначальное значение) раз в сутки
     */
    void resetSequence();
}