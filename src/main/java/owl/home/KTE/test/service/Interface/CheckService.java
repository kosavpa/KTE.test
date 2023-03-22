package owl.home.KTE.test.service.Interface;


import org.springframework.stereotype.Service;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;

import java.util.List;


@Service
public interface CheckService {
    Check checkById(long checkId);
    List<Check> checksByClientId(long clientId);
    boolean deleteCheckById(long checkId);
    Check saveCheck(Check check);
    Check generateCheck(long clientId, double totalPrice, List<TotalPriceShopingListRequest> shopingList);
}