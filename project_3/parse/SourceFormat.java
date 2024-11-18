package parse;

import java.util.logging.Logger;

public class SourceFormat {

    public enum Source {
        FILE,
        URL
    }

    public enum Format {
        NEWSAPI,
        SIMPLE
    }

    private final Source source; 
    private final Format format;

    /**
     * Constructor for SourceFormat.
     * 
     * @param source the source of the data (file path or URL)
     * @param format the format of the data (NEWSAPI or SIMPLE)
     */
    public SourceFormat(Source source, Format format) {
        this.source = source;
        this.format = format;
    }

    /**
     * Gets the source.
     * 
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Gets the format.
     * 
     * @return the format
     */
    public Format getFormat() {
        return format;
    }

    public void accept(Parser parser, String jsonContent, Logger logger) throws Exception {
        parser.visit(this, jsonContent, logger);
    }
}
