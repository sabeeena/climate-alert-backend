package kz.kazgeowarning.newservice.controller;

import kz.kazgeowarning.newservice.entity.News;
import kz.kazgeowarning.newservice.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public Optional<News> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PostMapping
    public News saveNews(@RequestBody News news) {
        return newsService.saveNews(news);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        return newsService.updateNews(id,news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
    }
}