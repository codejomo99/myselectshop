package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 생성
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productRepository.save(new Product(productRequestDto));

        return new ProductResponseDto(product);
    }

    // 조회
    public List<ProductResponseDto> readProduct() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::new).toList();
    }

}
