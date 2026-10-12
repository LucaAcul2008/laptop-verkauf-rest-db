package laptopshop.service;

import laptopshop.entity.Customer;
import laptopshop.entity.Sale;
import laptopshop.repository.CustomerRepository;
import laptopshop.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final SaleRepository saleRepository;

	public CustomerService(CustomerRepository customerRepository, SaleRepository saleRepository) {
		this.customerRepository = customerRepository;
		this.saleRepository = saleRepository;
	}

	public List<Customer> getAll() {
		return customerRepository.findAll();
	}

	public Customer get(Long id) {
		return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Kunde", id));
	}

	public List<Sale> getSales(Long id) {
		get(id);
		return saleRepository.findByCustomerId(id);
	}

	public Customer create(Customer customer) {
		if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
			throw new ConflictException("E-Mail-Adresse ist schon vergeben");
		}
		customer.setId(null);
		return customerRepository.save(customer);
	}

	public Customer update(Long id, Customer data) {
		Customer existing = get(id);
		if (!existing.getEmail().equalsIgnoreCase(data.getEmail())
				&& customerRepository.existsByEmailIgnoreCase(data.getEmail())) {
			throw new ConflictException("E-Mail-Adresse ist schon vergeben");
		}
		existing.setName(data.getName());
		existing.setEmail(data.getEmail());
		return customerRepository.save(existing);
	}

	public void delete(Long id) {
		Customer existing = get(id);
		if (saleRepository.existsByCustomerId(id)) {
			throw new ConflictException("Kunde hat Käufe und kann nicht gelöscht werden");
		}
		customerRepository.delete(existing);
	}
}
