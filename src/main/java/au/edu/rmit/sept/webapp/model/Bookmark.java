package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity class representing a bookmark in the system.
 * A bookmark links a {@link User} with an {@link Article}.
 */
@Entity
@Table(name = "bookmarks")
public class Bookmark {

    /**
     * Unique identifier for the bookmark.
     * This value is auto-generated using the identity generation strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who created the bookmark.
     * This is a many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The article that is bookmarked by the user.
     * This is a many-to-one relationship with the {@link Article} entity.
     */
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    /**
     * Default constructor for the Bookmark entity.
     * Required for JPA.
     */
    public Bookmark() {
    }

    /**
     * Parameterized constructor for creating a bookmark with a user and an article.
     *
     * @param user    The user who is bookmarking the article.
     * @param article The article to be bookmarked.
     */
    public Bookmark(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    /**
     * Gets the unique identifier of the bookmark.
     *
     * @return The ID of the bookmark.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user who created the bookmark.
     *
     * @return The user associated with this bookmark.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who created the bookmark.
     *
     * @param user The user to be associated with this bookmark.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the article that is bookmarked.
     *
     * @return The article associated with this bookmark.
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Sets the article that is bookmarked.
     *
     * @param article The article to be associated with this bookmark.
     */
    public void setArticle(Article article) {
        this.article = article;
    }
}
