package com.dailycode.dreamshops.helper;

import com.dailycode.dreamshops.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatchResult {
    private Product product;
    private List<String> updatedFields;

}

