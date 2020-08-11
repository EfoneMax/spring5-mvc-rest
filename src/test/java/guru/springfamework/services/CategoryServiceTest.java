package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CategoryMapper;
import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {
    public static final Category category = Category.builder().id(1L).name("One").build();
    public static final Category category2 = Category.builder().id(2L).name("Two").build();

    @Mock
    CategoryRepository repository;

    CategoryService service;

    @Before
    public void setUp() {
        service = new CategoryServiceImpl(repository, CategoryMapper.INSTANCE);
    }

    @Test
    public void getAllCategories() {
        List<Category> categories = Arrays.asList(category, category2);
        when(repository.findAll()).thenReturn(categories);

        List<CategoryDTO> allCategories = service.getAllCategories();
        assertEquals(allCategories.size(), 2);
    }

    @Test
    public void getCategoryByName() {
        when(repository.findByName(any())).thenReturn(category);

        CategoryDTO categoryOne = service.getCategoryByName("One");

        assertEquals(categoryOne.getId(), category.getId());
        assertEquals(categoryOne.getName(), category.getName());
    }
}