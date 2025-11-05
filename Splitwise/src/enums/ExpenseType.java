package enums;

/**
 * Enum representing different categories of expenses
 * Used for categorizing and organizing expenses
 */
public enum ExpenseType {
    /**
     * Food and dining related expenses
     */
    FOOD("Food & Dining"),
    
    /**
     * Travel and transportation expenses
     */
    TRAVEL("Travel & Transport"),
    
    /**
     * Entertainment and leisure expenses
     */
    ENTERTAINMENT("Entertainment"),
    
    /**
     * Utility bills (electricity, water, gas, internet)
     */
    UTILITIES("Utilities"),
    
    /**
     * Rent and housing expenses
     */
    RENT("Rent & Housing"),
    
    /**
     * Shopping and retail expenses
     */
    SHOPPING("Shopping"),
    
    /**
     * Healthcare and medical expenses
     */
    HEALTHCARE("Healthcare"),
    
    /**
     * Education related expenses
     */
    EDUCATION("Education"),
    
    /**
     * Miscellaneous expenses that don't fit other categories
     */
    OTHER("Other");
    
    private final String displayName;
    
    ExpenseType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Check if this expense type is considered essential
     * @return true if the expense is essential (utilities, rent, healthcare)
     */
    public boolean isEssential() {
        return this == UTILITIES || this == RENT || this == HEALTHCARE;
    }
}