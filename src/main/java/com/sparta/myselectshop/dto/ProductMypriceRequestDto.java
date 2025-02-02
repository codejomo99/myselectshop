package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductMypriceRequestDto {
    private int myprice;

    public ProductMypriceRequestDto(int myprice) {
        this.myprice = myprice;
    }
}
