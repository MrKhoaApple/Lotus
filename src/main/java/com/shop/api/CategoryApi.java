package com.shop.api;

import java.util.List;

import com.shop.entitty.Product;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.entitty.Category;
import com.shop.repository.CategoryRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/categories")
public class CategoryApi {
	@Autowired
	CategoryRepository repo;

	@Autowired
	ProductRepository productRepository;

	@GetMapping
	public ResponseEntity<List<Category>> getAll() {
		return ResponseEntity.ok(repo.findByStatusTrue());
	}

	@GetMapping("/admin")
	public ResponseEntity<List<Category>> getAllAdmin() {
		return ResponseEntity.ok(repo.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<Category> getById(@PathVariable("id") Long id) {
		if (!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.findById(id).get());
	}

	@PostMapping
	public ResponseEntity<Category> post(@RequestBody Category category) {
		if (repo.existsById(category.getCategoryId())) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(repo.save(category));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Category> put(@RequestBody Category category, @PathVariable("id") Long id) {
		if(!id.equals(category.getCategoryId())) {
			return ResponseEntity.badRequest().build();
		}
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.save(category));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		repo.deleteById(id);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("set-status-show/{id}")
	public ResponseEntity<Void> setStatusShow(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Category category = repo.findById(id).get();

		category.setStatus(true);
		repo.save(category);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("set-status-hide/{id}")
	public ResponseEntity<Void> setStatusHide(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		Category category = repo.findById(id).get();
		category.setStatus(false);
		repo.save(category);
		return ResponseEntity.ok().build();
	}
}
