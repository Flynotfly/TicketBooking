package com.example.services;

import com.example.entities.Category;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CategoryService {
    @PersistenceContext(unitName = "bookingPU")
    private EntityManager em;

    public void addCategory(Category category) {
        em.persist(category);
    }

    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public Category getCategoryById(Long id) {
        return em.find(Category.class, id);
    }

    public void updateCategory(Category category) {
        em.merge(category);
    }

    public void deleteCategory(Long id) {
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
        }
    }
}
