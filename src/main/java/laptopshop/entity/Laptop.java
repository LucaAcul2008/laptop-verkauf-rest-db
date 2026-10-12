package laptopshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** Ein Laptop im Sortiment mit Lagerbestand. */
@Entity
public class Laptop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String brand;

	@NotBlank
	private String model;

	private String cpu;

	@Min(1)
	private int ramGb;

	@Min(1)
	private int storageGb;

	/** Verkaufspreis pro Stück in Euro. */
	@NotNull
	@DecimalMin("0.01")
	@Column(precision = 10, scale = 2)
	private BigDecimal price;

	/** Anzahl auf Lager. */
	@Min(0)
	private int stock;

	public Laptop() {
	}

	public Laptop(String brand, String model, String cpu, int ramGb, int storageGb, BigDecimal price, int stock) {
		this.brand = brand;
		this.model = model;
		this.cpu = cpu;
		this.ramGb = ramGb;
		this.storageGb = storageGb;
		this.price = price;
		this.stock = stock;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getBrand() { return brand; }
	public void setBrand(String brand) { this.brand = brand; }
	public String getModel() { return model; }
	public void setModel(String model) { this.model = model; }
	public String getCpu() { return cpu; }
	public void setCpu(String cpu) { this.cpu = cpu; }
	public int getRamGb() { return ramGb; }
	public void setRamGb(int ramGb) { this.ramGb = ramGb; }
	public int getStorageGb() { return storageGb; }
	public void setStorageGb(int storageGb) { this.storageGb = storageGb; }
	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }
	public int getStock() { return stock; }
	public void setStock(int stock) { this.stock = stock; }
}
