package client;

import commands.ICommand;

/**
 * Manages the commands.
 */
public final class Invoker {
    /* Constructor */
    public Invoker() { }

    /**
     * Calls the execute method from ICommand interface.
     */
    public void execute(final ICommand command) {
        command.execute();
    }
}
