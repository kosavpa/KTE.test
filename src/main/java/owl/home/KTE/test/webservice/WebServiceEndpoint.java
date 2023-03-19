package owl.home.KTE.test.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import owl.home.KTE.test.model.check.Check;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.model.util.*;
import owl.home.KTE.test.model.util.AdditionalProductInfo;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;
import test_web_service.kte.*;

import static owl.home.KTE.test.service.util.ClientUtil.*;
import static owl.home.KTE.test.service.util.ProductUtil.*;

import java.util.List;


@Endpoint
public class WebServiceEndpoint {
    @Autowired
    ClientService clientService;
    @Autowired
    private ProductService productService;
    private static final String NAMESPACE_URI = "http://KTE-web-service/";

    @PayloadRoot(namespace = NAMESPACE_URI + "client/all-client", localPart = "getAllClientRequest")
    @ResponsePayload
    public GetAllClientResponse getCountry() {
        List<Client> clients = clientService.allClient();
        List<test_web_service.kte.Client> wsClients = mapClientListToWsClientList(clients);

        GetAllClientResponse clientResponse = new GetAllClientResponse();
        ClientList clientList = new ClientList();
        clientList.getClient().addAll(wsClients);
        clientResponse.setClients(clientList);

        return clientResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "client/update-discount", localPart = "updateClientDiscountRequest")
    @ResponsePayload
    public UpdateClientDiscountResponse updateClientDiscount(@RequestPayload UpdateClientDiscountRequest request) {
        clientService.updateDiscounts(request.getClientId(), request.getPersonalDiscount1(), request.getPersonalDiscount2());
        Client client = clientService.clientById(request.getClientId());

        UpdateClientDiscountResponse response = new UpdateClientDiscountResponse();
        response.setClient(mapClientToWsClient(clientService.clientById(request.getClientId())));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "client/statistic", localPart = "getClientStatisticRequest")
    @ResponsePayload
    public GetClientStatisticResponse statisticClient(@RequestPayload GetClientStatisticRequest request) {
        StatisticClientResponse statisticClient = clientService.statisticClient(request.getClientId());

        GetClientStatisticResponse response = new GetClientStatisticResponse();
        response.setClientStatistic(mapStatisticClientToWsStatisticClient(statisticClient));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/all-product", localPart = "getAllProductRequest")
    @ResponsePayload
    public GetAllProductResponse statisticClient() {
        List<Product> products = productService.allProduct();
        List<test_web_service.kte.Product> wsProduct = mapProductListToWsProductList(products);
        ProductList productList = new ProductList();
        productList.getProduct().addAll(wsProduct);

        GetAllProductResponse response = new GetAllProductResponse();

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/additional-product", localPart = "getAdditionalProductInfoRequest")
    @ResponsePayload
    public GetAdditionalProductInfoResponse additionalProduct(@RequestPayload GetAdditionalProductInfoRequest request) {
        AdditionalProductInfo productInfo = productService.additionalProductInfo(request.getProductId(), request.getClientId());

        GetAdditionalProductInfoResponse response = new GetAdditionalProductInfoResponse();
        response.setAdditionalProductInfo(mapAdditionalProductInfoToWsAdditionalProductInfo(productInfo));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/total-price", localPart = "getTotalPriceRequest")
    @ResponsePayload
    public GetTotalPriceResponse totalPrice(@RequestPayload GetTotalPriceRequest request) {
        GetTotalPriceResponse response = new GetTotalPriceResponse();
        TotalPriceResponse wsRequest = response.getTotalPriceResponse();

        List<TotalPriceShopingListRequest> totalPriceShopingListRequests = mapWsRequestTototalPriceShopingListRequests(request.getTotalPriceRequestList());
        TotalPriceShopingListResponse totalPriceShopingListResponse =
                totalPriceProductResponseFromRequestShopingList(totalPriceShopingListRequests, productService);
        response.setTotalPriceResponse(mapTotalPriceShopingListToWsTotalPrise(totalPriceShopingListResponse));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/create-feedback", localPart = "createFeedbackProductRequest")
    @ResponsePayload
    public CreateFeedbackProductResponce createFeedback(@RequestPayload CreateFeedbackProductRequest request) {
        productService.saveFeedbackProduct(request.getProductId(), request.getClientId(), request.getAmountStar());

        CreateFeedbackProductResponce responce = new CreateFeedbackProductResponce();
        AdditionalProductInfo productInfo = productService.additionalProductInfo(request.getProductId(), request.getClientId());
        responce.setAdditionalProductInfo(mapAdditionalProductInfoToWsAdditionalProductInfo(productInfo));

        return responce;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/statistic", localPart = "getProductStatisticRequest")
    @ResponsePayload
    public GetProductStatisticResponse createFeedback(@RequestPayload GetProductStatisticRequest request) {
        StatisticProductResponse statisticProductResponse = productService.statisticProduct(request.getProductId());
        StatisticProduct wsStatistic = mapStatisticProductToWsStatisticProduct(statisticProductResponse);

        GetProductStatisticResponse response = new GetProductStatisticResponse();
        response.setProductStatistic(wsStatistic);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI + "product/generate-check", localPart = "createCheckRequest")
    @ResponsePayload
    public CreateCheckResponse createCheck(@RequestPayload CreateCheckRequest request) {
        List<TotalPriceShopingListRequest> shopingListRequests = mapWsRequestTototalPriceShopingListRequests(request.getTotalPriceRequestList());
        Check check = productService.generateCheck(request.getClientId(), request.getTotalPrice(), shopingListRequests);

        CreateCheckResponse response = new CreateCheckResponse();
        response.setCheck(mapCheckToWsCheck(check));

        return response;
    }
}