package utils.enums;

public enum CommandType {
    SEARCH("search"),
    SELECT("select"),
    LOAD("load"),
    PLAY_PAUSE("playPause"),
    REPEAT("repeat"),
    SHUFFLE("shuffle"),
    FORWARD("forward"),
    BACKWARD("backward"),
    LIKE("like"),
    NEXT("next"),
    PREV("prev"),
    ADD_REMOVE_IN_PLAYLIST("addRemoveInPlaylist"),
    STATUS("status"),
    CREATE_PLAYLIST("createPlaylist"),
    SWITCH_VISIBILITY("switchVisibility"),
    FOLLOW("follow"),
    SHOW_PLAYLISTS("showPlaylists"),
    SHOW_PREFERRED_SONGS("showPreferredSongs"),
    GET_TOP5_SONGS("getTop5Songs"),
    GET_TOP5_PLAYLISTS("getTop5Playlists"),
    ADD_USER("addUser"),
    DELETE_USER("deleteUser"),
    SHOW_ALBUMS("showAlbums"),
    SHOW_PODCASTS("showPodcasts"),
    ADD_ALBUM("addAlbum"),
    REMOVE_ALBUM("removeAlbum"),
    ADD_EVENT("addEvent"),
    REMOVE_EVENT("removeEvent"),
    ADD_MERCH("addMerch"),
    ADD_PODCAST("addPodcast"),
    REMOVE_PODCAST("removePodcast"),
    ADD_ANNOUNCEMENT("addAnnouncement"),
    REMOVE_ANNOUNCEMENT("removeAnnouncement"),
    SWITCH_CONNECTION_STATUS("switchConnectionStatus"),
    GET_TOP5_ALBUMS("getTop5Albums"),
    GET_TOP5_ARTISTS("getTop5Artists"),
    GET_ALL_USERS("getAllUsers"),
    GET_ONLINE_USERS("getOnlineUsers"),
    PRINT_CURRENT_PAGE("printCurrentPage"),
    CHANGE_PAGE("changePage");

    private final String label;

    CommandType(final String label) {
        this.label = label;
    }

    /**
     * Gets a CommandType enum from the label String.
     * @param text String that will be compared to the label.
     * @return CommandType enum corresponding to the label.
     * @throws IllegalArgumentException if the String does not correspond to a command.
     */
    public static CommandType fromString(final String text) throws IllegalArgumentException {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.label.equals(text)) {
                return commandType;
            }
        }

        throw new IllegalArgumentException("Command " + text + " not supported.");
    }
}
