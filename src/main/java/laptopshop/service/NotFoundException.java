package laptopshop.service;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String what, Long id) {
		super(what + " mit ID " + id + " nicht gefunden");
	}
}
