package utils.enums;

public enum PlaylistVisibility {
    PUBLIC("public"),
    PRIVATE("private");

    private final String label;

    PlaylistVisibility(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * @param currVisibility visibility before cycling.
     * @return PUBLIC, if current visibility is PRIVATE, and otherwise.
     */
    public static PlaylistVisibility cycleVisibility(final PlaylistVisibility currVisibility) {
        if (currVisibility == PUBLIC) {
            return PRIVATE;
        }

        return PUBLIC;
    }
}
