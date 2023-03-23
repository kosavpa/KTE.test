package owl.home.KTE.test.service.Impl;
/**
 * Имплиментация сервисного слоя чеков, некоторые методы могут кидать непроверяемые исключения
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.util.CheckForResponce;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.repo.CheckRepository;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.util.Carrensy;

import java.text.SimpleDateFormat;
import java.util.*;

import static owl.home.KTE.test.service.util.ProductUtil.*;


@Component
@Transactional(readOnly = true)
public class CheckServiceImpl implements CheckService {
    private CheckRepository repository;
    private ClientService clientService;
    private ProductService productService;

    @Autowired
    public void setRepository(CheckRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Ищет чек по идентификатору. Может бросить RuntimeException, если чека с таким id нет,
     * с соответствующим сообщением
     * @param checkId - идентификатор чека
     * @return - чек
     */
    @Override
    public Check checkById(long checkId) {
        return repository.findById(checkId).orElseThrow(
                () -> new IllegalArgumentException("Check with this id not found!")
        );
    }

    @Override
    public List<Check> checksByClientId(long clientId) {
        return repository.findCheckByClientId(clientId);
    }

    @Transactional
    @Override
    public boolean deleteCheckById(long checkId) {
        repository.deleteById(checkId);

        return repository.existsById(checkId);
    }

    @Transactional
    @Override
    public Check saveCheck(Check check) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long checkId = repository.saveWithSequence(dateFormat.format(check.getDate()), check.getFinalPrice(), check.getClient().getId());

        return checkById(checkId);
    }

    /**
     * Создание и сохраниение чека. Перед этими операциями происходит проверка введеной суммы и суммы расчитаной, после идет подсчет
     * итоговой стоимости с учетом всех скидок и если они превышают 18% устанавливается скидка равноя 18%, ответ возвращается специальным объектом
     * @param clientId - идентификатор клиента
     * @param totalPrice - итоговая сумма (в копейках)
     * @param shopingList - список покупок
     * @return - объкт чека дляя ответа
     */
    @Transactional
    @Override
    public CheckForResponce generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList) {
        Map<Long, Product> productMap = new HashMap<>();
        shopingList
                .stream()
                .forEach(tpr -> productMap.put(tpr.getProductId(), productService.productById(tpr.getProductId())));

        TotalPriceShopingListResponse totalPriceShopingList = totalPriceProductResponseFromRequestShopingList(shopingList, Carrensy.KOP, productMap);
        double totalPriceExcludeClientDiscount = totalPriceShopingList.getTotalPrice();

        if (totalPriceExcludeClientDiscount != totalPrice)
            throw new IllegalArgumentException("Bad total price in request!");

        Set<ProductForCheck> productsForCheck = productForCheckListFromTotalPriceShopingListRequest(shopingList, productMap);
        Client client = clientService.clientById(clientId);
        double priceWhithClientDiscount = priceWithClientDiscount(client, productsForCheck);

        Check check = Check
                .builder()
                .client(client)
                .date(Calendar.getInstance().getTime())
                .shoppingList(productsForCheck)
                .finalPrice(priceWhithClientDiscount)
                .build();

        long checkNumber = saveCheck(check).getNumber();
        String checkNumberForResponse = createCheckNumberForResponse(checkNumber);

        CheckForResponce checkForResponce = CheckForResponce
                .builder()
                .client(check.getClient())
                .date(check.getDate())
                .finalPrice(check.getFinalPrice())
                .shoppingList(check.getShoppingList())
                .number(checkNumberForResponse)
                .build();

        return checkForResponce;
    }

    /**
     * Сброс последовательности
     */
    @Transactional
    @Override
    public void resetSequence() {
        repository.alterSequence();
    }

    public static String createCheckNumberForResponse(long checkNumber) {
        String result = String.valueOf(checkNumber);

        if(result.length() < 4){
            result = String.format("%05d", checkNumber);
        }

        return result;
    }


}
