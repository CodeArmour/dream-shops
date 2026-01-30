package com.dailycode.dreamshops.service.product;

import com.dailycode.dreamshops.dto.ProductDto;
import com.dailycode.dreamshops.helper.PatchResult;
import com.dailycode.dreamshops.model.Category;
import com.dailycode.dreamshops.model.Product;
import com.dailycode.dreamshops.request.AddProductRequest;
import com.dailycode.dreamshops.request.PatchProductRequest;
import com.dailycode.dreamshops.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    Product updateProduct(UpdateProductRequest product, Long productId);
    PatchResult updatePatchProduct(PatchProductRequest product, Long productId);

    void deleteProductById(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrands(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
