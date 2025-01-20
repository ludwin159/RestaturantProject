package com.commons;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;

    private String name;

    private LocalDateTime createAt;

    private List<ProductDTO> products = new ArrayList<>();

    public void addProduct(ProductDTO product) {
        this.products.add(product);
    }
    public void removeProduct(ProductDTO product) {
        this.products.remove(product);
        product.setCategory(null);
    }

}
