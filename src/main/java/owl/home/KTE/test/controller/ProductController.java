package owl.home.KTE.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ProductService;
import owl.home.KTE.test.service.Interface.RatingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static owl.home.KTE.test.service.util.ProductUtil.*;


@RestController
@RequestMapping("/rest/v1/product-productService")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private RatingService ratingService;

    @GetMapping("/all")
    ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productService.allProduct());
    }

    @GetMapping("/additional/{productId}/{clientId}")
    ResponseEntity<AdditionalProductInfo> getAdditionalProductInfo(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId){
        return ResponseEntity.ok(productService.additionalProductInfo(productId, clientId));
    }

    @GetMapping("/total-price")
    ResponseEntity<TotalPriceShopingListResponse> totalPriceShopingLists(HttpServletRequest request){
        List<TotalPriceShopingListRequest> shopingList = totalPriseRequestList(request);

        return ResponseEntity.ok(productService.totalPriceResponse(shopingList));
    }

    @PutMapping("/feedback/{productId}/{clientId}/{amountStar}")
    ResponseEntity<AdditionalProductInfo> feedBackProduct(
            @PathVariable("productId") long productId,
            @PathVariable("clientId") long clientId,
            @PathVariable("amountStar") int amountStar
    ){
        ratingService.saveFeedbackProduct(productId, clientId, amountStar);

        return ResponseEntity.ok(productService.additionalProductInfo(productId, clientId));
    }

    @GetMapping("/statistic/{productId}")
    ResponseEntity<StatisticProductResponse> productStatisctic(@PathVariable("productId") long productId){
        return ResponseEntity.ok(productService.statisticProduct(productId));
    }

    @PostMapping("/generate-check/{clientId}/{totalPrice}")
    ResponseEntity<Check> generateCheck(
            @PathVariable("clientId") long clientId,
            @PathVariable("totalPrice") double totalPrice,
            @RequestBody List<TotalPriceShopingListRequest> shopingList
    ){
        return ResponseEntity.ok(checkService.generateCheck(clientId, totalPrice, shopingList));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}