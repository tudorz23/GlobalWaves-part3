package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import commands.adminCommands.recommendationsStrategy.FansPlaylistStrategy;
import commands.adminCommands.recommendationsStrategy.RandomPlaylistStrategy;
import commands.adminCommands.recommendationsStrategy.RandomSongStrategy;
import commands.adminCommands.recommendationsStrategy.RecommendationStrategy;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.PlayerState;
import utils.enums.UserType;

public class UpdateRecommendationsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public UpdateRecommendationsCommand(final Session session, final CommandInput commandInput,
                             final User user, final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.user = user;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        if (user.getType() != UserType.BASIC_USER) {
            printer.print(user.getUsername() + " is not a normal user.");
            return;
        }

        user.getPlayer().simulateTimePass(session.getTimestamp());

        if (user.getPlayer().getPlayerState() == PlayerState.EMPTY
                || user.getPlayer().getPlayerState() == PlayerState.STOPPED) {
            printer.print("No new recommendations were found");
            return;
        }

        if (user.getPlayer().getCurrPlaying().getType() != AudioType.SONG) {
            printer.print("No new recommendations were found");
            return;
        }

        RecommendationStrategy strategy;
        switch (commandInput.getRecommendationType()) {
            case "random_song" -> strategy = new RandomSongStrategy(session, user, printer);
            case "random_playlist" -> strategy = new RandomPlaylistStrategy(session, user, printer);
            case "fans_playlist" -> strategy = new FansPlaylistStrategy(session, user, printer);
            default -> {
                printer.print("Command not supported.");
                return;
            }
        }

        strategy.recommend();
    }
}
