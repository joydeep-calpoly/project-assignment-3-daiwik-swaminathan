package parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

final class Driver {

    /**
     * Main method to demonstrate the functionality of the NewsParser and SimpleParser. 
     * It reads articles from input JSON files, fetches articles from a news API,
     * and handles JSON files with the new simple format.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger("NewsParserLogger");
            FileHandler fileHandler = new FileHandler("newsparser.log", true); // Append to the log file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Example of processing local JSON files
            String[] jsonFiles = {
                "/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/newsapi.txt"
            };

            for (String filePath : jsonFiles) {
                String jsonContent = readFile(filePath);
                if (jsonContent != null) {
                    
                    SourceFormat fileNewsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.NEWSAPI);
                    Parser newsParser1 = new NewsParser();
                    fileNewsSource.accept(newsParser1, jsonContent, logger);
                    parseContent(jsonContent, newsParser1, logger);
                }
            }

            // Load API key from config.properties file
            String apiKey = loadApiKey("config.properties", logger);
            if (apiKey != null) {
                String apiUrl = "http://newsapi.org/v2/top-headlines?country=us&apiKey=" + apiKey;
                String apiJsonContent = fetchArticlesFromApi(apiUrl, logger);
                if (apiJsonContent != null) {
                    SourceFormat apiNewsSource = new SourceFormat(SourceFormat.Source.URL, SourceFormat.Format.NEWSAPI);
                    Parser newsParser2 = new NewsParser();
                    apiNewsSource.accept(newsParser2, apiJsonContent, logger);
                    parseContent(apiJsonContent, newsParser2, logger);
                }
            }

            // Example of parsing the new simple format
            String simpleJsonFilePath = "/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/simple.txt";
            String simpleJsonContent = readFile(simpleJsonFilePath);
            if (simpleJsonContent != null) {
                SourceFormat fileNewsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.SIMPLE);
                Parser simpleParser = new SimpleParser();
                fileNewsSource.accept(simpleParser, simpleJsonContent, logger);
                parseContent(simpleJsonContent, simpleParser, logger);
            }

            fileHandler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reads the content of a file and returns it as a string.
    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    // Parses and prints the articles
    private static void parseContent(String jsonContent, Parser parser, Logger logger) {
        try {
            List<Article> validArticles = parser.parse(jsonContent, logger);
            for (Article article : validArticles) {
                System.out.println(article);
            }
        } catch (Exception e) {
            logger.warning("Error parsing content: " + e.getMessage());
        }
    }

    // Loads API key from a properties file
    private static String loadApiKey(String configFilePath, Logger logger) {
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            Properties properties = new Properties();
            properties.load(fis);
            return properties.getProperty("news_api_key");
        } catch (Exception e) {
            logger.warning("Error loading API key from " + configFilePath + ": " + e.getMessage());
            return null;
        }
    }

    // Returns string response from given API
    private static String fetchArticlesFromApi(String apiUrl, Logger logger) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check if the response code is HTTP_OK
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                logger.warning("Failed to fetch articles: HTTP error code " + connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.warning("Error fetching articles from API: " + e.getMessage());
            return null;
        }
        return response.toString();
    }
}
