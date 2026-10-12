package laptopshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Testet die Use Cases aus der Doku. Die Testdaten kommen aus TestDataLoader. */
@SpringBootTest
@AutoConfigureMockMvc
class LaptopShopApiTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	ObjectMapper om;

	private long create(String url, String json) throws Exception {
		String body = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		return om.readTree(body).get("id").asLong();
	}

	@Test
	void testDataIsLoaded() throws Exception {
		mvc.perform(get("/api/laptops")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(4)));
		mvc.perform(get("/api/customers")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
	}

	@Test
	void filterByBrand() throws Exception {
		mvc.perform(get("/api/laptops?brand=hp")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void filterByMaxPrice() throws Exception {
		mvc.perform(get("/api/laptops?maxPrice=800")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void lowStockFilter() throws Exception {
		mvc.perform(get("/api/laptops?lowStockBelow=3")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void salesOfCustomer() throws Exception {
		mvc.perform(get("/api/customers/1/sales")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void saleReducesStockAndCancelRestoresIt() throws Exception {
		long lid = create("/api/laptops",
				"{\"brand\":\"Dell\",\"model\":\"XPS 13\",\"cpu\":\"Intel Core Ultra 5\",\"ramGb\":16,\"storageGb\":512,\"price\":2000.00,\"stock\":10}");
		long sid = create("/api/sales", "{\"quantity\":2,\"customer\":{\"id\":3},\"laptop\":{\"id\":" + lid + "}}");

		mvc.perform(get("/api/sales/" + sid)).andExpect(jsonPath("$.totalPrice").value(4000.0));
		mvc.perform(get("/api/laptops/" + lid)).andExpect(jsonPath("$.stock").value(8));

		mvc.perform(delete("/api/sales/" + sid)).andExpect(status().isNoContent());
		mvc.perform(get("/api/laptops/" + lid)).andExpect(jsonPath("$.stock").value(10));
	}

	@Test
	void saleWithTooLittleStockIsRejected() throws Exception {
		mvc.perform(post("/api/sales").contentType(MediaType.APPLICATION_JSON)
				.content("{\"quantity\":5,\"customer\":{\"id\":3},\"laptop\":{\"id\":4}}"))
				.andExpect(status().isConflict());
	}

	@Test
	void duplicateEmailIsRejected() throws Exception {
		mvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Doppelt\",\"email\":\"anna.huber@example.com\"}"))
				.andExpect(status().isConflict());
	}

	@Test
	void validationAndNotFound() throws Exception {
		mvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"\",\"email\":\"kaputt\"}")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/laptops/99999")).andExpect(status().isNotFound());
	}

	@Test
	void soldLaptopAndCustomerWithSalesCannotBeDeleted() throws Exception {
		mvc.perform(delete("/api/laptops/3")).andExpect(status().isConflict());
		mvc.perform(delete("/api/customers/1")).andExpect(status().isConflict());
	}

	@Test
	void openApiDocsAreAvailable() throws Exception {
		mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
	}
}
