package pages;

import database.users.User;
import utils.enums.PageType;

public abstract class Page {
    private final User owningUser;
    private PageType type;

    /* Constructor */
    public Page(final User owningUser) {
        this.owningUser = owningUser;
    }

    /**
     * Prints details regarding the Page, personalized depending on the owning user.
     * @return Details as a String.
     */
    public abstract String printPage();


    /* Getters and Setters */
    /**
     * Getter for type;
     */
    public PageType getType() {
        return type;
    }

    /**
     * Setter for type.
     */
    public void setType(final PageType type) {
        this.type = type;
    }

    /**
     * Getter for owningUser.
     */
    public User getOwningUser() {
        return owningUser;
    }
}
