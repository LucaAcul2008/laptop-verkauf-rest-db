package laptopshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Ein Verkauf. Verbindet Kunde und Laptop (n:m wird über diese Tabelle aufgelöst).
 */
@Entity
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "laptop_id")
	private Laptop laptop;

	@Min(1)
	private int quantity;

	/** Gesamtpreis = Preis des Laptops zum Verkaufszeitpunkt x Menge (wird vom Service berechnet). */
	@Column(precision = 12, scale = 2)
	private BigDecimal totalPrice;

	private LocalDate saleDate;

	public Sale() {
	}

	public Sale(Customer customer, Laptop laptop, int quantity, BigDecimal totalPrice, LocalDate saleDate) {
		this.customer = customer;
		this.laptop = laptop;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.saleDate = saleDate;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	public Laptop getLaptop() { return laptop; }
	public void setLaptop(Laptop laptop) { this.laptop = laptop; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public BigDecimal getTotalPrice() { return totalPrice; }
	public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
	public LocalDate getSaleDate() { return saleDate; }
	public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
}
