package database.users;

import database.records.Event;
import database.audio.Album;
import fileio.input.CommandInput;
import pages.ArtistPage;
import utils.enums.UserType;
import java.util.ArrayList;

public final class Artist extends User {
    private final ArtistPage officialPage;

    /* Constructor */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        this.setType(UserType.ARTIST);
        this.officialPage = new ArtistPage(this);
    }

    /**
     * Adds an album to the page.
     * @return instance of the added album.
     */
    public Album addAlbum(final CommandInput commandInput) {
        return officialPage.addAlbum(commandInput);
    }


    /**
     * Removes an album from the page.
     * @param album album to remove
     */
    public void removeAlbum(final Album album) {
        officialPage.removeAlbum(album);
    }


    /**
     * Searches if an album exists on the page.
     * @param name name of the searched album
     */
    public Album findAlbum(final String name) {
        return officialPage.findAlbum(name);
    }


    /**
     * Adds an event to the page.
     */
    public void addEvent(final CommandInput commandInput) {
        officialPage.addEvent(commandInput);
    }


    /**
     * Searches if an event exists on the page.
     * @param name name of the searched event.
     */
    public Event findEvent(final String name) {
        return officialPage.findEvent(name);
    }


    /**
     * Removes an event from the page.
     * @param event event to remove.
     */
    public void removeEvent(final Event event) {
        officialPage.removeEvent(event);
    }


    /**
     * Adds a piece of merch to the page.
     */
    public void addMerch(final CommandInput commandInput) {
        officialPage.addMerch(commandInput);
    }


    /**
     * @return number of likes of the artist.
     */
    public int computeLikeCnt() {
        int totalLikes = 0;

        for (Album album : getAlbums()) {
            totalLikes += album.computeLikeCnt();
        }

        return totalLikes;
    }

    /* Getters and Setters */
    public ArtistPage getOfficialPage() {
        return officialPage;
    }
    public ArrayList<Album> getAlbums() {
        return officialPage.getAlbums();
    }
}
