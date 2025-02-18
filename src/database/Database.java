package database;

import database.audio.*;
import database.monetization.Monetization;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import database.users.User;
import utils.enums.UserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Database {
    private ArrayList<BasicUser> basicUsers;
    private ArrayList<Artist> artists;
    private ArrayList<Host> hosts;
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;
    private Monetization monetization;

    /* Constructor */
    public Database() {
        this.basicUsers = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.hosts = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.albums = new ArrayList<>();
        monetization = new Monetization();
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


    /**
     * Traverses the user database and returns the instance of the
     * requested user, if it exists.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User searchUserInDatabase(final String name) throws IllegalArgumentException {
        for (User user : getBasicUsers()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        for (User user : getArtists()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        for (User user : getHosts()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        throw new IllegalArgumentException("User not found in the database.");
    }


    /**
     * Computes the revenue that each listened song generates.
     * @param listenedSongs Map of < Song, listenCnt > type.
     * @return HashMap of < Song, Double >, meaning < Song, revenue >.
     */
    public Map<Song, Double> computeSongMonetization(final Map<Song, Integer> listenedSongs,
                                                     final double sumToSplit) {
        Map<Song, Double> songMonetization = new HashMap<>();

        double totalListens = 0.0;
        for (Map.Entry<Song, Integer> entry : listenedSongs.entrySet()) {
            totalListens += (double) entry.getValue();
        }

        for (Map.Entry<Song, Integer> entry : listenedSongs.entrySet()) {
            double songRevenue = entry.getValue() * sumToSplit / totalListens;
            songMonetization.put(entry.getKey(), songRevenue);
        }

        return songMonetization;
    }


    /**
     * Updates the song revenue map of the artists that own each of the Songs
     * from songMonetization map. These Songs are those that have been listened
     * to by one user after buying premium subscription or between two ads.
     * @param songMonetization Map of < Song, revenue > type.
     */
    public void updateArtistMonetization(final Map<Song, Double> songMonetization) {
        for (Map.Entry<Song, Double> entry : songMonetization.entrySet()) {
            Song song = entry.getKey();
            String artistName = song.getArtist();

            monetization.addSongRevenue(artistName, song, entry.getValue());
        }
    }


    /**
     * @return Deep copy of the Ad object from the database.
     */
    public Song getAdvertisementFromDatabase() {
        for (Song song : songs) {
            if (song.getName().equals("Ad Break")) {
                return song.getDeepCopy();
            }
        }
        // Never reached.
        throw new IllegalStateException("Ad not present in the database.");
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
    public Monetization getMonetization() {
        return monetization;
    }
}
