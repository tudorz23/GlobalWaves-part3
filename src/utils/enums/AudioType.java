package utils.enums;

public enum AudioType {
    SONG("song"),
    PLAYLIST("playlist"),
    PODCAST("podcast"),
    ALBUM("album");

    private final String label;

    AudioType(final String label) {
        this.label = label;
    }

    /**
     * Gets an AudioType enum from the label String.
     * @param text String that will be compared to the labels.
     * @return AudioType enum corresponding to the label.
     */
    public static AudioType fromString(final String text) {
        for (AudioType audioType : AudioType.values()) {
            if (audioType.label.equals(text)) {
                return audioType;
            }
        }
        return null;
    }
}
