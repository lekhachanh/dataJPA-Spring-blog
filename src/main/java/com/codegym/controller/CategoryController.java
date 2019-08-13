package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import com.codegym.service.BlogService;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public ResponseEntity<Page<Category>> showCategoryForm(@RequestParam ("s") Optional <String> s, Pageable pageable){
        Page<Category> categories = categoryService.findAll(pageable);

        if (s.isPresent()){
            categories = categoryService.findAllByNameContaining(s.get(), pageable);
        }else {
            categories = categoryService.findAll(pageable);
        }

        if (categories == null){
            return new ResponseEntity<Page<Category>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Page<Category>>(categories, HttpStatus.OK);
    }
    @RequestMapping(value = "/category/view-category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id){
        System.out.println("fetch Category by id"+id);
        Category category = categoryService.findById(id);
        if (category == null){
            System.out.println("Category with id" + id + " not found");
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Category>(HttpStatus.OK);
    }


//
//    @GetMapping("/create")
//    public ModelAndView showCreateForm(){
//        ModelAndView modelAndView = new ModelAndView("/category/create");
//        modelAndView.addObject("category", new Category());
//        return modelAndView;
//    }
//
//    @PostMapping("/save")
//    public ModelAndView saveCategory(@ModelAttribute("category") Category category) {
//        categoryService.save(category);
//
//        ModelAndView modelAndView = new ModelAndView("/category/create");
//        modelAndView.addObject("category", new Category());
//        modelAndView.addObject("message", "created successfully");
//        return modelAndView;
//    }
//
//    @GetMapping("/edit/{id}")
//    public ModelAndView editCategoryForm(@PathVariable Long id){
//        Category category = categoryService.findById(id);
//        if (category != null) {
//            ModelAndView modelAndView = new ModelAndView("/category/edit");
//            modelAndView.addObject("category", category );
//            return modelAndView;
//        }else {
//            ModelAndView modelAndView = new ModelAndView("/error-404");
//            return modelAndView;
//        }
//    }
//
//    @PostMapping("/update")
//    public ModelAndView updateCategory(@ModelAttribute("category") Category category) {
//        categoryService.save(category);
//        ModelAndView modelAndView = new ModelAndView("/category/edit");
//        modelAndView.addObject("category", category);
//        modelAndView.addObject("message", "category updated successfully");
//        return modelAndView;
//    }
//
//    @GetMapping("/delete/{id}")
//    public ModelAndView deleteCategoryForm(@PathVariable Long id) {
//        Category category = categoryService.findById(id);
//        if (category != null) {
//            ModelAndView modelAndView = new ModelAndView("/category/delete");
//            modelAndView.addObject("category", category);
//            modelAndView.addObject("message", "delete successfully");
//            return modelAndView;
//
//        }else {
//            ModelAndView modelAndView = new ModelAndView("/error-404");
//            return modelAndView;
//        }
//    }
//
//    @PostMapping("/remove")
//    public ModelAndView deleteCategory(@ModelAttribute ("category") Category category){
//        categoryService.remove(category.getId());
//        ModelAndView modelAndView = new ModelAndView("/category/delete");
//        modelAndView.addObject("category", category);
//        modelAndView.addObject("message", "delete success");
//        return modelAndView;
//    }
//
//    @GetMapping("/view/{id}")
//    public ModelAndView viewCategory(@PathVariable("id") Long id){
//        Category category = categoryService.findById(id);
//        if(category == null){
//            return new ModelAndView("/error-404");
//        }
//
//        Iterable<Blog> blogs = blogService.findAllByCategory(category);
//
//        ModelAndView modelAndView = new ModelAndView("/category/view");
//        modelAndView.addObject("category", category);
//        modelAndView.addObject("blogs", blogs);
//        return modelAndView;
//    }

}
