package parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
final class Article {
    private final String title;
    private final String description;
    private final String publishedAt;
    private final String url;

    /**
     * Constructor for Article.
     * 
     * @param title       the title of the article
     * @param description a short description or summary of the article
     * @param publishedAt the timestamp of the article's publication
     * @param url         the URL of the article
     */
    Article(@JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("publishedAt") String publishedAt,
            @JsonProperty("url") String url) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    /**
     * Gets the title of the article.
     * Needed by test file.
     * 
     * @return the title of the article
     */
    String getTitle() {
        return title;
    }

    /**
     * Gets the description of the article.
     * Needed by test file.
     * 
     * @return the description of the article
     */
    String getDescription() {
        return description;
    }

    /**
     * Gets the publication date and time of the article.
     * Needed by test file.
     * 
     * @return the publication date and time of the article
     */
    String getPublishedAt() {
        return publishedAt;
    }

    /**
     * Gets the URL of the article.
     * Needed by test file.
     * 
     * @return the URL of the article
     */
    String getUrl() {
        return url;
    }

    /**
     * Converts the article to a string representation.
     * Used by main method.
     * 
     * @return a string representation of the article
     */
    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Published At: " + publishedAt + "\n" +
                "URL: " + url + "\n\n";
    }
}