package pages;

import database.users.User;
import fileio.input.CommandInput;
import utils.enums.PageType;

public final class PageFactory {
    private final User user;

    /* Constructor */
    public PageFactory(final User user) {
        this.user = user;
    }

    /**
     * Factory Method that creates Page instances, based on the CommandInput.
     * @param commandInput key that decides the type of instance that is created.
     * @return Page object.
     * @throws IllegalArgumentException if the requested page is invalid.
     */
    public Page getPage(final CommandInput commandInput) throws IllegalArgumentException {
        PageType pageType = PageType.fromString(commandInput.getNextPage());

        switch (pageType) {
            case HOME -> {
                return new HomePage(user);
            }
            case LIKED_CONTENT -> {
                return new LikedContentPage(user);
            }
            default -> throw new IllegalArgumentException("Invalid page.");
        }
    }
}
