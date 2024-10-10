package au.edu.rmit.sept.webapp.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing an article in the system.
 * An article has attributes such as title, link, description, author, published date, and an image URL.
 */
@Entity
@Table(name = "articles")
public class Article {
    
    /**
     * Unique identifier for the article.
     * This value is auto-generated using the identity generation strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the article.
     */
    private String title;

    /**
     * URL link to the article.
     */
    private String link;

    /**
     * A description or excerpt of the article. This is stored as MEDIUMTEXT in the database.
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    /**
     * Author of the article.
     */
    private String author;

    /**
     * Date when the article was published.
     */
    private Date publishedDate;

    /**
     * URL of the article's image or thumbnail.
     */
    private String imageUrl;

    /**
     * Default constructor for the Article entity.
     * Required for JPA.
     */
    public Article() {}

    /**
     * Parameterized constructor for creating an article with all attributes.
     *
     * @param title The title of the article.
     * @param link The URL link to the article.
     * @param description A brief description of the article.
     * @param author The author of the article.
     * @param publishedDate The publication date of the article.
     * @param imageUrl The URL of the article's image.
     */
    public Article(String title, String link, String description, String author, Date publishedDate, String imageUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.author = author;
        this.publishedDate = publishedDate;
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the unique identifier of the article.
     *
     * @return The ID of the article.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the link to the article.
     *
     * @return The URL link of the article.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link of the article.
     *
     * @param link The new URL link of the article.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the title of the article.
     *
     * @return The title of the article.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the article.
     *
     * @param title The new title of the article.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the article.
     *
     * @return The description of the article.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the article.
     *
     * @param description The new description of the article.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the author of the article.
     *
     * @return The author of the article.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the article.
     *
     * @param author The new author of the article.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publication date of the article.
     *
     * @return The publication date of the article.
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Sets the publication date of the article.
     *
     * @param publishedDate The new publication date of the article.
     */
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * Gets the image URL of the article.
     *
     * @return The URL of the article's image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL of the article.
     *
     * @param imageUrl The new URL of the article's image.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}