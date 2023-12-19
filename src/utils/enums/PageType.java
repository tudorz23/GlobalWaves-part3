package utils.enums;

public enum PageType {
    HOME("Home"),
    LIKED_CONTENT("LikedContent"),
    ARTIST_PAGE("ArtistPage"),
    HOST_PAGE("HostPage");

    private final String label;

    PageType(final String label) {
        this.label = label;
    }

    /**
     * Gets a PageType enum from the label String.
     * @param text String that will be compared to the label.
     * @return PageType enum corresponding to the label.
     * @throws IllegalArgumentException if requested page does not exist.
     */
    public static PageType fromString(final String text) throws IllegalArgumentException {
        for (PageType pageType : PageType.values()) {
            if (pageType.label.equals(text)) {
                return pageType;
            }
        }
        throw new IllegalArgumentException("Non-existent page.");
    }
}
