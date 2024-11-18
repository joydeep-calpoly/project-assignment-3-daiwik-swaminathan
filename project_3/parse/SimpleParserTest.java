package parse;

import org.junit.*;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;

public final class SimpleParserTest {

    private Logger logger;

    /**
     * Sets up the logger before each test case. This method is called before each test, 
     * initializing the logger used for testing purposes.
     */
    @Before
    public void setup() {
        logger = Logger.getLogger(SimpleParserTest.class.getName());
    }

    /**
     * Test method to validate the parsing of a JSON file with 2 valid articles.
     * It does assertions to make sure every field is as expected.
     */
    @Test
    public void testValidInput() {
        try {
            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/valid_simple.json")));
            
            SimpleParser parser = new SimpleParser();
            List<Article> articles = parser.parse(jsonContent, logger);

            assertEquals(2, articles.size());
            
            Article article = articles.get(0);
            assertEquals("Assignment #2", article.getTitle());
            assertEquals("Extend Assignment #1 to support multiple sources and to introduce source processor.", article.getDescription());
            assertEquals("2021-04-16 09:53:23.709229", article.getPublishedAt());
            assertEquals("https://canvas.calpoly.edu/courses/55411/assignments/274503", article.getUrl());

            Article article2 = articles.get(1);
            assertEquals("Assignment #3", article2.getTitle());
            assertEquals("Assignment on implementing JSON parsing for various formats.", article2.getDescription());
            assertEquals("2021-04-18 15:30:00.123456", article2.getPublishedAt());
            assertEquals("https://canvas.calpoly.edu/courses/55411/assignments/274504", article2.getUrl());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown during parsing: " + e.getMessage());
        }
    }

    /**
     * Test method to validate the parsing of a JSON file with 2 invalid articles.
     * Since both of the articles are missing at least one field, they both are not
     * to be included in the returned list and so the size should be 0.
     */
    @Test
    public void testInvalidInput() {
        try {
            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/invalid_json.json")));
            
            SimpleParser parser = new SimpleParser();
            List<Article> articles = parser.parse(jsonContent, logger);

            assertEquals(0, articles.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception thrown during parsing: " + e.getMessage());
        }
    }

    @Test
    public void testCorrectParserAssignment() {
        // Give correct input configuration to SourceFormat
        // In this case SIMPLE format to the constructor when using SimpleParser
        // There should be no errors thrown
        try {
            SourceFormat newsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.SIMPLE);
            
            Parser simpleParser = new SimpleParser();

            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/simple.txt")));

            newsSource.accept(simpleParser, jsonContent, logger);

        } catch (Exception e) {
            Assert.fail("Exception should not be thrown with correct parser: " + e.getMessage());
        }
    }

    @Test
    public void testIncorrectParserFormat() {
        // Give incorrect input configuration to SourceFormat
        // In this case NEWSAPI format to the constructor when using SimpleParser
        // There should be exception thrown
        try {
            SourceFormat newsSource = new SourceFormat(SourceFormat.Source.FILE, SourceFormat.Format.NEWSAPI);
            
            Parser newsParser = new SimpleParser();

            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/simple.txt")));

            newsSource.accept(newsParser, jsonContent, logger);

            // We should not see this following assert fail
            Assert.fail("Expected IllegalArgumentException for incorrect parser");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("SimpleParser cannot handle simple format"));
        } catch (Exception e) {
            Assert.fail("Unexpected exception type: " + e.getClass().getName());
        }
    }

    @Test
    public void testCorrectParserSource() {
        // Give incorrect input configuration to SourceFormat
        // In this case url source when simple parser can only do file
        // There should be an exception thrown
        try {
            SourceFormat newsSource = new SourceFormat(SourceFormat.Source.URL, SourceFormat.Format.SIMPLE);
            
            Parser simpleParser = new SimpleParser();

            String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get("/Users/daiwik/Desktop/project-assignment-3-daiwik-swaminathan/project_3/inputs/simple.txt")));

            newsSource.accept(simpleParser, jsonContent, logger);
            Assert.fail("Expected IllegalArgumentException for incorrect parser");

        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("SimpleParser cannot handle url source"));
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown with correct parser: " + e.getMessage());
        }
    }
}
