package com.shop.api;

import com.shop.entitty.Import;
import com.shop.entitty.ImportDetail;
import com.shop.entitty.Product;
import com.shop.repository.ImportDetailRepository;
import com.shop.repository.ImportRepository;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("api/import")
public class ImportApi {
    @Autowired
    ImportRepository importRepository;
    @Autowired
    ImportDetailRepository importDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Import>> getAllImport() {
        return ResponseEntity.ok(importRepository.findAll());
    }

    @GetMapping("/getImportById/{id}")
    public ResponseEntity<Import> getImportById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(importRepository.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Import> post(@RequestBody Import imports, @RequestBody List<ImportDetail> importDetailList) {
        try {
            importRepository.save(imports);
            Long importId = imports.getId();
            if(!importDetailList.isEmpty()) {
                Map<Long, Integer> mapQuantityProduct = new HashMap<>();
                for (ImportDetail importDetail : importDetailList) {
                    importDetail.getAnImport().setId(importId);
                    mapQuantityProduct.put(importDetail.getProduct().getProductId(), importDetail.getQuantityImport());
                    imports.setTotalAmount(imports.getTotalAmount() + (importDetail.getPriceImport() * importDetail.getQuantityImport()));
                }
                importRepository.save(imports);
                importDetailRepository.saveAll(importDetailList);

                List<Product> productList = productRepository.findAllById(mapQuantityProduct.keySet());
                for (Product product : productList) {
                    product.setQuantity(product.getQuantity() + mapQuantityProduct.get(product.getProductId()));
                }
                productRepository.saveAll(productList);
            }
            return ResponseEntity.ok(imports);
        }catch (Exception ex) {
            System.out.println(ex.getMessage() + ex.getCause());
            return ResponseEntity.badRequest().build();
        }
    }

}
