package kz.kazgeowarning.newservice.service;

import kz.kazgeowarning.newservice.entity.News;
import kz.kazgeowarning.newservice.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }
    public ResponseEntity<News> updateNews(Long id, News news) {
        Optional<News> existingNews = getNewsById(id);

        if (existingNews.isPresent()) {
            News newsToUpdate = existingNews.get();
            newsToUpdate.setTitle(news.getTitle());
            newsToUpdate.setSubtitle(news.getSubtitle());
            newsToUpdate.setText(news.getText());
            newsToUpdate.setPublicationDate(news.getPublicationDate());
            newsToUpdate.setCategory(news.getCategory());

            News savedNews = saveNews(newsToUpdate);

            return ResponseEntity.ok(savedNews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}