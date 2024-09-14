package au.edu.rmit.sept.webapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import au.edu.rmit.sept.webapp.model.Article;
import au.edu.rmit.sept.webapp.model.Bookmark;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.BookmarkRepository;

public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    public void addBookmark(User user, Article article) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setArticle(article);
        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(User user, Article article) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndArticle(user, article);
        bookmark.ifPresent(bookmarkRepository::delete);
    }
}
