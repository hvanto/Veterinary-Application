package au.edu.rmit.sept.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.ArticleRepository;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;

@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<Bookmark> findByUser(User user) {
        return bookmarkRepository.findByUser(user);
    }

    public Page<Bookmark> findByUser(User user, int page) {
        try {
            if (page < 0) {
                throw new IllegalArgumentException("page cannot be negative");
            }
            // Show 10 articles per page
            Pageable pageable = PageRequest.of(page, 10);
            return bookmarkRepository.findByUser(user, pageable);
    
        } catch (Exception e) {
            System.err.println("An error occurred while fetching paginated articles: " + e.getMessage());
            // Return an empty page
            return Page.empty();
        }
    }
    
    public Bookmark addBookmark(User user, Article article) {
        // add artile to database if doesn't exists
        if (articleRepository.findByLink(article.getLink()).isEmpty()) {
            articleRepository.save(article);
        }
        Bookmark bookmark = new Bookmark(user, article);
        return bookmarkRepository.save(bookmark);
    }
    
    public Boolean removeBookmark(User user, Article article) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndArticle(user, article);
        bookmark.ifPresent(bookmarkRepository::delete);
        return bookmark.isPresent();
    }
}
