package strategies;

import models.Content;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class BasicSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Content> search(String keyword, List<Content> allContent) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return allContent.stream()
                    .filter(Content::isPublic)
                    .sorted(Comparator.comparing(Content::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }
        
        String searchTerm = keyword.toLowerCase().trim();
        
        return allContent.stream()
                .filter(Content::isPublic) // Only search public content
                .filter(content -> 
                    content.getTitle().toLowerCase().contains(searchTerm) ||
                    content.getDescription().toLowerCase().contains(searchTerm)
                )
                .sorted((c1, c2) -> {
                    // Prioritize title matches over description matches
                    boolean c1TitleMatch = c1.getTitle().toLowerCase().contains(searchTerm);
                    boolean c2TitleMatch = c2.getTitle().toLowerCase().contains(searchTerm);
                    
                    if (c1TitleMatch && !c2TitleMatch) {
                        return -1;
                    } else if (!c1TitleMatch && c2TitleMatch) {
                        return 1;
                    } else {
                        // If both have title match or both don't, sort by engagement
                        long score1 = c1.getViews() + c1.getLikes();
                        long score2 = c2.getViews() + c2.getLikes();
                        return Long.compare(score2, score1); // Higher engagement first
                    }
                })
                .collect(Collectors.toList());
    }
}