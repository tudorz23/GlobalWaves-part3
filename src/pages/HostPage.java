package pages;

import database.records.Announcement;
import database.audio.Episode;
import database.audio.Podcast;
import database.users.User;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import utils.enums.PageType;
import java.util.ArrayList;
import java.util.Iterator;

public final class HostPage extends Page {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;

    /* Constructor */
    public HostPage(final User owningUser) {
        super(owningUser);
        setType(PageType.HOST_PAGE);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Adds a new podcast to the podcast list.
     * @param commandInput Data containing details of the new podcast.
     * @throws IllegalArgumentException if the operation fails.
     */
    public Podcast addPodcast(final CommandInput commandInput) throws IllegalArgumentException {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(commandInput.getName())) {
                throw new IllegalArgumentException(commandInput.getUsername()
                        + " has another podcast with the same name.");
            }
        }

        for (EpisodeInput episodeInput1 : commandInput.getEpisodes()) {
            for (EpisodeInput episodeInput2 : commandInput.getEpisodes()) {
                if (episodeInput1 != episodeInput2
                    && episodeInput1.getName().equals(episodeInput2.getName())) {
                    throw new IllegalArgumentException(commandInput.getUsername()
                            + " has the same episode in this podcast.");
                }
            }
        }

        Podcast newPodcast = new Podcast(commandInput.getName(), commandInput.getUsername());

        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            newPodcast.addEpisode(new Episode(episodeInput));
        }

        podcasts.add(newPodcast);
        return newPodcast;
    }


    /**
     * Removes the podcast from the list.
     */
    public void removePodcast(final Podcast podcast) {
        podcasts.remove(podcast);
    }


    /**
     * @param name name of the requested podcast
     * @return Podcast from the list with the given name.
     * @throws IllegalArgumentException if no podcast with the given name is found.
     */
    public Podcast findPodcast(final String name) throws IllegalArgumentException {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        throw new IllegalArgumentException(getOwningUser().getUsername()
                + " doesn't have a podcast with the given name.");
    }


    /**
     * Adds a new announcement to the announcement list.
     * @param commandInput Data containing details of the new announcement.
     * @throws IllegalArgumentException if the operation fails.
     */
    public void addAnnouncement(final CommandInput commandInput) throws IllegalArgumentException {
        for (Announcement announcement : announcements) {
            if (announcement.name().equals(commandInput.getName())) {
                throw new IllegalArgumentException(commandInput.getUsername()
                        + " has already added an announcement with this name.");
            }
        }

        Announcement announcement = new Announcement(commandInput.getName(),
                                                    commandInput.getDescription());
        announcements.add(announcement);
    }


    /**
     * @param name name of the requested announcement.
     * @return Announcement from the list with the given name.
     * @throws IllegalArgumentException if no announcement with the given name is found.
     */
    public Announcement findAnnouncement(final String name) throws IllegalArgumentException {
        for (Announcement announcement : announcements) {
            if (announcement.name().equals(name)) {
                return announcement;
            }
        }

        throw new IllegalArgumentException(getOwningUser().getUsername()
                + " has no announcement with the given name.");
    }


    /**
     * Removes the announcement from the list.
     */
    public void removeAnnouncement(final Announcement announcement) {
        announcements.remove(announcement);
    }


    @Override
    public String printPage() {
        StringBuilder stringBuilder = new StringBuilder("Podcasts:\n\t[");

        Iterator<Podcast> podcastIterator = podcasts.iterator();
        while (podcastIterator.hasNext()) {
            Podcast podcast = podcastIterator.next();
            stringBuilder.append(podcast.getName()).append(":\n\t[");

            Iterator<Episode> episodeIterator = podcast.getEpisodes().iterator();
            while (episodeIterator.hasNext()) {
                Episode episode = episodeIterator.next();
                stringBuilder.append(episode.getName()).append(" - ")
                        .append(episode.getDescription());

                if (episodeIterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }

            stringBuilder.append("]\n");

            if (podcastIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]\n\nAnnouncements:\n\t[");

        Iterator<Announcement> announcementIterator = announcements.iterator();
        while (announcementIterator.hasNext()) {
            Announcement announcement = announcementIterator.next();
            stringBuilder.append(announcement.name()).append(":\n\t")
                    .append(announcement.description());

            if (announcementIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }

    /* Getters and Setters */
    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }
}
