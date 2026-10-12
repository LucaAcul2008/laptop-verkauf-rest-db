package laptopshop.controller;

import laptopshop.service.ConflictException;
import laptopshop.service.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Übersetzt Fehler in saubere HTTP-Statuscodes mit JSON-Meldung. */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> notFound(NotFoundException e) {
		return Map.of("error", e.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> conflict(ConflictException e) {
		return Map.of("error", e.getMessage());
	}

	@ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> badRequest(Exception e) {
		return Map.of("error", e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> invalid(MethodArgumentNotValidException e) {
		StringBuilder sb = new StringBuilder();
		e.getBindingResult().getFieldErrors()
				.forEach(f -> sb.append(f.getField()).append(": ").append(f.getDefaultMessage()).append("; "));
		return Map.of("error", sb.toString().trim());
	}
}
