package commands.adminCommands.deleteUserStrategy;

import client.Session;
import database.audio.Podcast;
import database.users.Host;
import database.users.User;

public final class DeleteHostStrategy implements IDeleteUserStrategy {
    private final Session session;
    private final Host host;

    /* Constructor */
    public DeleteHostStrategy(final Session session, final Host host) {
        this.session = session;
        this.host = host;
    }

    @Override
    public boolean deleteUser() {
        // Check if any user is on host's official page.
        for (User user : session.getDatabase().getBasicUsers()) {
            if (user.getCurrPage() == host.getOfficialPage()) {
                return false;
            }
        }

        for (User user : session.getDatabase().getHosts()) {
            if (user.getCurrPage() == host.getOfficialPage()) {
                return false;
            }
        }

        for (User user : session.getDatabase().getArtists()) {
            if (user.getCurrPage() == host.getOfficialPage()) {
                return false;
            }
        }

        // Check if any user interacts with any of the host's podcasts.
        if (checkPodcastsInteractions()) {
            return false;
        }

        for (Podcast podcast : host.getPodcasts()) {
            session.getDatabase().removePodcast(podcast);
        }

        session.getDatabase().removeUser(host);
        return true;
    }


    /**
     * Checks if any user interacts with any of the host's podcasts.
     * @return true if it does, false otherwise.
     */
    private boolean checkPodcastsInteractions() {
        for (Podcast podcast : host.getPodcasts()) {
            for (User user : session.getDatabase().getArtists()) {
                if (user.interactsWithAudio(podcast)) {
                    return true;
                }
            }

            for (User user : session.getDatabase().getHosts()) {
                if (user.interactsWithAudio(podcast)) {
                    return true;
                }
            }

            for (User user : session.getDatabase().getBasicUsers()) {
                if (user.interactsWithAudio(podcast)) {
                    return true;
                }
            }
        }
        return false;
    }
}
