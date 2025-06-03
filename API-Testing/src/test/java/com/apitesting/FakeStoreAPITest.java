package com.apitesting;

import com.apitesting.model.Product;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class FakeStoreAPITest {
    private static final String BASE_URL = "https://fakestoreapi.com";
    private Product[] products;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        // Get products once for all tests
        products = fetchProducts();
    }

    private Product[] fetchProducts() {
        Response response = given()
            .when()
            .get("/products")
            .then()
            .extract()
            .response();
        
        assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        return response.as(Product[].class);
    }

    @Test
    public void testResponseStructure() {
        assertNotNull(products, "Products array should not be null");
        assertTrue(products.length > 0, "Products array should not be empty");
        
        // Verify first product structure
        Product firstProduct = products[0];
        assertNotNull(firstProduct.getId(), "Product ID should not be null");
        assertNotNull(firstProduct.getTitle(), "Product title should not be null");
        assertNotNull(firstProduct.getPrice(), "Product price should not be null");
        assertNotNull(firstProduct.getDescription(), "Product description should not be null");
        assertNotNull(firstProduct.getCategory(), "Product category should not be null");
        assertNotNull(firstProduct.getRating(), "Product rating should not be null");
    }

    @Test
    public void testProductValidation() {
        List<Product> defectiveProducts = Arrays.stream(products)
            .filter(this::hasDefects)
            .collect(Collectors.toList());

        if (!defectiveProducts.isEmpty()) {
            System.out.println("\nDefective Products Found:");
            defectiveProducts.forEach(product -> {
                System.out.println("\nProduct ID: " + product.getId());
                printDefects(product);
            });
        }

        assertTrue(defectiveProducts.isEmpty(), "Found products with defects");
    }

    @Test
    public void testPriceRange() {
        double maxPrice = Arrays.stream(products)
            .mapToDouble(Product::getPrice)
            .max()
            .orElse(0.0);

        double minPrice = Arrays.stream(products)
            .mapToDouble(Product::getPrice)
            .min()
            .orElse(0.0);

        System.out.println("Price Range - Min: $" + minPrice + ", Max: $" + maxPrice);
        assertTrue(maxPrice > 0, "Maximum price should be greater than 0");
        assertTrue(minPrice >= 0, "Minimum price should not be negative");
    }

    @Test
    public void testRatingRange() {
        double maxRating = Arrays.stream(products)
            .map(Product::getRating)
            .mapToDouble(rating -> rating.getRate())
            .max()
            .orElse(0.0);

        System.out.println("Maximum Rating Found: " + maxRating);
        assertTrue(maxRating <= 5.0, "Rating should not exceed 5.0");
        assertTrue(maxRating > 0.0, "Maximum rating should be greater than 0");
    }

    private boolean hasDefects(Product product) {
        return isEmptyTitle(product) || 
               hasNegativePrice(product) || 
               hasInvalidRating(product);
    }

    private boolean isEmptyTitle(Product product) {
        return product.getTitle() == null || product.getTitle().trim().isEmpty();
    }

    private boolean hasNegativePrice(Product product) {
        return product.getPrice() < 0;
    }

    private boolean hasInvalidRating(Product product) {
        return product.getRating() != null && 
               (product.getRating().getRate() < 0 || product.getRating().getRate() > 5);
    }

    private void printDefects(Product product) {
        if (isEmptyTitle(product)) {
            System.out.println("- Empty or null title");
        }
        if (hasNegativePrice(product)) {
            System.out.println("- Negative price: " + product.getPrice());
        }
        if (hasInvalidRating(product)) {
            System.out.println("- Invalid rating: " + product.getRating().getRate());
        }
    }
} 