package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Audio;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.AudioType;
import utils.enums.LogStatus;
import utils.enums.PlayerState;
import utils.enums.RepeatState;

public class LoadRecommendationsCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public LoadRecommendationsCommand(final Session session, final CommandInput commandInput,
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

        if (user.getLogStatus() == LogStatus.OFFLINE) {
            printer.printOfflineUser();
            return;
        }

        Audio recommendation = user.getAnalytics().getLatestRecommendation();

        if (recommendation == null) {
            printer.print("No recommendations available.");
            return;
        }

        user.getPlayer().setCurrPlaying(recommendation.getDeepCopy());

        user.getPlayer().getCurrPlaying().setListener(user);
        user.getPlayer().setPrevTimeInfo(session.getTimestamp());
        user.getPlayer().setPlayerState(PlayerState.PLAYING);
        user.getPlayer().setShuffle(false);

        if (recommendation.getType() == AudioType.PLAYLIST) {
            user.getPlayer().setRepeatState(RepeatState.NO_REPEAT_COLLECTION);
        } else {
            user.getPlayer().setRepeatState(RepeatState.NO_REPEAT);
        }

        user.getPlayer().getCurrPlaying().updateAnalytics();
        printer.print("Playback loaded successfully.");
    }
}
