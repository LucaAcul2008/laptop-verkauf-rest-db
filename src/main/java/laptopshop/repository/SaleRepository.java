package laptopshop.repository;

import laptopshop.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	List<Sale> findByCustomerId(Long customerId);

	List<Sale> findByLaptopId(Long laptopId);

	boolean existsByCustomerId(Long customerId);

	boolean existsByLaptopId(Long laptopId);
}
