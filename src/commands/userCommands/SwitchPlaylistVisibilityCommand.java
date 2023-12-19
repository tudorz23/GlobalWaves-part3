package commands.userCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Playlist;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.LogStatus;
import utils.enums.PlaylistVisibility;

public final class SwitchPlaylistVisibilityCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public SwitchPlaylistVisibilityCommand(final Session session, final CommandInput commandInput,
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

        int oldId = commandInput.getPlaylistId();
        if (oldId > user.getPlaylists().size()) {
            printer.print("The specified playlist ID is too high.");
            return;
        }

        int realId = oldId - 1;

        Playlist playlist = user.getPlaylists().get(realId);

        PlaylistVisibility newVisibility =
                PlaylistVisibility.cycleVisibility(playlist.getVisibility());
        playlist.setVisibility(newVisibility);

        printer.print("Visibility status updated successfully to "
                        + playlist.getVisibility().getLabel() + ".");
    }
}
