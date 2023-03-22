package owl.home.KTE.test.webService.Interface;


import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.model.util.StatisticProductResponse;
import owl.home.KTE.test.model.util.TotalPriceShopingListRequest;
import owl.home.KTE.test.model.util.TotalPriceShopingListResponse;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;


@WebService(name = "ProductService" ,targetNamespace = "http://kte.test-web-service")
public interface ProductWebService {
    @WebResult(name = "Product")
    @RequestWrapper(
            localName = "getAllProductRequest",
            className = "owl.home.KTE.test.webservice.AllProductRequest")
    @ResponseWrapper(
            localName = "getAllProductResponse",
            className = "owl.home.KTE.test.webservice.AllProductResponse")
    List<Product> getAllProduct();

    @WebResult(name = "AdditionalProductInfo")
    @RequestWrapper(
            localName = "getAdditionalProductInfoRequest",
            className = "owl.home.KTE.test.webservice.AdditionalProductInfoRequest")
    @ResponseWrapper(
            localName = "getAdditionalProductInfoResponse",
            className = "owl.home.KTE.test.webservice.AdditionalProductInfoResponse")
    AdditionalProductInfo getAdditionalProductInfo(
            @WebParam(name = "productId") long productId,
            @WebParam(name = "clientId") long clientId);

    @WebResult(name = "TotalPriceShopingLists")
    @RequestWrapper(
            localName = "getTotalPriceShopingListsRequest",
            className = "owl.home.KTE.test.webservice.TotalPriceShopingListsRequest")
    @ResponseWrapper(
            localName = "getTotalPriceShopingListsResponse",
            className = "owl.home.KTE.test.webservice.TotalPriceShopingListsResponse")
    TotalPriceShopingListResponse totalPriceShopingLists(@WebParam(name = "ShopingList")List<TotalPriceShopingListRequest> shopingList);

    @WebResult(name = "FeedBackProduct")
    @RequestWrapper(
            localName = "getFeedBackProductRequest",
            className = "owl.home.KTE.test.webservice.FeedBackProductRequest")
    @ResponseWrapper(
            localName = "getFeedBackProductResponse",
            className = "owl.home.KTE.test.webservice.FeedBackProductResponse")
    AdditionalProductInfo feedBackProduct(
            @WebParam(name = "productId") long productId,
            @WebParam(name = "clientId") long clientId,
            @WebParam(name = "amountStar") int amountStar);

    @WebResult(name = "ProductStatisctic")
    @RequestWrapper(
            localName = "getProductStatiscticRequest",
            className = "owl.home.KTE.test.webservice.ProductStatiscticRequest")
    @ResponseWrapper(
            localName = "getProductStatiscticResponse",
            className = "owl.home.KTE.test.webservice.ProductStatiscticResponse")
    StatisticProductResponse productStatisctic(@WebParam(name = "productId") long productId);

    @WebResult(name = "GenerateCheck")
    @RequestWrapper(
            localName = "getGenerateCheckRequest",
            className = "owl.home.KTE.test.webservice.GenerateCheckRequest")
    @ResponseWrapper(
            localName = "getGenerateCheckResponse",
            className = "owl.home.KTE.test.webservice.GenerateCheckResponse")
    Check generateCheck(
            @WebParam(name = "clientId") long clientId,
            @WebParam(name = "totalPrice") double totalPrice,
            @WebParam(name = "TotalPriceShopingListRequest") List<TotalPriceShopingListRequest> shopingList);
}
