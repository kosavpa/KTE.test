package owl.home.KTE.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.*;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.Interface.RatingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static owl.home.KTE.test.service.util.ProductUtil.*;


@RestController
@RequestMapping("/rest/v1/product-productService")
public class ProductController {
    private ProductService productService;
    private CheckService checkService;
    private RatingService ratingService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }

    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * @return список товаров
     */
    @GetMapping("/all")
    ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productService.allProduct());
    }

    /**
     * @param productId - идентификатор продукта
     * @param clientId - идентификатор клиента
     * @return - информацию о товаре
     */
    @GetMapping("/additional/{productId}/{clientId}")
    ResponseEntity<AdditionalProductInfo> getAdditionalProductInfo(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId){
        return ResponseEntity.ok(productService.additionalProductInfo(productId, clientId));
    }

    /**
     * @param request - сервлетный запрос
     * @return - ответ итоговой стоимости товаров
     */
    @GetMapping("/total-price")
    ResponseEntity<TotalPriceShopingListResponse> totalPriceShopingLists(HttpServletRequest request){
        List<TotalPriceShopingListRequest> shopingList = totalPriseRequestList(request);

        return ResponseEntity.ok(productService.totalPriceResponse(shopingList));
    }

    /**
     * Оценка товара
     * @param productId - идентификатор продукта
     * @param clientId - идентификатор клиента
     * @param amountStar - количество звезд
     * @return - информация о продукте
     */
    @PutMapping("/feedback/{productId}/{clientId}/{amountStar}")
    ResponseEntity<AdditionalProductInfo> feedBackProduct(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId,
            @PathVariable("amountStar") int amountStar
    ){

        ratingService.saveFeedbackProduct(productId, clientId, amountStar);

        AdditionalProductInfo productInfo = null;
        try {
             productInfo = productService.additionalProductInfo(productId, clientId);
        } catch (Exception e){
            if (e.getClass().equals(IllegalArgumentException.class) & e.getMessage().equals("Rating with this id not found!"))
                return ResponseEntity.ok(ratingService.createEmptyAdditonalProductInfo(productId, clientId));
        }

        return ResponseEntity.ok(productInfo);
    }

    /**
     * @param productId - идентификатор продукта
     * @return - статистика продукта
     */
    @GetMapping("/statistic/{productId}")
    ResponseEntity<StatisticProductResponse> productStatisctic(@PathVariable("productId") long productId){
        return ResponseEntity.ok(productService.statisticProduct(productId));
    }

    /**
     * @param clientId - идентификатор клиента
     * @param totalPrice - итоговая сумма
     * @param shopingList - список запроса итоговой стоимости товара
     * @return - чек
     */
    @PostMapping("/generate-check/{clientId}/{totalPrice}")
    ResponseEntity<CheckForResponce> generateCheck(
            @PathVariable("clientId") long clientId,
            @PathVariable("totalPrice") double totalPrice,
            @RequestBody List<TotalPriceShopingListRequest> shopingList
    ){
        return ResponseEntity.ok(checkService.generateCheck(clientId, totalPrice, shopingList));
    }

    /**
     * Метод для отлова исключений которые прокидываются в серверном слое и отображения сообщения
     * @param exception - исключение которое проброшено
     * @return - ответ с кодом 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}