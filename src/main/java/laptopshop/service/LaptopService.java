package laptopshop.service;

import laptopshop.entity.Laptop;
import laptopshop.repository.LaptopRepository;
import laptopshop.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LaptopService {

	private final LaptopRepository laptopRepository;
	private final SaleRepository saleRepository;

	public LaptopService(LaptopRepository laptopRepository, SaleRepository saleRepository) {
		this.laptopRepository = laptopRepository;
		this.saleRepository = saleRepository;
	}

	/** Optionale Filter: Marke, Maximalpreis oder "wenig auf Lager" (Bestand unter lowStockBelow). */
	public List<Laptop> getAll(String brand, BigDecimal maxPrice, Integer lowStockBelow) {
		if (brand != null) {
			return laptopRepository.findByBrandIgnoreCase(brand);
		}
		if (maxPrice != null) {
			return laptopRepository.findByPriceLessThanEqual(maxPrice);
		}
		if (lowStockBelow != null) {
			return laptopRepository.findByStockLessThan(lowStockBelow);
		}
		return laptopRepository.findAll();
	}

	public Laptop get(Long id) {
		return laptopRepository.findById(id).orElseThrow(() -> new NotFoundException("Laptop", id));
	}

	public Laptop create(Laptop laptop) {
		laptop.setId(null);
		return laptopRepository.save(laptop);
	}

	public Laptop update(Long id, Laptop data) {
		Laptop existing = get(id);
		existing.setBrand(data.getBrand());
		existing.setModel(data.getModel());
		existing.setCpu(data.getCpu());
		existing.setRamGb(data.getRamGb());
		existing.setStorageGb(data.getStorageGb());
		existing.setPrice(data.getPrice());
		existing.setStock(data.getStock());
		return laptopRepository.save(existing);
	}

	public void delete(Long id) {
		Laptop existing = get(id);
		if (saleRepository.existsByLaptopId(id)) {
			throw new ConflictException("Laptop wurde schon verkauft und kann nicht gelöscht werden");
		}
		laptopRepository.delete(existing);
	}
}
