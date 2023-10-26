package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.ICategoryRepository;
import seaFood.PTseafood.utils.SlugUtil;

import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    public Category getCategoryById(Long id)
    {
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not exist with id:"+id));
    }
    public Category save(Category category)
    {
        return categoryRepository.save(category);
    }

    public Category addCategory(Category category)
    {
        String slug = SlugUtil.createSlug(category.getName());
        category.setParentId(0L);
        category.setSlug(slug);
        return save(category);
    }
    public void deleteCategory(Long id)
    {
        categoryRepository.deleteById(id);
    }
    public Category updateCategory(Category category)
    {
        String slug = SlugUtil.createSlug(category.getName());
        category.setSlug(slug);
        return save(category);
    }

}
