package com.shop.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop.entitty.Category;
import com.shop.entitty.Product;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/products")
public class ProductApi {
	@Autowired
	ProductRepository repo;
	
	@Autowired
	CategoryRepository cRepo;
	
	@GetMapping
	public ResponseEntity<List<Product>> getAll() {
		return ResponseEntity.ok(repo.findByStatusTrue());
	}

	@GetMapping("/quantity")
	public ResponseEntity<List<Product>> getAllQuantityGreaterThanZero() {
		return ResponseEntity.ok(repo.findAllProducts());
	}

	@GetMapping("/admin")
	public ResponseEntity<List<Product>> getAllAdmin() {
		return ResponseEntity.ok(repo.findAll());
	}

	@GetMapping("bestseller")
	public ResponseEntity<List<Product>> getBestSeller() {
		return ResponseEntity.ok(repo.findProductGreaterThanZeroSoldDesc());
	}
	
	@GetMapping("bestseller-admin")
	public ResponseEntity<List<Product>> getBestSellerAdmin() {
		return ResponseEntity.ok(repo.findTop10ByOrderBySoldDesc());
	}
	
	@GetMapping("latest")
	public ResponseEntity<List<Product>> getLasted() {
		return ResponseEntity.ok(repo.findProductGreaterThanZeroEnteredDateDesc());
	}
	
	@GetMapping("rated")
	public ResponseEntity<List<Product>> getRated() {
		return ResponseEntity.ok(repo.findProductRated());
	}
	
	@GetMapping("suggest/{categoryId}/{productId}")
	public ResponseEntity<List<Product>> suggest(@PathVariable("categoryId") Long categoryId, @PathVariable("productId") Long productId) {
		return ResponseEntity.ok(repo.findProductSuggest(categoryId, productId, categoryId, categoryId));
	}
	
	@GetMapping("category/{id}")
	public ResponseEntity<List<Product>> getByCategory(@PathVariable("id") Long id) {
		if(!cRepo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}		
		Category c = cRepo.findById(id).get();
		return ResponseEntity.ok(repo.findByCategoryAndStatusTrueAndQuantityGreaterThan(c, 0));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}		
		return ResponseEntity.ok(repo.findById(id).get());
	}
	
	@PostMapping
	public ResponseEntity<Product> post(@RequestBody Product product) {
		if(repo.existsById(product.getProductId())) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(repo.save(product));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Product> put(@PathVariable("id") Long id, @RequestBody Product product) {
		if(!id.equals(product.getProductId())) {
			return ResponseEntity.badRequest().build();
		}
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.save(product));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Product p = repo.findById(id).get();
		p.setStatus(false);
		// repo.save(p);
		repo.deleteById(id);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("set-status-hide/{id}")
	public ResponseEntity<Void> setStatusHide(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Product p = repo.findById(id).get();
		p.setStatus(false);
		repo.save(p);
		// repo.deleteById(id);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("set-status-show/{id}")
	public ResponseEntity<Void> setStatusShow(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Product p = repo.findById(id).get();
		p.setStatus(true);
		repo.save(p);
		// repo.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/priceRange")
	public ResponseEntity<List<Product>> getByPriceRange(@RequestParam Double min, Double max) {
		return ResponseEntity.ok(repo.findByPriceBetweenAndStatusTrue(min,max));
	}
}
