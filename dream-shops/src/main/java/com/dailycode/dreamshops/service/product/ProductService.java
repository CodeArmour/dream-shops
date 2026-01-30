package com.dailycode.dreamshops.service.product;

import com.dailycode.dreamshops.dto.ImageDto;
import com.dailycode.dreamshops.dto.ProductDto;
import com.dailycode.dreamshops.excepion.ResourceNotFoundException;
import com.dailycode.dreamshops.helper.PatchResult;
import com.dailycode.dreamshops.model.Category;
import com.dailycode.dreamshops.model.Image;
import com.dailycode.dreamshops.model.Product;
import com.dailycode.dreamshops.repository.CategoryRepository;
import com.dailycode.dreamshops.repository.ImageRepository;
import com.dailycode.dreamshops.repository.ProductRepository;
import com.dailycode.dreamshops.request.AddProductRequest;
import com.dailycode.dreamshops.request.PatchProductRequest;
import com.dailycode.dreamshops.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory =  new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(
                createProduct(request, category)
        );
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        productRepository.delete(product); // will not touch category
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }


    @Override
    public PatchResult updatePatchProduct(PatchProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existing -> {
                    List<String> updatedFields = new ArrayList<>();

                    if (request.getName() != null) {
                        existing.setName(request.getName());
                        updatedFields.add("name");
                    }
                    if (request.getBrand() != null) {
                        existing.setBrand(request.getBrand());
                        updatedFields.add("brand");
                    }
                    if (request.getPrice() != null) {
                        existing.setPrice(request.getPrice());
                        updatedFields.add("price");
                    }
                    if (request.getInventory() != null) {
                        existing.setInventory(request.getInventory());
                        updatedFields.add("inventory");
                    }
                    if (request.getDescription() != null) {
                        existing.setDescription(request.getDescription());
                        updatedFields.add("description");
                    }

                    if (request.getCategory() != null && request.getCategory().getName() != null) {
                        Category category = categoryRepository.findByName(request.getCategory().getName());
                        if (category == null) {
                            throw new ResourceNotFoundException("Category not found");
                        }
                        existing.setCategory(category);
                        updatedFields.add("category");
                    }

                    productRepository.save(existing);
                    return new PatchResult(existing, updatedFields);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrands(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = mapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> mapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
