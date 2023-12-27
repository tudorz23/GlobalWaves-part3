package pages;

import database.audio.Audio;
import database.audio.Podcast;
import database.audio.Song;
import database.audio.SongCollection;
import database.users.Artist;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import utils.enums.AudioType;
import utils.enums.PageType;
import utils.enums.PlayerState;

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
            case ARTIST_PAGE ->{
                if (user.getPlayer() == null || user.getPlayer().getPlayerState() == PlayerState.EMPTY
                    || user.getPlayer().getPlayerState() == PlayerState.STOPPED
                    || user.getPlayer().getCurrPlaying().getType() == AudioType.PODCAST) {
                    throw new IllegalArgumentException("Invalid page.");
                }

                String artistName = getArtistName();
                Artist artist = user.getDatabase().searchArtistInDatabase(artistName);
                return artist.getOfficialPage();
            }
            case HOST_PAGE -> {
                if (user.getPlayer() == null || user.getPlayer().getPlayerState() == PlayerState.EMPTY
                        || user.getPlayer().getPlayerState() == PlayerState.STOPPED
                        || user.getPlayer().getCurrPlaying().getType() != AudioType.PODCAST) {
                    throw new IllegalArgumentException("Invalid page.");
                }

                String hostName = ((Podcast) user.getPlayer().getCurrPlaying()).getOwner();
                Host host = user.getDatabase().searchHostInDatabase(hostName);
                return host.getOfficialPage();
            }
            default -> throw new IllegalArgumentException("Invalid page.");
        }
    }


    private String getArtistName() {
        String artistName;
        Audio currentlyPlaying = user.getPlayer().getCurrPlaying();
        if (currentlyPlaying.getType() == AudioType.SONG) {
            artistName = ((Song) currentlyPlaying).getArtist();
        } else {
            SongCollection collection = (SongCollection) currentlyPlaying;
            artistName = collection.getSongs().get(collection.getPlayingSongIndex())
                        .getArtist();
        }
        return artistName;
    }
}
