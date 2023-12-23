package database;

import database.audio.*;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import database.users.User;
import utils.enums.UserType;

import java.util.ArrayList;

public final class Database {
    private ArrayList<BasicUser> basicUsers;
    private ArrayList<Artist> artists;
    private ArrayList<Host> hosts;
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;

    /* Constructor */
    public Database() {
        this.basicUsers = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.hosts = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    /**
     * Adds a new song to the songs list.
     */
    public void addSong(final Song song) {
        songs.add(song);
    }


    /**
     * Adds a new podcast to the podcasts list.
     */
    public void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }


    /**
     * Removes a podcast from the podcast list.
     */
    public void removePodcast(final Podcast podcast) {
        podcasts.remove(podcast);

        for (User user : basicUsers) {
            user.removeListenedPodcast(podcast);
        }

        for (User user : artists) {
            user.removeListenedPodcast(podcast);
        }

        for (User user : hosts) {
            user.removeListenedPodcast(podcast);
        }
    }


    /**
     * Adds a new basic user to the basicUsers list.
     */
    public void addBasicUser(final BasicUser user) {
        basicUsers.add(user);
    }


    /**
     * Adds a new artist to the artists list.
     */
    public void addArtist(final Artist artist) {
        artists.add(artist);
    }


    /**
     * Adds a new host to the hosts list.
     */
    public void addHost(final Host host) {
        hosts.add(host);
    }


    /**
     * Adds a new playlist to the playlists list.
     */
    public void addPlaylist(final Playlist playlist) {
        playlists.add(playlist);
    }


    /**
     * Removes a playlist from the database.
     */
    public void removePlaylist(final Playlist playlist) {
        playlists.remove(playlist);

        for (User user : basicUsers) {
            user.removeFollowedPlaylist(playlist);
        }

        for (User user : artists) {
            user.removeFollowedPlaylist(playlist);
        }

        for (User user : hosts) {
            user.removeFollowedPlaylist(playlist);
        }
    }


    /**
     * Adds a new album to the albums list (and all its songs to the songs list).
     */
    public void addAlbum(final Album album) {
        albums.add(album);

        for (Song song : album.getSongs()) {
            addSong(song);
        }
    }


    /**
     * Removes an album from the album list (and all its songs from the song list).
     */
    public void removeAlbum(final Album album) {
        albums.remove(album);

        for (Song song : album.getSongs()) {
            songs.remove(song);

            for (Playlist playlist : playlists) {
                if (playlist.getSongs().contains(song)) {
                    playlist.removeSong(song);
                }
            }

            for (User user : basicUsers) {
                user.removeLikedSong(song);
            }

            for (User user : artists) {
                user.removeLikedSong(song);
            }

            for (User user : hosts) {
                user.removeLikedSong(song);
            }
        }
    }


    /**
     * Checks if the Audio entity can be removed or not,
     * i.e. if any user interacts with it.
     * @return true, if it can, false otherwise.
     */
    public boolean canRemoveAudio(final Audio audio) {
        for (User iterUser : basicUsers) {
            if (iterUser.interactsWithAudio(audio)) {
                return false;
            }
        }

        for (User iterUser : artists) {
            if (iterUser.interactsWithAudio(audio)) {
                return false;
            }
        }

        for (User iterUser : hosts) {
            if (iterUser.interactsWithAudio(audio)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Traverses the song database and returns the instance of the
     * requested song, if it exists.
     * @param reqSong Song object to search for in the Database (might be a deep copy).
     * @return Song instance for success, null otherwise.
     * @throws IllegalArgumentException if the song is not found.
     */
    public Song searchSongInDatabase(final Song reqSong) throws IllegalArgumentException {
        for (Song song : songs) {
            if (song.equals(reqSong)) {
                return song;
            }
        }
        throw new IllegalArgumentException("Critical: Song not found in the database.");
    }


    /**
     * Traverses the podcast database and returns the instance of the
     * requested podcast, if it exists.
     * @param reqPodcast Podcast object to search for in the Database (might be a deep copy).
     * @return Podcast instance for success, null otherwise.
     * @throws IllegalArgumentException if the podcast is not found.
     */
    public Podcast searchPodcastInDatabase(final Podcast reqPodcast) throws IllegalArgumentException {
        for (Podcast podcast : podcasts) {
            if (podcast.equals(reqPodcast)) {
                return podcast;
            }
        }
        throw new IllegalArgumentException("Critical: Podcast not found in the database.");
    }


    /**
     * Traverses the user lists and searches for the username.
     * @return true if the username exists in the database, false otherwise.
     */
    public boolean checkExistingUsername(final String username) {
        for (User user : basicUsers) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        for (User user : artists) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        for (User user : hosts) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Simulates the passing of the time for all the users of the database.
     * @param currTime Current time.
     */
    public void simulateTimeForEveryone(final int currTime) {
        for (User user : basicUsers) {
            user.getPlayer().simulateTimePass(currTime);
        }

        for (User artist : artists) {
            artist.getPlayer().simulateTimePass(currTime);
        }

        for (User host : hosts) {
            host.getPlayer().simulateTimePass(currTime);
        }
    }


    /**
     * Removes the user from the database.
     */
    public void removeUser(final User user) {
        if (user.getType() == UserType.BASIC_USER) {
            basicUsers.remove((BasicUser) user);
            return;
        }

        if (user.getType() == UserType.ARTIST) {
            artists.remove((Artist) user);
            return;
        }

        hosts.remove((Host) user);
    }


    /**
     * Traverses the artist database and returns the instance of the
     * requested artist, if it exists.
     * @throws IllegalArgumentException if the artist is not found.
     */
    public Artist searchArtistInDatabase(final String artistName) throws IllegalArgumentException {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(artistName)) {
                return artist;
            }
        }
        throw new IllegalArgumentException("Artist not found in the database.");
    }


    /**
     * Traverses the host database and returns the instance of the
     * requested host, if it exists.
     * @throws IllegalArgumentException if the host is not found.
     */
    public Host searchHostInDatabase(final String hostName) throws IllegalArgumentException {
        for (Host host : hosts) {
            if (host.getUsername().equals(hostName)) {
                return host;
            }
        }
        throw new IllegalArgumentException("Host not found in the database.");
    }

    /* Getters and Setters */
    public ArrayList<Song> getSongs() {
        return songs;
    }
    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }
    public ArrayList<BasicUser> getBasicUsers() {
        return basicUsers;
    }
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
    public ArrayList<Album> getAlbums() {
        return albums;
    }
    public ArrayList<Artist> getArtists() {
        return artists;
    }
    public ArrayList<Host> getHosts() {
        return hosts;
    }
}
