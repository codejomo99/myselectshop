package com.sparta.myselectshop.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.repository.ProductRepository;
import com.sparta.myselectshop.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트","테스트 이미지","테스트 링크",10000);
        User user = new User("테스트","테스트","test.com", UserRoleEnum.USER);

        userRepository.save(user);
        productRepository.save(new Product(productRequestDto, user));
    }



    @Test
    @DisplayName("readProduct - Product를 DB에서 조회")
    void readProduct(){
        // Given
        ProductRequestDto requestDto = new ProductRequestDto("테스트","이미지 경로","링크 경로",1000);
        ProductRequestDto requestDto2 = new ProductRequestDto("테스트2","이미지 경로2","링크 경로2",2000);
        User user = userRepository.findByUsername("테스트").orElseThrow();
        productService.createProduct(requestDto,user);
        productService.createProduct(requestDto2,user);

        // When
        List<ProductResponseDto> responseDtos =  productService.readProduct(user);

        // Then
        assertEquals(3,responseDtos.size());
    }

    @Test
    @DisplayName("updateProduct - 정상적으로 myPrice를 업데이트")
    void updateProduct_success() {
        // Given: 테스트용 데이터 준비
        Product product = productRepository.findAll().get(0);
        Long productId = product.getId();
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(1000);

        // When: updateProduct 호출
        ProductResponseDto responseDto = productService.updateProduct(productId, requestDto);

        // Then: 업데이트 결과 검증
        assertNotNull(responseDto);
        assertEquals(1000, responseDto.getMyprice()); // 업데이트된 가격 확인
        assertEquals(product.getTitle(), responseDto.getTitle()); // 이름이 유지되는지 확인
    }

    @Test
    @DisplayName("updateProduct - 예외처리")
    void updateProduct_invalidPrice() {
        // Given: 테스트용 데이터 준비
        Product product = productRepository.findAll().get(0);
        Long productId = product.getId();
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(50); // 유효하지 않은 가격

        // When & Then: IllegalArgumentException 발생 확인
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(productId, requestDto)
        );

        assertEquals("유효하지 않은 관심 가격 입니다. 최소 " + 100 + "원 이상으로 설정해 주세요", exception.getMessage());
    }

    @Test
    @DisplayName("updateProduct - 존재하지 않는 상품으로 예외 발생")
    void updateProduct_productNotFound() {
        // Given: 존재하지 않는 상품 ID
        Long invalidProductId = 999L; // 존재하지 않는 ID
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(200);

        // When & Then: NullPointerException 발생 확인
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> productService.updateProduct(invalidProductId, requestDto)
        );

        assertEquals("해당 상품을 찾을 수 없습니다.", exception.getMessage());
    }


}
