package strategies;

import models.Content;
import java.util.List;

public interface SearchStrategy {
    /**
     * Search content based on the strategy implementation
     * @param keyword Search keyword
     * @param allContent List of all available content
     * @return List of matching content
     */
    List<Content> search(String keyword, List<Content> allContent);
}