package owl.home.KTE.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.Product;
import owl.home.KTE.test.service.Interface.ClientService;
import owl.home.KTE.test.service.Interface.ProductService;

@SpringBootApplication
public class MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(ClientService clientService, ProductService productService){
		return runner -> {
			clientService.saveClient(Client
					.builder()
					.name("Zosya")
					.personalDiscount1(0)
					.personalDiscount2(8)
					.build());

			clientService.saveClient(Client
					.builder()
					.name("Rijiy")
					.personalDiscount1(6)
					.personalDiscount2(19)
					.build());

			productService.saveProduct(Product
					.builder()
					.about("Best cat's laser, is wnderfull playing toy from fluffy kitty!")
					.name("Bluster gun")
					.price(999)
					.build());

			productService.saveProduct(Product
					.builder()
					.about("Any turtle will be happy on this road.")
					.name("Road of obstacles")
					.price(763)
					.build());

			productService.saveProduct(Product
					.builder()
					.about("Red ball turtles will not be left without attention!")
					.name("Sirius")
					.price(175)
					.build());

			productService.saveProduct(Product
					.builder()
					.about("This is mous not indistinguishable from the real.")
					.name("True mouse")
					.price(612)
					.build());
		};
	}
}
