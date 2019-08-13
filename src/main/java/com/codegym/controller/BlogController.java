package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import com.codegym.service.BlogService;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

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

    @RequestMapping(value = "/blogList", method = RequestMethod.GET)
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

    @RequestMapping(value = "/blogList/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Blog> getArticle(@PathVariable("id") Long id){
        System.out.println("fetch article by id"+id);
        Blog blog = blogService.findById(id);
        if (blog == null){
            System.out.println("Article with id" + id + " not found");
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    @RequestMapping(value = "/blogList", method = RequestMethod.POST)
    public ResponseEntity<Void> createArticle(@RequestBody Blog blog, UriComponentsBuilder ucBuilder) {
        blogService.save(blog);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/blogList/{id}").buildAndExpand(blog.getId()).toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/blogList/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Blog> updateArticle(@PathVariable("id") Long id, @RequestBody Blog blog) {
        System.out.println("Updating Article " + id);

        Blog currentBlog = blogService.findById(id);

        if (currentBlog == null) {
            System.out.println("Article with id " + id + " not found");
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }

        currentBlog.setAuthor(blog.getAuthor());
        currentBlog.setContent(blog.getContent());
        currentBlog.setCategory(blog.getCategory());
        currentBlog.setId(blog.getId());

        blogService.save(currentBlog);
        return new ResponseEntity<Blog>(currentBlog, HttpStatus.OK);
    }

    @RequestMapping(value = "/blogList/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Blog> deleteArticle(@PathVariable("id") Long id) {
        System.out.println("Fetching & Deleting Article with id " + id);

        Blog blog = blogService.findById(id);
        if (blog == null) {
            System.out.println("Unable to delete. Article with id " + id + " not found");
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }

        blogService.remove(id);
        return new ResponseEntity<Blog>(HttpStatus.NO_CONTENT);
    }

}
