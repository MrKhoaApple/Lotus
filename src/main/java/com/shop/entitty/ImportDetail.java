package com.shop.entitty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "importDetail")
public class ImportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate expireDate;
    private int quantityImport;
    private Double priceImport;
    @ManyToOne
    @JoinColumn(name = "importId")
    private Import anImport;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
