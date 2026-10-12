package laptopshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laptopshop.entity.Customer;
import laptopshop.entity.Sale;
import laptopshop.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Kunden")
public class CustomerController {

	private final CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Alle Kunden")
	public List<Customer> getAll() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Einen Kunden per ID")
	public Customer get(@PathVariable Long id) {
		return service.get(id);
	}

	@GetMapping("/{id}/sales")
	@Operation(summary = "Alle Käufe eines Kunden")
	public List<Sale> sales(@PathVariable Long id) {
		return service.getSales(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Kunde anlegen")
	public Customer create(@Valid @RequestBody Customer customer) {
		return service.create(customer);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Kunde ändern")
	public Customer update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
		return service.update(id, customer);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Kunde löschen (nur ohne Käufe)")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
