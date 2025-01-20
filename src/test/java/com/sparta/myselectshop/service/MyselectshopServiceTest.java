package com.sparta.myselectshop.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MyselectshopServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @Transactional
    // @Rollback(value = false)
    @DisplayName("createProduct - Product를 DB에 저장하고 올바른 결과 반환")
    void createProduct_integrationTest() {

        // Given: 요청 데이터 준비
        ProductRequestDto requestDto = new ProductRequestDto("테스트","이미지 경로","링크 경로",1000);

        // When: 서비스 메소드 호출
        ProductResponseDto responseDto = productService.createProduct(requestDto);

        // Then: 결과 확인
        Product savedProduct = productRepository.findAll().get(0);
        assertEquals("테스트", savedProduct.getTitle());
        assertEquals(1000, savedProduct.getLprice());
    }



}
