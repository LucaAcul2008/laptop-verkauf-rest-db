package laptopshop.repository;

import laptopshop.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {

	List<Laptop> findByBrandIgnoreCase(String brand);

	List<Laptop> findByPriceLessThanEqual(BigDecimal maxPrice);

	List<Laptop> findByStockLessThan(int stock);
}
