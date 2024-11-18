package parse;

import org.junit.*;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;

public final class NewsParserTest {

    private Logger logger;

    /**
     * Sets up the logger before each test case. This method is called before the execution 
     * of each test, initializing the logger used for testing purposes.
     */
    @Before
    public void setup() {
        logger = Logger.getLogger(NewsParserTest.class.getName());
    }

    /**
     * Test method to validate the parsing of a JSON file with valid articles. It checks if 
     * the correct number of articles is parsed and verifies the fields (title, description, 
     * publishedAt, and URL) for each article.
     */
    @Test
    public void testValidInput() {
        try {
            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/valid.json")));
                
            Parser np = new NewsParser();
            List<Article> articles = np.parse(jsonContent, logger); 

            assertEquals(2, articles.size());

            Article article1 = articles.get(0);
            assertEquals("Title 1", article1.getTitle());
            assertEquals("Description 1", article1.getDescription());
            assertEquals("2024-10-15T10:00:00Z", article1.getPublishedAt());
            assertEquals("https://example.com/1", article1.getUrl());

            Article article2 = articles.get(1);
            assertEquals("Title 2", article2.getTitle());
            assertEquals("Description 2", article2.getDescription());
            assertEquals("2024-10-15T11:00:00Z", article2.getPublishedAt());
            assertEquals("https://example.com/2", article2.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown during parsing: " + e.getMessage());
        }
    }

    /**
     * Test method to validate the parsing of a JSON file with some invalid articles. It checks 
     * that only valid articles are returned and ensures that fields of the valid articlex
     * are correct.
     */
    @Test
    public void testInvalidInput() {
        try {
            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/invalid.json")));

            Parser np = new NewsParser();
            List<Article> articles = np.parse(jsonContent, logger);

            assertEquals(1, articles.size());

            Article article = articles.get(0);
            assertEquals("Title 2", article.getTitle());
            assertEquals("Description 2", article.getDescription());
            assertEquals("2024-10-15T11:00:00Z", article.getPublishedAt());
            assertEquals("https://example.com/2", article.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown during parsing: " + e.getMessage());
        }
    }

    @Test
    public void testCorrectParserAssignment() {
        // Give correct input configuration to SourceFormat
        // In this case NEWSAPI format to the constructor when using NewsParser
        // There should be no errors thrown
        try {
            SourceFormat newsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.NEWSAPI);
            
            Parser newsParser = new NewsParser();

            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/valid.json")));

            newsSource.accept(newsParser, jsonContent, logger);

        } catch (Exception e) {
            Assert.fail("Exception should not be thrown with correct parser: " + e.getMessage());
        }
    }

    @Test
    public void testIncorrectParserFormat() {
        // Give incorrect input configuration to SourceFormat
        // In this case SIMPLE format to the constructor when using NewsParser
        // There should be exception thrown
        try {
            SourceFormat newsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.SIMPLE);
            
            Parser simpleParser = new NewsParser();

            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/valid.json")));

            newsSource.accept(simpleParser, jsonContent, logger);

            // We should not see this following assert fail
            Assert.fail("Expected IllegalArgumentException for incorrect parser");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("NewsParser can only handle NEWSAPI format"));
        } catch (Exception e) {
            Assert.fail("Unexpected exception type: " + e.getClass().getName());
        }
    }
}
