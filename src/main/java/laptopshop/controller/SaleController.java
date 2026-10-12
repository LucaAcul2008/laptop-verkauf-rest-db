package laptopshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laptopshop.entity.Sale;
import laptopshop.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Verkäufe")
public class SaleController {

	private final SaleService service;

	public SaleController(SaleService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Alle Verkäufe, optional pro Kunde oder pro Laptop")
	public List<Sale> getAll(@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) Long laptopId) {
		return service.getAll(customerId, laptopId);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Einen Verkauf per ID")
	public Sale get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Laptop verkaufen: Bestand sinkt, Gesamtpreis wird berechnet")
	public Sale create(@Valid @RequestBody Sale sale) {
		return service.create(sale);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Verkauf stornieren: Bestand steigt wieder")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
