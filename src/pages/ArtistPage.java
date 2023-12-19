package pages;

import database.records.Event;
import database.audio.Album;
import database.audio.Song;
import database.records.Merch;
import database.users.User;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import utils.enums.PageType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;

import static utils.Constants.MAX_YEAR_EVENT;
import static utils.Constants.MIN_YEAR_EVENT;

public final class ArtistPage extends Page {
    private final ArrayList<Album> albums;
    private final ArrayList<Event> events;
    private final ArrayList<Merch> merchList;

    /* Constructor */
    public ArtistPage(final User owningUser) {
        super(owningUser);
        setType(PageType.ARTIST_PAGE);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchList = new ArrayList<>();
    }

    /**
     * Adds a new album to the album list of the page.
     * @param commandInput Data containing details of the new album.
     * @throws IllegalArgumentException if the operation fails.
     */
    public Album addAlbum(final CommandInput commandInput) throws IllegalArgumentException {
        for (Album album : albums) {
            if (album.getName().equals(commandInput.getName())) {
                throw new IllegalArgumentException(commandInput.getUsername()
                        + " has another album with the same name.");
            }
        }

        for (SongInput songInput1 : commandInput.getSongs()) {
            for (SongInput songInput2 : commandInput.getSongs()) {
                if (songInput1 != songInput2
                        && songInput1.getName().equals(songInput2.getName())) {
                    throw new IllegalArgumentException(commandInput.getUsername()
                            + " has the same song at least twice in this album.");
                }
            }
        }

        Album newAlbum = new Album(commandInput.getName(), getOwningUser().getUsername(),
                                commandInput.getReleaseYear(), commandInput.getDescription());

        for (SongInput songInput : commandInput.getSongs()) {
            newAlbum.addSong(new Song(songInput));
        }

        albums.add(newAlbum);
        return newAlbum;
    }


    /**
     *  Removes the album from the album list.
     */
    public void removeAlbum(final Album album) {
        albums.remove(album);
    }


    /**
     * @param name name of the requested album
     * @return Album from the list with the given name.
     * @throws IllegalArgumentException if no album with the given name is found.
     */
    public Album findAlbum(final String name) throws IllegalArgumentException {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        throw new IllegalArgumentException(getOwningUser().getUsername()
                + " doesn't have an album with the given name.");
    }


    /**
     * Adds a new event to the event list.
     * @param commandInput Data containing details of the new event.
     * @throws IllegalArgumentException if the operation fails.
     */
    public void addEvent(final CommandInput commandInput) throws IllegalArgumentException {
        for (Event event : events) {
            if (event.name().equals(commandInput.getName())) {
                throw new IllegalArgumentException(commandInput.getUsername()
                        + " has another event with the same name.");
            }
        }

        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
            date = LocalDate.parse(commandInput.getDate(), formatter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Event for " + commandInput.getUsername()
                                                + " does not have a valid date.");
        }

        if (date.getYear() < MIN_YEAR_EVENT || date.getYear() > MAX_YEAR_EVENT) {
            throw new IllegalArgumentException("Event for " + commandInput.getUsername()
                                                + " does not have a valid date.");
        }

        Event newEvent = new Event(commandInput.getName(), commandInput.getDescription(),
                                    commandInput.getDate());
        events.add(newEvent);
    }


    /**
     * @param name name of the requested event.
     * @return Event from the list with the given name.
     * @throws IllegalArgumentException if no event with the given name is found.
     */
    public Event findEvent(final String name) throws IllegalArgumentException {
        for (Event event : events) {
            if (event.name().equals(name)) {
                return event;
            }
        }

        throw new IllegalArgumentException(getOwningUser().getUsername()
                + " doesn't have an event with the given name.");
    }


    /**
     * Removes the event from the event list.
     */
    public void removeEvent(final Event event) {
        events.remove(event);
    }


    /**
     * Adds a new merch to the merch list.
     * @param commandInput Data containing details of the new merch.
     * @throws IllegalArgumentException if the operation fails.
     */
    public void addMerch(final CommandInput commandInput) throws IllegalArgumentException {
        for (Merch merch : merchList) {
            if (merch.name().equals(commandInput.getName())) {
                throw new IllegalArgumentException(commandInput.getUsername()
                        + " has merchandise with the same name.");
            }
        }

        if (commandInput.getPrice() < 0) {
            throw new IllegalArgumentException("Price for merchandise can not be negative.");
        }

        Merch newMerch = new Merch(commandInput.getName(), commandInput.getDescription(),
                                    commandInput.getPrice());
        merchList.add(newMerch);
    }


    @Override
    public String printPage() {
        StringBuilder stringBuilder = new StringBuilder("Albums:\n\t[");

        Iterator<Album> albumIterator = albums.iterator();
        while (albumIterator.hasNext()) {
            Album album = albumIterator.next();
            stringBuilder.append(album.getName());

            if (albumIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]\n\nMerch:\n\t[");

        Iterator<Merch> merchIterator = merchList.iterator();
        while (merchIterator.hasNext()) {
            Merch merch = merchIterator.next();
            stringBuilder.append(merch.name()).append(" - ").append(merch.price())
                    .append(":\n\t").append(merch.description());

            if (merchIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]\n\nEvents:\n\t[");

        Iterator<Event> eventIterator = events.iterator();
        while (eventIterator.hasNext()) {
            Event event = eventIterator.next();
            stringBuilder.append(event.name()).append(" - ").append(event.date())
                    .append(":\n\t").append(event.description());

            if (eventIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /* Getters */
    public ArrayList<Album> getAlbums() {
        return albums;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
}
