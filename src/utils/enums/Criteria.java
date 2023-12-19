package utils.enums;

public enum Criteria {
    BEFORE,
    AFTER;

    /**
     * Parses the criteria given as a String by the first character.
     * @param text Criteria to parse.
     * @return Criteria enum with the requested meaning.
     * @throws IllegalArgumentException if text does not start with either '>' or '<'.
     */
    public static Criteria parseCriteria(final String text) {
        if (text.charAt(0) == '<') {
            return BEFORE;
        }

        if (text.charAt(0) == '>') {
            return AFTER;
        }

        throw new IllegalArgumentException("Criteria does not respect the requested format.");
    }
}
