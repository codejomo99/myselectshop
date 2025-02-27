package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.excepiton.ProductNotFoundException;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;
    private final MessageSource messageSource;

    public static final int MIN_MY_PRICE = 100;

    // 생성
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        Product product = productRepository.save(new Product(productRequestDto,user));


        return new ProductResponseDto(product);
    }

    // 조회
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> readProduct(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        UserRoleEnum role = user.getRole();

        Page<Product> productList;

        if(role == UserRoleEnum.USER){
            productList = productRepository.findAllByUser(user,pageable);
        }else{
            productList = productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::new);
    }


    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myPrice = requestDto.getMyprice();

        if (myPrice < MIN_MY_PRICE) {
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "below.min.my.price",
                            new Integer[]{MIN_MY_PRICE},
                            "Wrong Price",
                            Locale.getDefault()
                    )
            );
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
                new ProductNotFoundException(messageSource.getMessage(
                        "not.found.product",
                        null,
                        "Not Found Product",
                        Locale.getDefault()
                )));

        product.updateByItemDto(itemDto);
    }

    public void addFolder(Long productId, Long folderId, User user) {

        // 상품이 존재하는지 확인
        Product product = productRepository.findById(productId).orElseThrow(()-> new NullPointerException("해당 상품이 존재하지 않습니다."));
        // 폴더가 존재하는지 확인
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NullPointerException("해당 폴더가 존재하지 않습니다."));

        // 폴더와 상품의 유저가 가져온 유저가 맞는지
        if(!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("회원님의 관심 상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        // 하나의 상품에 중복된 폴더가 있는지 확인
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product,folder);

        if(overlapFolder.isPresent()){
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        productFolderRepository.save(new ProductFolder(product,folder));

    }
    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc, User user) {

        Sort.Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Product> products = productRepository.findAllByUserAndProductFolderList_FolderId(user,folderId,pageable);
        Page<ProductResponseDto> responseDtos = products.map(ProductResponseDto::new);
        return responseDtos;
    }
}
