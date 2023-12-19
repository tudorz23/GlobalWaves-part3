package commands.adminCommands.deleteUserStrategy;

import client.Session;
import database.audio.Album;
import database.users.Artist;
import database.users.User;

public final class DeleteArtistStrategy implements IDeleteUserStrategy {
    private final Session session;
    private final Artist artist;

    /* Constructor */
    public DeleteArtistStrategy(final Session session, final Artist artist) {
        this.session = session;
        this.artist = artist;
    }

    @Override
    public boolean deleteUser() {
        // Check if any user is on artist's official page.
        for (User user : session.getDatabase().getBasicUsers()) {
            if (user.getCurrPage() == artist.getOfficialPage()) {
                return false;
            }
        }

        for (User user : session.getDatabase().getArtists()) {
            if (user.getCurrPage() == artist.getOfficialPage()) {
                return false;
            }
        }

        for (User user : session.getDatabase().getHosts()) {
            if (user.getCurrPage() == artist.getOfficialPage()) {
                return false;
            }
        }

        // Check if any user interacts with any of the artist's albums.
        if (checkAlbumsInteractions()) {
            return false;
        }

        for (Album album : artist.getAlbums()) {
            session.getDatabase().removeAlbum(album);
        }

        session.getDatabase().removeUser(artist);
        return true;
    }


    /**
     * Checks if any user interacts with any of the artist's albums.
     * @return true if it does, false otherwise.
     */
    private boolean checkAlbumsInteractions() {
        for (Album album : artist.getAlbums()) {
            for (User user : session.getDatabase().getBasicUsers()) {
                if (user.interactsWithAudio(album)) {
                    return true;
                }
            }

            for (User user : session.getDatabase().getHosts()) {
                if (user.interactsWithAudio(album)) {
                    return true;
                }
            }

            for (User user : session.getDatabase().getArtists()) {
                if (user.interactsWithAudio(album)) {
                    return true;
                }
            }
        }
        return false;
    }
}
