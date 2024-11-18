package parse;

import java.util.List;
import java.util.logging.Logger;

interface Parser {
    /**
     * Parses JSON content to extract articles and validate their fields.
     * 
     * @param jsonContent the JSON content containing the articles
     * @param logger      a logger to log warnings for missing or invalid fields
     * @return a list of valid articles, excluding any articles with missing or invalid fields
     * @throws Exception if there is an issue parsing the JSON
     */
    List<Article> parse(String content, Logger logger) throws Exception;

    /**
     * Visit method for double dispatch based on source and format.
     * 
     * @param sourceFormat the source and format details
     * @param logger      a logger to log warnings for missing or invalid fields
     * @return a list of valid articles
     * @throws Exception if there is an issue parsing the JSON
     */
    List<Article> visit(SourceFormat sourceFormat, String jsonContent, Logger logger) throws Exception;
}
