package pages;

import database.audio.Playlist;
import database.audio.Song;
import database.users.User;
import utils.enums.PageType;

import java.util.Iterator;

public final class LikedContentPage extends Page {
    /* Constructor */
    public LikedContentPage(final User owningUser) {
        super(owningUser);
        setType(PageType.LIKED_CONTENT);
    }

    @Override
    public String printPage() {
        StringBuilder stringBuilder = new StringBuilder("Liked songs:\n\t[");

        Iterator<Song> songIterator = getOwningUser().getLikedSongs().iterator();
        while (songIterator.hasNext()) {
            Song song = songIterator.next();
            stringBuilder.append(song.getName()).append(" - ").append(song.getArtist());

            if (songIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]\n\nFollowed playlists:\n\t[");
        Iterator<Playlist> playlistIterator = getOwningUser().getFollowedPlaylists().iterator();
        while (playlistIterator.hasNext()) {
            Playlist playlist = playlistIterator.next();
            stringBuilder.append(playlist.getName()).append(" - ").append(playlist.getOwner());

            if (playlistIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
