package commands.adminCommands.recommendationsStrategy;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.audio.Song;
import database.users.User;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSongStrategy implements RecommendationStrategy {
    private final Session session;
    private final User user;
    private final PrinterBasic printer;

    /* Constructor */
    public RandomSongStrategy(final Session session, final User user,
                              final PrinterBasic printer) {
        this.session = session;
        this.user = user;
        this.printer = printer;
    }

    @Override
    public void recommend() {
        Song playingSong = (Song) user.getPlayer().getCurrPlaying();
        int timePosition = playingSong.getTimePosition();

        if (timePosition < 30) {
            printer.print("No new recommendations were found");
            return;
        }

        List<Song> sameGenreSongs = new ArrayList<>();
        for (Song song : session.getDatabase().getSongs()) {
            if (song.getGenre().equalsIgnoreCase(playingSong.getGenre())) {
                sameGenreSongs.add(song);
            }
        }

        if (sameGenreSongs.isEmpty()) {
            printer.print("No new recommendations were found");
            return;
        }

        Random rand = new Random(timePosition);
        int index = rand.nextInt(sameGenreSongs.size());

        Song recommendation = sameGenreSongs.get(index);

        try {
            user.getAnalytics().addSongRecommendation(recommendation);
        } catch (IllegalArgumentException exception) {
            printer.print("No new recommendations were found");
        }

        user.getAnalytics().updateLatestRecommendation(recommendation);
        printer.print("The recommendations for user " + user.getUsername()
                + " have been updated successfully.");
    }
}
