package database.users;

import database.Database;
import database.analytics.ContentCreatorAnalytics;
import database.analytics.HostAnalytics;
import database.records.Announcement;
import database.audio.Podcast;
import fileio.input.CommandInput;
import pages.HostPage;
import utils.enums.UserType;

import java.util.ArrayList;

public final class Host extends ContentCreator {
    private final HostPage officialPage;
    private HostAnalytics hostAnalytics;

    /* Constructor */
    public Host(final String username, final int age, final String city,
                final Database database) {
        super(username, age, city, database);
        this.setType(UserType.HOST);
        this.officialPage = new HostPage(this);
        this.hostAnalytics = new HostAnalytics();
    }

    /**
     * Adds a new podcast to the page.
     * @return The instance of the added Podcast.
     */
    public Podcast addPodcast(final CommandInput commandInput) {
        return officialPage.addPodcast(commandInput);
    }


    /**
     * Removes a podcast from the page.
     */
    public void removePodcast(final Podcast podcast) {
        officialPage.removePodcast(podcast);
    }


    /**
     * Searches if a Podcast exists on the page.
     * @param name name of the searched podcast.
     */
    public Podcast findPodcast(final String name) {
        return officialPage.findPodcast(name);
    }


    /**
     * Adds an announcement on the page.
     */
    public void addAnnouncement(final CommandInput commandInput) {
        officialPage.addAnnouncement(commandInput);
    }


    /**
     * Searches if an announcement exists on the page.
     * @param name name of the searched announcement.
     */
    public Announcement findAnnouncement(final String name) {
        return officialPage.findAnnouncement(name);
    }


    /**
     * Removes an announcement from the page.
     */
    public void removeAnnouncement(final Announcement announcement) {
        officialPage.removeAnnouncement(announcement);
    }


    @Override
    public ContentCreatorAnalytics getContentCreatorAnalytics() {
        return hostAnalytics;
    }

    /* Getters and Setters */
    public HostPage getOfficialPage() {
        return officialPage;
    }
    public ArrayList<Podcast> getPodcasts() {
        return officialPage.getPodcasts();
    }
    public HostAnalytics getHostAnalytics() {
        return hostAnalytics;
    }
}
