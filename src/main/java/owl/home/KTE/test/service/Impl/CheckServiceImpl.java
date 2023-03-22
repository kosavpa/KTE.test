package owl.home.KTE.test.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.ProductForCheck;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.repo.CheckRepository;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static owl.home.KTE.test.service.util.ProductUtil.*;


@Component
public class CheckServiceImpl implements CheckService {
    @Autowired
    CheckRepository repository;
    @Autowired
    ClientService clientService;
    @Autowired
    ProductService productService;

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

    @Override
    public boolean deleteCheckById(long checkId) {
        repository.deleteById(checkId);

        return repository.existsById(checkId);
    }

    @Override
    public Check saveCheck(Check check) {
        return repository.save(check);
    }

    @Override
    public Check generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList) {
        TotalPriceShopingListResponse totalPriceShopingList = totalPriceProductResponseFromRequestShopingList(shopingList, productService);
        double totalPriceExcludeClientDiscount = totalPriceShopingList.getTotalPrice();

        if (totalPriceExcludeClientDiscount != totalPrice)
            throw new IllegalArgumentException("Bad total price in request!");

        Set<ProductForCheck> productsForCheck = productForCheckListFromTotalPriceShopingListRequest(shopingList, productService);
        Client client = clientService.clientById(clientId);
        double priceWhithClientDiscount = priceWithClientDiscount(client, productsForCheck);

        Check check = Check
                .builder()
                .client(client)
                .date(Calendar.getInstance().getTime())
                .shoppingList(productsForCheck)
                .finalPrice(priceWhithClientDiscount)
                .build();

        check = saveCheck(check);

        return check;
    }
}
