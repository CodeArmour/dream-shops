package com.dailycode.dreamshops.service.category;

import com.dailycode.dreamshops.excepion.AlreadyExistsException;
import com.dailycode.dreamshops.excepion.ResourceNotFoundException;
import com.dailycode.dreamshops.model.Category;
import com.dailycode.dreamshops.repository.CategoryRepository;
import com.dailycode.dreamshops.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName() + "already exists!"));
    }



    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(
                        ()-> new ResourceNotFoundException("Category not found!")
                );
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        if (productRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Cannot delete category because it still has products.");
        }
        categoryRepository.delete(category);
    }
}
