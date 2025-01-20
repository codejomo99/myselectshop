package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMypriceRequestDto {
    private int myprice;

    public ProductMypriceRequestDto(int myprice) {
        this.myprice = myprice;
    }
}
