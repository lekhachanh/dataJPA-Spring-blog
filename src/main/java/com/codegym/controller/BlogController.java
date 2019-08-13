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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Page<Category> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @RequestMapping(value = "/blog/list", method = RequestMethod.GET)
    public ResponseEntity<Page<Blog>> showBlogForm(@RequestParam("s") Optional<String> s, Pageable pageable) {
        Page<Blog> blogs = blogService.findAll(pageable);
        if (s.isPresent()) {
            blogs = blogService.findAllByAuthorContaining(s.get(), pageable);
        } else {
            blogs = blogService.findAll(pageable);
        }
        if (blogs == null){
            return new ResponseEntity<Page<Blog>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Page<Blog>>(blogs, HttpStatus.OK);
    }

    @RequestMapping(value = "/blog/view-article/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody//lấy thông tin đối tượng Blog trong @RequestBody đẩy
                 //vào phương thức thực thi của web service.
    public ResponseEntity<Blog> getArticle(@PathVariable("id") Long id){
        System.out.println("fetch article by id"+id);
        Blog blog = blogService.findById(id);
        if (blog == null){
            System.out.println("Article with id" + id + " not found");
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }
//
//    @GetMapping("/create")
//    public ModelAndView showCreateForm() {
//        ModelAndView modelAndView = new ModelAndView("/blog/create");
//        modelAndView.addObject("blog", new Blog());
//        return modelAndView;
//    }
//
//    @PostMapping("/save")
//    public ModelAndView saveBlog(@ModelAttribute("blog") Blog blog) {
//        blogService.save(blog);
//
//        ModelAndView modelAndView = new ModelAndView("/blog/create");
//        modelAndView.addObject("blog", new Blog());
//        modelAndView.addObject("message", "created successfully");
//        return modelAndView;
//    }
//
//    @GetMapping("/edit/{id}")
//    public ModelAndView editBlogForm(@PathVariable Long id) {
//        Blog blog = blogService.findById(id);
//        if (blog != null) {
//            ModelAndView modelAndView = new ModelAndView("/blog/edit");
//            modelAndView.addObject("blog", blog);
//            return modelAndView;
//        } else {
//            ModelAndView modelAndView = new ModelAndView("/error-404");
//            return modelAndView;
//        }
//    }
//
//    @PostMapping("/update")
//    public ModelAndView updateBlog(@ModelAttribute("blog") Blog blog) {
//        blogService.save(blog);
//        ModelAndView modelAndView = new ModelAndView("/blog/edit");
//        modelAndView.addObject("blog", blog);
//        modelAndView.addObject("message", "blog updated successfully");
//        return modelAndView;
//    }
//
//    @GetMapping("/delete/{id}")
//    public ModelAndView deleteBlogForm(@PathVariable Long id) {
//        Blog blog = blogService.findById(id);
//        if (blog != null) {
//            ModelAndView modelAndView = new ModelAndView("/blog/delete");
//            modelAndView.addObject("blog", blog);
//            modelAndView.addObject("message", "delete successfully");
//            return modelAndView;
//
//        } else {
//            ModelAndView modelAndView = new ModelAndView("/error-404");
//            return modelAndView;
//        }
//    }
//
//    @PostMapping("/remove")
//    public ModelAndView deleteBlog(@ModelAttribute("blog") Blog blog) {
//        blogService.remove(blog.getId());
//        ModelAndView modelAndView = new ModelAndView("/blog/delete");
//        modelAndView.addObject("blog", blog);
//        modelAndView.addObject("message", "delete success");
//        return modelAndView;
//    }
}
