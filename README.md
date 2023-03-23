# KTE.test
Test example for KTE company
# Используемые технологии
Spring Boot, spring Data, Spring Web, Spring Web Services, PostgreSQL, ApacheCXF, Lombok.
# Описание
## SOAP:
###  - Client service: 
**wsdl-файл находится по URI** - "/services/ClientService?wsdl";    
**поддерживаемые операции** - getAllClientRequest(список всех клиентов), getClientStatisticRequest(получение статистики клиента),
getUpdateDiscountRequest(обновление скидок клиента);    
*аттрибуты которые необходимо включить в запрос и ответы сервера перечислены в javadoc.*
###  - Product service: 
**wsdl-файл по адресу** - "/services/ProductService?wsdl";    
**поддерживаемые операции** - getAllProductRequest(список всех продуктов), getAdditionalProductInfoRequest(информация о продукте связанным с клиентом),
getTotalPriceShopingListsRequest(итоговая стоимость списка покупок), getFeedBackProductRequest(оставить отыв о товаре),
getProductStatiscticRequest(статистика продукта), getGenerateCheckRequest(создание чека (оформление покупки));
*аттрибуты которые необходимо включить в запрос и ответы сервера перечислены в javadoc.*
## REST:
###  - Client resource:
**Base URI** - /rest/v1/client-service;   
**Поддерживаемые операции:**    
 **GET:** /all, /statistic/{clientId}(список всех клиентов);    
 **Patch:** /update-discounts/{clientId}/{discount1}/{discount2};   
 *аттрибуты которые необходимо включить в запрос и ответы сервера перечислены в javadoc.*
### - Product resource:
**Base URI** - /rest/v1/product-productService;   
**Поддерживаемые операции:**
**GET:** /all(список всех продуктов), /additional/{productId}/{clientId}(информация о продукте связанным с клиентом),
/total-price(итоговая стоимость списка покупок), /statistic/{productId}(статистика продукта);   
**PUT:** /feedback/{productId}/{clientId}/{amountStar}(оставить отыв о товаре);
**POST:** /generate-check/{clientId}/{totalPrice}(создание чека (оформление покупки));
*аттрибуты которые необходимо включить в запрос и ответы сервера перечислены в javadoc.*
## Scheduling task
В проекте для обновления поля скидки товара и генератора последовательности предусмотрено задание по расписанию. При запуске задание стартует и раз в сутки выбирает товар на который будет действовать скидка и перезапускает генератор последовательности
## SoapUi
В данной папке лежат проекты тестов, а также сгенерированные отчеты о тестах в формате .csv.
## JavaDoc
В данной папке лежит сгенерированный javadoc. Для просмотра нужно запустить index.html.
## Docker
В данной папке лежат собранный .jar архив, dockerfile и docker-compose.yml. Таким образом, для запуска проекта все что нужно - это ввести в вашей cli docker-compose up.
