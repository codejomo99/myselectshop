package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public static final int MIN_MY_PRICE = 100;

    // 생성
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        Product product = productRepository.save(new Product(productRequestDto,user));


        return new ProductResponseDto(product);
    }

    // 조회
    public List<ProductResponseDto> readProduct(User user) {
        return productRepository.findAllByUser(user).stream()
                .map(ProductResponseDto::new).toList();
    }


    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myPrice = requestDto.getMyprice();

        if (myPrice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격 입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요");
        }

        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품을 찾을 수 없습니다.")
        );

        product.update(requestDto);

        return new ProductResponseDto(product);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다."));

        product.updateByItemDto(itemDto);
    }

    public List<ProductResponseDto> getAllProduct() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::new).toList();
    }
}
