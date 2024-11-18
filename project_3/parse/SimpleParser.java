package parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleParser implements Parser {

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
        List<Article> validArticles = new ArrayList<>();

        try {
            // Find out if we have a array of objects or single object
            // This is being done since simple.txt has only one object
            // but it should be able to handle multiple articles too.
            JsonNode rootNode = mapper.readTree(jsonContent);

            // Process each node as array of articles or a single article
            Iterator<JsonNode> nodes = rootNode.isArray() ? rootNode.elements() : List.of(rootNode).iterator();
            
            while (nodes.hasNext()) {
                JsonNode node = nodes.next();
                try {
                    Article article = mapper.treeToValue(node, Article.class);
                    
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

        } catch (Exception e) {
            logger.warning("Error parsing content: " + e.getMessage());
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
        if (sourceFormat.getFormat() != SourceFormat.Format.SIMPLE) {
            throw new IllegalArgumentException("SimpleParser cannot handle simple format");
        }

        if (sourceFormat.getSource() != SourceFormat.Source.FILE) {
            throw new IllegalArgumentException("SimpleParser cannot handle url source");
        }

        return parse(jsonContent, logger);
    }

}
