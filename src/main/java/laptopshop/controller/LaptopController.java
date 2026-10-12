package laptopshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laptopshop.entity.Laptop;
import laptopshop.service.LaptopService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/laptops")
@Tag(name = "Laptops")
public class LaptopController {

	private final LaptopService service;

	public LaptopController(LaptopService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Alle Laptops, optional gefiltert nach Marke, Maximalpreis oder niedrigem Bestand")
	public List<Laptop> getAll(@RequestParam(required = false) String brand,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) Integer lowStockBelow) {
		return service.getAll(brand, maxPrice, lowStockBelow);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Einen Laptop per ID")
	public Laptop get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Laptop anlegen")
	public Laptop create(@Valid @RequestBody Laptop laptop) {
		return service.create(laptop);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Laptop ändern (z. B. Preis oder Bestand)")
	public Laptop update(@PathVariable Long id, @Valid @RequestBody Laptop laptop) {
		return service.update(id, laptop);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Laptop löschen (nur wenn nie verkauft)")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
