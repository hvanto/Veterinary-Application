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

/**
 * Service class that manages bookmark-related operations.
 * It handles adding, removing, and fetching bookmarks for users,
 * as well as managing the persistence of articles.
 */
@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Fetches all bookmarks for a given user.
     *
     * @param user The user whose bookmarks are to be retrieved.
     * @return A list of bookmarks associated with the user.
     */
    public List<Bookmark> findByUser(User user) {
        return bookmarkRepository.findByUser(user);
    }

    /**
     * Fetches a paginated list of bookmarks for a given user.
     * Each page contains up to 10 bookmarks.
     *
     * @param user The user whose bookmarks are to be retrieved.
     * @param page The page number (starting from 0).
     * @return A page containing up to 10 bookmarks, or an empty page in case of an error.
     * @throws IllegalArgumentException if the page number is negative.
     */
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

    /**
     * Adds a bookmark for the specified user and article.
     * If the article does not already exist in the database, it is saved first.
     *
     * @param user The user adding the bookmark.
     * @param article The article to be bookmarked.
     * @return The created bookmark object.
     */
    public Bookmark addBookmark(User user, Article article) {
        // add artile to database if doesn't exists
        if (articleRepository.findByLink(article.getLink()).isEmpty()) {
            articleRepository.save(article);
        }
        Bookmark bookmark = new Bookmark(user, article);
        return bookmarkRepository.save(bookmark);
    }
    
    /**
     * Removes a bookmark for the specified user and article.
     * If the bookmark exists, it is deleted.
     *
     * @param user The user removing the bookmark.
     * @param article The article to be unbookmarked.
     * @return true if the bookmark existed and was removed, false otherwise.
     */
    public Boolean removeBookmark(User user, Article article) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserAndArticle(user, article);
        bookmark.ifPresent(bookmarkRepository::delete);
        return bookmark.isPresent();
    }
}
