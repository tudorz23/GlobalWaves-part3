package utils.enums;

public enum UserType {
    BASIC_USER("basic"),
    ARTIST("artist"),
    HOST("host");

    private final String label;

    UserType(final String label) {
        this.label = label;
    }

    /**
     * Gets a UserType enum from the label String.
     * @param text String that will be compared to the labels.
     * @return UserType enum corresponding to the label.
     */
    public static UserType fromString(final String text) {
        for (UserType userType : UserType.values()) {
            if (userType.label.equals(text)) {
                return userType;
            }
        }
        return null;
    }
}
