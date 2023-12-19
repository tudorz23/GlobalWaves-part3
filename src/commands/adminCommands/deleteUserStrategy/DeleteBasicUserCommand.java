package commands.adminCommands.deleteUserStrategy;

import client.Session;
import database.users.BasicUser;

public final class DeleteBasicUserCommand implements IDeleteUserStrategy {
    private final Session session;
    private final BasicUser basicUser;

    /* Constructor */
    public DeleteBasicUserCommand(final Session session, final BasicUser basicUser) {
        this.session = session;
        this.basicUser = basicUser;
    }

    @Override
    public boolean deleteUser() {
        session.getDatabase().removeUser(basicUser);
        return true;
    }
}
