package laptopshop.service;

import laptopshop.entity.Customer;
import laptopshop.entity.Laptop;
import laptopshop.entity.Sale;
import laptopshop.repository.CustomerRepository;
import laptopshop.repository.LaptopRepository;
import laptopshop.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService {

	private final SaleRepository saleRepository;
	private final LaptopRepository laptopRepository;
	private final CustomerRepository customerRepository;

	public SaleService(SaleRepository saleRepository, LaptopRepository laptopRepository,
			CustomerRepository customerRepository) {
		this.saleRepository = saleRepository;
		this.laptopRepository = laptopRepository;
		this.customerRepository = customerRepository;
	}

	public List<Sale> getAll(Long customerId, Long laptopId) {
		if (customerId != null) {
			return saleRepository.findByCustomerId(customerId);
		}
		if (laptopId != null) {
			return saleRepository.findByLaptopId(laptopId);
		}
		return saleRepository.findAll();
	}

	public Sale get(Long id) {
		return saleRepository.findById(id).orElseThrow(() -> new NotFoundException("Verkauf", id));
	}

	/**
	 * Verkauft einen Laptop: prüft den Bestand, zieht die Menge vom Lager ab und berechnet den Gesamtpreis.
	 * Reicht der Bestand nicht, wird abgelehnt (409).
	 */
	@Transactional
	public Sale create(Sale sale) {
		Long cid = sale.getCustomer() == null ? null : sale.getCustomer().getId();
		Long lid = sale.getLaptop() == null ? null : sale.getLaptop().getId();
		if (cid == null || lid == null) {
			throw new IllegalArgumentException("customer.id und laptop.id sind erforderlich");
		}
		Customer customer = customerRepository.findById(cid).orElseThrow(() -> new NotFoundException("Kunde", cid));
		Laptop laptop = laptopRepository.findById(lid).orElseThrow(() -> new NotFoundException("Laptop", lid));
		if (laptop.getStock() < sale.getQuantity()) {
			throw new ConflictException("Nicht genug auf Lager: nur noch " + laptop.getStock() + " Stück");
		}
		laptop.setStock(laptop.getStock() - sale.getQuantity());
		sale.setId(null);
		sale.setCustomer(customer);
		sale.setLaptop(laptop);
		sale.setTotalPrice(laptop.getPrice().multiply(BigDecimal.valueOf(sale.getQuantity())));
		if (sale.getSaleDate() == null) {
			sale.setSaleDate(LocalDate.now());
		}
		return saleRepository.save(sale);
	}

	/** Storniert den Verkauf und legt die Stücke wieder ins Lager. */
	@Transactional
	public void delete(Long id) {
		Sale sale = get(id);
		Laptop laptop = sale.getLaptop();
		laptop.setStock(laptop.getStock() + sale.getQuantity());
		saleRepository.delete(sale);
	}
}
