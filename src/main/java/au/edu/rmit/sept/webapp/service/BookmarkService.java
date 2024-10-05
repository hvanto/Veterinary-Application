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
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;

@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> findByUser(User user) {
        return bookmarkRepository.findByUser(user);
    }

    public Page<Bookmark> findByUser(User user, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return bookmarkRepository.findByUser(user, pageable);
    }

    public Bookmark addBookmark(User user, Article article) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndArticle(user, article);

        if (existingBookmark.isEmpty()) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setArticle(article);
            return bookmarkRepository.save(bookmark);
        } else {
            // Optional: Log or handle duplicate case
            return existingBookmark.get();
        }
    }

    public Boolean removeBookmark(User user, Article article) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndArticle(user, article);
        bookmark.ifPresent(bookmarkRepository::delete);
        return bookmark.isPresent();
    }
}
