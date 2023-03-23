package owl.home.KTE.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.service.Interface.CheckService;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@Autowired
	ProductService productService;
	@Autowired
	CheckService checkService;

	@Async
	@Scheduled(fixedRate = 86400000, initialDelay = 5000)
	public void clearAndSetDiscount(){
		productService.clearAndSetDiscountColumn();
	}

	@Autowired
	@Async
	@Scheduled(fixedRate = 86400000, initialDelay = 5000)
	public void resetSeq(){
		checkService.resetSequence();
	}
}