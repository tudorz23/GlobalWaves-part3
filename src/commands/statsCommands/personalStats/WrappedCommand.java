package commands.statsCommands.personalStats;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.Player;
import database.analytics.Analytics;
import database.analytics.ArtistAnalytics;
import database.analytics.HostAnalytics;
import database.users.Artist;
import database.users.Host;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import fileio.output.stats.PrinterWrapped;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class WrappedCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public WrappedCommand(final Session session, final CommandInput commandInput,
                          final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());

        Player userPlayer = user.getPlayer();
        if (userPlayer != null) {
            userPlayer.simulateTimePass(session.getTimestamp());
        }

        switch (user.getType()) {
            case BASIC_USER -> wrappedBasicUser();
            case ARTIST -> wrappedArtist();
            case HOST -> wrappedHost();
        }
    }

    public void wrappedBasicUser() {
        Analytics analytics = user.getAnalytics();
        if (analytics.getTopArtists().isEmpty() && analytics.getTopGenres().isEmpty()
            && analytics.getTopSongs().isEmpty() && analytics.getTopAlbums().isEmpty()
            && analytics.getTopPodcasts().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername());
            return;
        }

        PrinterWrapped printer = new PrinterWrapped(user, session, output);
        printer.printBasicUser();
    }


    public void wrappedArtist() {
        ArtistAnalytics analytics = ((Artist) user).getArtistAnalytics();
        if (analytics.getArtistTopAlbums().isEmpty() && analytics.getArtistTopSongs().isEmpty()
            && analytics.getArtistTopFans().isEmpty() && analytics.getArtistTopCities().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername());
            return;
        }

        PrinterWrapped printer = new PrinterWrapped(user, session, output);
        printer.printArtist();
    }

    public void wrappedHost() {
        HostAnalytics analytics = ((Host) user).getHostAnalytics();
        if (analytics.getHostTopFans().isEmpty() && analytics.getHostTopEpisodes().isEmpty()) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("No data to show for user " + user.getUsername());
            return;
        }

        PrinterWrapped printer = new PrinterWrapped(user, session, output);
        printer.printHost();
    }
}
