package laptopshop.config;

import laptopshop.entity.Customer;
import laptopshop.entity.Laptop;
import laptopshop.entity.Sale;
import laptopshop.repository.CustomerRepository;
import laptopshop.repository.LaptopRepository;
import laptopshop.repository.SaleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Befüllt die H2-In-Memory-DB beim Start mit Testdaten. */
@Configuration
public class TestDataLoader {

	@Bean
	CommandLineRunner loadData(LaptopRepository laptops, CustomerRepository customers, SaleRepository sales) {
		return args -> {
			Laptop hp = laptops.save(new Laptop("HP", "OmniBook X Flip 16", "Intel Core Ultra 7", 32, 1024, new BigDecimal("1299.00"), 5));
			Laptop msi = laptops.save(new Laptop("MSI", "Thin GF63", "Intel Core i5-12450H", 16, 512, new BigDecimal("599.00"), 2));
			Laptop lenovo = laptops.save(new Laptop("Lenovo", "ThinkPad E14", "AMD Ryzen 5 7535U", 16, 512, new BigDecimal("749.00"), 8));
			laptops.save(new Laptop("Apple", "MacBook Air 13 M3", "Apple M3", 16, 512, new BigDecimal("1199.00"), 1));

			Customer anna = customers.save(new Customer("Anna Huber", "anna.huber@example.com"));
			Customer max = customers.save(new Customer("Max Gruber", "max.gruber@example.com"));
			customers.save(new Customer("Sophie Berger", "sophie.berger@example.com"));

			sales.save(new Sale(anna, lenovo, 1, new BigDecimal("749.00"), LocalDate.of(2026, 10, 1)));
			sales.save(new Sale(max, msi, 1, new BigDecimal("599.00"), LocalDate.of(2026, 10, 5)));
		};
	}
}
