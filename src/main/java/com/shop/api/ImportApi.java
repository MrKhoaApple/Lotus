package com.shop.api;

import com.shop.entitty.Import;
import com.shop.entitty.Product;
import com.shop.entitty.User;
import com.shop.repository.ImportRepository;
import com.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/import")
public class ImportApi {
    @Autowired
    ImportRepository importRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Import>> getAllImport() {
        return ResponseEntity.ok(importRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Import> post(@RequestBody Import imports) {
        if(!importRepository.existsById(imports.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(importRepository.save(imports));
    }

    @PutMapping("{id}")
    public ResponseEntity<Import> put(@PathVariable("id") Long id, @RequestBody Import imports) {
        if(!id.equals(imports.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if(!importRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(importRepository.save(imports));
    }
}
