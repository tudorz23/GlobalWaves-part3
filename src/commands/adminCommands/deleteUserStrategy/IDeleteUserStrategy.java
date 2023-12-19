package commands.adminCommands.deleteUserStrategy;

/**
 * Interface for applying the Strategy Design Pattern to the Delete User Command.
 */
public interface IDeleteUserStrategy {
    /**
     * Deletes a user from the database and all his ties to the platform.
     * @return true, if user is deleted successfully, false otherwise.
     */
    boolean deleteUser();
}
