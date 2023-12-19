package commands.adminCommands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.ICommand;
import database.users.Artist;
import database.users.BasicUser;
import database.users.Host;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;

public final class AddUserCommand implements ICommand {
    private final Session session;
    private final CommandInput commandInput;
    private final ArrayNode output;

    /* Constructor */
    public AddUserCommand(final Session session, final CommandInput commandInput,
                               final ArrayNode output) {
        this.session = session;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        session.setTimestamp(commandInput.getTimestamp());
        PrinterBasic printer = new PrinterBasic(output, commandInput);

        if (session.getDatabase().checkExistingUsername(commandInput.getUsername())) {
            printer.print("The username " + commandInput.getUsername() + " is already taken.");
            return;
        }

        switch (commandInput.getType()) {
            case "user" -> addBasicUser();
            case "artist" -> addArtist();
            case "host" -> addHost();
            default -> throw new IllegalArgumentException("Invalid input.");
        }

        printer.print("The username " + commandInput.getUsername()
                        + " has been added successfully.");
    }


    /**
     * Helper for adding a new basic user to the database.
     */
    private void addBasicUser() {
        BasicUser user = new BasicUser(commandInput.getUsername(), commandInput.getAge(),
                                        commandInput.getCity());
        session.getDatabase().addBasicUser(user);
    }


    /**
     * Helper for adding a new artist to the database.
     */
    private void addArtist() {
        Artist artist = new Artist(commandInput.getUsername(), commandInput.getAge(),
                                        commandInput.getCity());
        session.getDatabase().addArtist(artist);
    }


    /**
     * Helper for adding a new host to the database.
     */
    private void addHost() {
        Host host = new Host(commandInput.getUsername(), commandInput.getAge(),
                                        commandInput.getCity());
        session.getDatabase().addHost(host);
    }
}
