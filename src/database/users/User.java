package database.users;

import database.*;
import database.audio.*;
import fileio.input.UserInput;
import pages.HomePage;
import pages.Page;
import utils.enums.*;
import java.util.ArrayList;

public abstract class User extends Searchable {
    private String username;
    private int age;
    private String city;
    private ArrayList<Searchable> searchResult;  // null when no search has been done.
    private Searchable selection;
    private Player player;
    private ArrayList<Playlist> playlists;
    private ArrayList<Podcast> listenedPodcasts;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private UserType type;
    private Page currPage;
    private LogStatus logStatus;

    /* Constructors */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.searchResult = null;
        this.player = new Player();
        this.playlists = new ArrayList<>();
        this.listenedPodcasts = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.currPage = new HomePage(this);
        this.setSearchableType(SearchableType.USER);
    }

    public User(final UserInput userInput) {
        this(userInput.getUsername(), userInput.getAge(), userInput.getCity());
    }

    /**
     * Adds a new Playlist to user's playlists.
     * @return true, for success, false, if a playlist with the
     * same name already exists in user's list.
     */
    public boolean addPlaylist(final Playlist newPlaylist) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(newPlaylist.getName())) {
                return false;
            }
        }

        playlists.add(newPlaylist);
        return true;
    }


    /**
     * Adds a new Playlist to user's followed playlists.
     */
    public void addFollowedPlaylist(final Playlist playlist) {
        followedPlaylists.add(playlist);
    }


    /**
     * Removes a Playlist from user's followed playlists.
     */
    public void removeFollowedPlaylist(final Playlist playlist) {
        followedPlaylists.remove(playlist);
    }


    /**
     * Adds a new Song to user's liked songs.
     */
    public void addLikedSong(final Song song) {
        likedSongs.add(song);
        song.incrementLikeCnt();
    }


    /**
     * Removes a Song from user's liked songs.
     */
    public void removeLikedSong(final Song song) {
        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.decrementLikeCnt();
        }
    }


    /**
     * If podcast is found to have been listened to, it is removed from the list.
     * @param podcast podcast to remove if has been listened to.
     */
    public void removeListenedPodcast(final Podcast podcast) {
        listenedPodcasts.removeIf(listenedPodcast
                -> listenedPodcast.getName().equals(podcast.getName())
                && listenedPodcast.getOwner().equals(podcast.getOwner()));
    }


    /**
     * Checks if the User interacts with the Audio entity, i.e. if he has it
     * loaded or has one of its songs loaded, in case of Playlists and Albums.
     * @param audio Audio entity.
     * @return true if he does interact with it, false otherwise.
     */
    public boolean interactsWithAudio(final Audio audio) {
        if (player.getPlayerState() == PlayerState.EMPTY
                || player.getPlayerState() == PlayerState.STOPPED) {
            return false;
        }

        switch (audio.getType()) {
            case SONG -> {
                Song audioSong = (Song) audio;
                return interactsWithSong(audioSong);
            }
            case ALBUM -> {
                Album audioAlbum = (Album) audio;
                return interactsWithAlbum(audioAlbum);
            }
            case PLAYLIST -> {
                Playlist audioPlaylist = (Playlist) audio;
                return interactsWithPlaylist(audioPlaylist);
            }
            default -> {
                Podcast audioPodcast = (Podcast) audio;
                return interactsWithPodcast(audioPodcast);
            }
        }
    }


    /**
     * Helper for checking if the user interacts with the song.
     * @return true if he does, false otherwise.
     */
    private boolean interactsWithSong(final Song song) {
        Audio currPlaying = player.getCurrPlaying();

        if (currPlaying.getType() != AudioType.SONG) {
            return false;
        }

        Song currSong = (Song) currPlaying;
        return song.getName().equals(currSong.getName())
                && song.getArtist().equals(currSong.getArtist());
    }


    /**
     * Helper for checking if the user interacts with the album.
     * @return true if he does, false otherwise.
     */
    private boolean interactsWithAlbum(final Album album) {
        Audio currPlaying = player.getCurrPlaying();

        switch (currPlaying.getType()) {
            case PLAYLIST -> {
                Playlist currPlaylist = (Playlist) currPlaying;
                for (Song playlistSong : currPlaylist.getSongs()) {
                    for (Song albumSong : album.getSongs()) {
                        if (playlistSong.getName().equals(albumSong.getName())
                                && playlistSong.getArtist().equals(albumSong.getArtist())) {
                            return true;
                        }
                    }
                }
                return false;
            }
            case ALBUM -> {
                Album currAlbum = (Album) currPlaying;
                return album.getName().equals(currAlbum.getName())
                    && album.getOwner().equals(currAlbum.getOwner());
            }
            case SONG -> {
                Song currSong = (Song) currPlaying;
                for (Song song : album.getSongs()) {
                    if (song.getName().equals(currSong.getName())
                        && song.getArtist().equals(currSong.getArtist())) {
                        return true;
                    }
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }


    /**
     * Helper for checking if the user interacts with the playlist.
     * @return true if he does, false otherwise.
     */
    private boolean interactsWithPlaylist(final Playlist playlist) {
        Audio currPlaying = player.getCurrPlaying();
        if (currPlaying.getType() != AudioType.PLAYLIST) {
            return false;
        }

        Playlist currPlaylist = (Playlist) currPlaying;
        return currPlaylist.getName().equals(playlist.getName())
                && currPlaylist.getOwner().equals(playlist.getOwner());
    }


    /**
     * Helper for checking if the user interacts with the podcast.
     * @return true if he does, false otherwise.
     */
    private boolean interactsWithPodcast(final Podcast podcast) {
        Audio currPlaying = player.getCurrPlaying();
        if (currPlaying.getType() != AudioType.PODCAST) {
            return false;
        }

        Podcast currPodcast = (Podcast) currPlaying;
        return currPodcast.getName().equals(podcast.getName())
                && currPodcast.getOwner().equals(podcast.getOwner());
    }


    /* Getters and Setters */
    /**
     * Getter for username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Setter for username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    /**
     * Getter for age.
     */
    public int getAge() {
        return age;
    }
    /**
     * Setter for age.
     */
    public void setAge(final int age) {
        this.age = age;
    }
    /**
     * Getter for city.
     */
    public String getCity() {
        return city;
    }
    /**
     * Setter for city.
     */
    public void setCity(final String city) {
        this.city = city;
    }
    /**
     * Getter for searchResult.
     */
    public ArrayList<Searchable> getSearchResult() {
        return searchResult;
    }
    /**
     * Setter for searchResult.
     */
    public void setSearchResult(final ArrayList<Searchable> searchResult) {
        this.searchResult = searchResult;
    }
    /**
     * Getter for selection.
     */
    public Searchable getSelection() {
        return selection;
    }
    /**
     * Setter for selection.
     */
    public void setSelection(final Searchable selection) {
        this.selection = selection;
    }
    /**
     * Getter for player.
     */
    public Player getPlayer() {
        return player;
    }
    /**
     * Setter for player.
     */
    public void setPlayer(final Player player) {
        this.player = player;
    }
    /**
     * Getter for playlist.
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
    /**
     * Getter for listenedPodcast.
     */
    public ArrayList<Podcast> getListenedPodcasts() {
        return listenedPodcasts;
    }
    /**
     * Getter for likedSongs.
     */
    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }
    /**
     * Getter for followedPlaylists.
     */
    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }
    /**
     * Getter for type.
     */
    public UserType getType() {
        return type;
    }
    /**
     * Setter for type.
     */
    public void setType(final UserType type) {
        this.type = type;
    }
    /**
     * Getter for currPage.
     */
    public Page getCurrPage() {
        return currPage;
    }
    /**
     * Setter for currPage.
     */
    public void setCurrPage(final Page currPage) {
        this.currPage = currPage;
    }
    /**
     * Getter for logStatus.
     */
    public LogStatus getLogStatus() {
        return logStatus;
    }
    /**
     * Setter for logStatus.
     */
    public void setLogStatus(final LogStatus logStatus) {
        this.logStatus = logStatus;
    }
}
