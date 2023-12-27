package client;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.CommandFactory;
import commands.ICommand;
import commands.adminCommands.MonetizationCommand;
import database.Database;
import database.audio.Podcast;
import database.audio.Song;
import database.users.BasicUser;
import database.users.User;
import fileio.input.*;
import utils.enums.PremiumState;

import java.util.List;
import java.util.Map;

import static utils.Constants.PREMIUM_FEE;

public class AdminInteraction {
    private Database database;
    private Session session;
    private final Invoker invoker;
    private CommandFactory commandFactory;
    private final LibraryInput libraryInput;
    private final List<CommandInput> commandList;
    private final ArrayNode output;

    /* Constructor */
    public AdminInteraction(final LibraryInput libraryInput, final List<CommandInput> commandList,
                            final ArrayNode output) {
        this.libraryInput = libraryInput;
        this.commandList = commandList;
        this.output = output;
        invoker = new Invoker();
    }

    /**
     * Entry point to the program.
     */
    public void startAdminInteraction() {
        prepareDatabase();
        initSession();
        commandFactory = new CommandFactory(session, output);
        startCommands();
        endProgram();
    }

    /**
     * Populates the database with data taken from the input.
     */
    private void prepareDatabase() {
        this.database = new Database();

        // Populate database with songs.
        for (SongInput songInput : libraryInput.getSongs()) {
            Song song = new Song(songInput);
            database.addSong(song);
        }

        // Populate database with podcasts.
        for (PodcastInput podcastInput : libraryInput.getPodcasts()) {
            Podcast podcast = new Podcast(podcastInput);
            database.addPodcast(podcast);
        }

        // Populate database with users.
        for (UserInput userInput : libraryInput.getUsers()) {
            BasicUser user =  new BasicUser(userInput, database);
            database.addBasicUser(user);
        }
    }

    /**
     * Initializes a new Session.
     */
    private void initSession() {
        this.session = new Session(database);
    }

    /**
     * Iterates through the commands from the input.
     */
    private void startCommands() {
        for (CommandInput commandInput : commandList) {
            executeAction(commandInput);
        }
    }

    /**
     * Calls the Command Factory to generate a command based on the commandInput.
     * @param commandInput the current command.
     */
    private void executeAction(final CommandInput commandInput) {
        ICommand command;

        try {
            command = commandFactory.getCommand(commandInput);
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }

        invoker.execute(command);
    }


    private void endProgram() {
        checkPremiumUsersMonetization();

        ICommand monetizationCommand = new MonetizationCommand(session, output);
        monetizationCommand.execute();
    }


    private void checkPremiumUsersMonetization() {
        for (User user : database.getBasicUsers()) {
            if (user.getPremiumState() == PremiumState.FREE) {
                continue;
            }

            Map<Song, Integer> listenedAsPremium = user.getPlayer().getListenedAsPremium();
            if (listenedAsPremium.isEmpty()) {
                continue;
            }

            Map<Song, Double> songMonetization = database
                            .computeSongMonetization(listenedAsPremium, PREMIUM_FEE);

            database.updateArtistMonetization(songMonetization);
        }
    }
}
