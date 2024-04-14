package kz.kazgeowarning.newservice.service;

import kz.kazgeowarning.newservice.entity.News;
import kz.kazgeowarning.newservice.entity.NewsFilterDTO;
import kz.kazgeowarning.newservice.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(News::getPublicationDate).reversed())
                .collect(Collectors.toList());
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
            newsToUpdate.setPublicationDate(new Date());
            newsToUpdate.setImageUrl(news.getImageUrl());

            News savedNews = saveNews(newsToUpdate);

            return ResponseEntity.ok(savedNews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public List<News> searchByDate(NewsFilterDTO newsDTO) {
        List<News> allNews = getAllNews();
        List<News> allNewsExceptRecentTwo = allNews.size() > 2 ? allNews.subList(0, allNews.size() - 2) : allNews;

        if (newsDTO == null || newsDTO.getFrom() == null || newsDTO.getTo() == null) {
            return allNewsExceptRecentTwo;
        }

        List<News> filteredNews = allNewsExceptRecentTwo.stream()
                .filter(news -> news.getPublicationDate().after(newsDTO.getFrom()) && news.getPublicationDate().before(newsDTO.getTo()))
                .collect(Collectors.toList());

        return filteredNews;
    }

    public List<News> getTwoLastNews() {
        List<News> allNews = getAllNews();
        List<News> lastTwoNews = allNews.size() > 1 ? allNews.subList(0, 2) : allNews;

        return lastTwoNews;
    }
}
