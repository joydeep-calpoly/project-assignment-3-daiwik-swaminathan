package parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

final class NewsParser implements Parser {

    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class NewsResponse {
        private final String status;
        private final int totalResults;
        private final List<Article> articles;

        /**
         * Constructor for NewsResponse.
         * 
         * @param status       the status of the news response
         * @param totalResults the total number of articles in the response
         * @param articles     the list of articles parsed from the JSON
         */
        NewsResponse(
                @JsonProperty("status") String status,
                @JsonProperty("totalResults") int totalResults,
                @JsonProperty("articles") List<Article> articles) {
            this.status = status;
            this.totalResults = totalResults;
            this.articles = articles;
        }

        /**
         * Retrieves the list of articles from the JSON response.
         * 
         * @return a list of articles
         */
        public List<Article> getArticles() {
            return articles;
        }
    }

    /**
     * Parses JSON content to extract articles and validate their fields.
     * 
     * @param jsonContent the JSON content containing the articles
     * @param logger      a logger to log warnings for missing or invalid fields
     * @return a list of valid articles, excluding any articles with missing or invalid fields
     * @throws Exception if there is an issue parsing the JSON
     */
    public List<Article> parse(String jsonContent, Logger logger) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NewsResponse parsedData = mapper.readValue(jsonContent, NewsResponse.class);

        List<Article> validArticles = new ArrayList<>();
        for (Article article : parsedData.getArticles()) {
            try {
                if (article.getTitle() == null) {
                    throw new Exception("Missing or invalid field: title");
                }
                if (article.getDescription() == null) {
                    throw new Exception("Missing or invalid field: description");
                }
                if (article.getPublishedAt() == null) {
                    throw new Exception("Missing or invalid field: publishedAt");
                }
                if (article.getUrl() == null) {
                    throw new Exception("Missing or invalid field: url");
                }

                validArticles.add(article);

            } catch (Exception e) {
                logger.warning("Error parsing article: " + e.getMessage());
            }
        }

        return validArticles;
    }

    /**
     * Visit method for double dispatch based on source and format.
     * 
     * @param sourceFormat the source and format details
     * @param logger      a logger to log warnings for missing or invalid fields
     * @return a list of valid articles
     * @throws Exception if there is an issue parsing the JSON
     */
    public List<Article> visit(SourceFormat sourceFormat, String jsonContent, Logger logger) throws Exception {
        if (sourceFormat.getFormat() != SourceFormat.Format.NEWSAPI) {
            throw new IllegalArgumentException("NewsParser can only handle NEWSAPI format");
        }

        // String jsonContent = "";

        // if (sourceFormat.getSource() == SourceFormat.Source.FILE) {
        //     jsonContent = Files.readString(Paths.get(sourceFormat.getSource()));
        // }
        // else {
        //     StringBuilder response = new StringBuilder();
        //     try {
        //         URL url = new URL(apiUrl);
        //         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //         connection.setRequestMethod("GET");
        //         connection.setRequestProperty("Accept", "application/json");

        //         // Check if the response code is HTTP_OK
        //         if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        //             try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        //                 String inputLine;
        //                 while ((inputLine = in.readLine()) != null) {
        //                     response.append(inputLine);
        //                 }
        //             }
        //         } else {
        //             logger.warning("Failed to fetch articles: HTTP error code " + connection.getResponseCode());
        //         }
        //     } catch (Exception e) {
        //         logger.warning("Error fetching articles from API: " + e.getMessage());
        //         return null;
        //     }
        //     return response.toString();
        // }

        return parse(jsonContent, logger);
    }
}
