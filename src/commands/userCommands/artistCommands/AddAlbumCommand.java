package commands.userCommands.artistCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.audio.Album;
import database.users.Artist;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.UserType;

public final class AddAlbumCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final User user;
    private final ArrayNode output;

    /* Constructor */
    public AddAlbumCommand(final Session session, final CommandInput commandInput,
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

        if (user.getType() != UserType.ARTIST) {
            printer.print(user.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;

        Album newAlbum;
        try {
            newAlbum = artist.addAlbum(commandInput);
        } catch (IllegalArgumentException illegalArgumentException) {
            printer.print(illegalArgumentException.getMessage());
            return;
        }

        session.getDatabase().addAlbum(newAlbum);
        printer.print(user.getUsername() + " has added new album successfully.");
    }
}
