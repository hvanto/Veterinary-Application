package au.edu.rmit.sept.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;

@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> findByUser(User user) {
        return bookmarkRepository.findByUser(user);
    };

    public void addBookmark(User user, Article article) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndArticle(user, article);
    
        if (existingBookmark.isEmpty()) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setArticle(article);
            bookmarkRepository.save(bookmark);
        } else {
            // Optional: Log or handle duplicate case
            System.out.println("Bookmark already exists for this user and article.");
        }
    }

    public void removeBookmark(User user, Article article) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndArticle(user, article);
        bookmark.ifPresent(bookmarkRepository::delete);
    }
}
