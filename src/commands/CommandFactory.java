package commands;

import client.Session;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.adminCommands.AdBreakCommand;
import commands.adminCommands.AddUserCommand;
import commands.adminCommands.DeleteUserCommand;
import commands.adminCommands.UpdateRecommendationsCommand;
import commands.statsCommands.adminStats.*;
import commands.statsCommands.personalStats.*;
import commands.playerCommands.*;
import commands.searchbarCommands.LoadCommand;
import commands.searchbarCommands.SearchCommand;
import commands.searchbarCommands.SelectCommand;
import commands.userCommands.*;
import commands.userCommands.artistCommands.*;
import commands.userCommands.hostCommands.AddAnnouncementCommand;
import commands.userCommands.hostCommands.AddPodcastCommand;
import commands.userCommands.hostCommands.RemoveAnnouncementCommand;
import commands.userCommands.hostCommands.RemovePodcastCommand;
import commands.userCommands.pageCommands.ChangePageCommand;
import commands.userCommands.pageCommands.NextPageCommand;
import commands.userCommands.pageCommands.PreviousPageCommand;
import database.users.User;
import fileio.input.CommandInput;
import fileio.output.PrinterBasic;
import utils.enums.CommandType;

public class CommandFactory {
    private final Session session;
    private final ArrayNode output;

    /* Constructor */
    public CommandFactory(final Session session, final ArrayNode output) {
        this.session = session;
        this.output = output;
    }

    /**
     * Public Factory Method that creates ICommand instances, based on the CommandInput.
     * Split in multiple methods to keep them short and manageable.
     * This part manages commands that do not require a User instance.
     * @param commandInput key that decides the type of instance that is created.
     * @return ICommand object.
     * @throws IllegalArgumentException if command is not supported.
     */
    public ICommand getCommand(final CommandInput commandInput) throws IllegalArgumentException {
        CommandType commandType;
        try {
            commandType = CommandType.fromString(commandInput.getCommand());
        } catch (IllegalArgumentException exception) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("Command " + commandInput.getCommand() + " not supported.");
            throw new IllegalArgumentException("Invalid command.");
        }

        switch (commandType) {
            case GET_TOP5_SONGS -> {
                return new GetTop5SongsCommand(session, commandInput, output);
            }
            case GET_TOP5_PLAYLISTS -> {
                return new GetTop5PlaylistsCommand(session, commandInput, output);
            }
            case GET_ONLINE_USERS -> {
                return new GetOnlineUsersCommand(session, commandInput, output);
            }
            case ADD_USER -> {
                return new AddUserCommand(session, commandInput, output);
            }
            case GET_ALL_USERS -> {
                return new GetAllUsersCommand(session, commandInput, output);
            }
            case GET_TOP5_ALBUMS -> {
                return new GetTop5AlbumsCommand(session, commandInput, output);
            }
            case GET_TOP5_ARTISTS -> {
                return new GetTop5ArtistsCommand(session, commandInput, output);
            }
            default -> {
                return helperGetCommand(commandInput, commandType);
            }
        }
    }


    /**
     * Helper to the public Factory Method. This part manages commands from stage 1
     * that require a User instance.
     * @return ICommand object.
     */
    private ICommand helperGetCommand(final CommandInput commandInput,
                                      final CommandType commandType) {
        User user = getUser(commandInput);

        switch (commandType) {
            case SEARCH -> {
                return new SearchCommand(session, commandInput, user, output);
            }
            case SELECT -> {
                return new SelectCommand(session, commandInput, user, output);
            }
            case LOAD -> {
                return new LoadCommand(session, commandInput, user, output);
            }
            case PLAY_PAUSE -> {
                return new PlayPauseCommand(session, commandInput, user, output);
            }
            case STATUS -> {
                return new StatusCommand(session, commandInput, user, output);
            }
            case CREATE_PLAYLIST -> {
                return new CreatePlaylistCommand(session, commandInput, user, output);
            }
            case ADD_REMOVE_IN_PLAYLIST -> {
                return new AddRemoveInPlaylistCommand(session, commandInput, user, output);
            }
            case LIKE -> {
                return new LikeSongCommand(session, commandInput, user, output);
            }
            case SHOW_PLAYLISTS -> {
                return new ShowPlaylistsCommand(session, commandInput, user, output);
            }
            case SHOW_PREFERRED_SONGS -> {
                return new ShowPreferredSongsCommand(session, commandInput, user, output);
            }
            case REPEAT -> {
                return new RepeatCommand(session, commandInput, user, output);
            }
            case SHUFFLE -> {
                return new ShuffleCommand(session, commandInput, user, output);
            }
            case NEXT -> {
                return new NextCommand(session, commandInput, user, output);
            }
            case SWITCH_VISIBILITY -> {
                return new SwitchPlaylistVisibilityCommand(session, commandInput, user, output);
            }
            case FOLLOW -> {
                return new FollowPlaylistCommand(session, commandInput, user, output);
            }
            case PREV -> {
                return new PrevCommand(session, commandInput, user, output);
            }
            case FORWARD -> {
                return new ForwardCommand(session, commandInput, user, output);
            }
            case BACKWARD -> {
                return new BackwardCommand(session, commandInput, user, output);
            }
            default -> {
                return helperGetCommandStage2(commandInput, commandType, user);
            }
        }
    }


    /**
     * Helper to the public Factory Method. This part manages commands from stage 2
     * that require a User instance.
     * @return ICommand object.
     * @throws IllegalArgumentException if the command is not supported.
     */
    private ICommand helperGetCommandStage2(final CommandInput commandInput,
                                            final CommandType commandType, final User user) {
        switch (commandType) {
            case SWITCH_CONNECTION_STATUS -> {
                return new SwitchConnectionStatusCommand(session, commandInput, user, output);
            }
            case ADD_ALBUM -> {
                return new AddAlbumCommand(session, commandInput, user, output);
            }
            case SHOW_ALBUMS -> {
                return new ShowAlbumsCommand(session, commandInput, user, output);
            }
            case PRINT_CURRENT_PAGE -> {
                return new PrintCurrentPageCommand(session, commandInput, user, output);
            }
            case ADD_EVENT -> {
                return new AddEventCommand(session, commandInput, user, output);
            }
            case ADD_MERCH -> {
                return new AddMerchCommand(session, commandInput, user, output);
            }
            case REMOVE_ALBUM -> {
                return new RemoveAlbumCommand(session, commandInput, user, output);
            }
            case ADD_PODCAST -> {
                return new AddPodcastCommand(session, commandInput, user, output);
            }
            case SHOW_PODCASTS -> {
                return new ShowPodcastsCommand(session, commandInput, user, output);
            }
            case REMOVE_PODCAST -> {
                return new RemovePodcastCommand(session, commandInput, user, output);
            }
            case DELETE_USER -> {
                return new DeleteUserCommand(session, commandInput, user, output);
            }
            case ADD_ANNOUNCEMENT -> {
                return new AddAnnouncementCommand(session, commandInput, user, output);
            }
            case REMOVE_ANNOUNCEMENT -> {
                return new RemoveAnnouncementCommand(session, commandInput, user, output);
            }
            case CHANGE_PAGE -> {
                return new ChangePageCommand(session, commandInput, user, output);
            }
            case REMOVE_EVENT -> {
                return new RemoveEventCommand(session, commandInput, user, output);
            }
            default -> {
                return helperGetCommandStage3(commandInput, commandType, user);
            }
        }
    }


    /**
     * Helper to the public Factory Method. This part manages commands from stage 3
     * that require a User instance.
     * @return ICommand object.
     * @throws IllegalArgumentException if the command is not supported.
     */
    private ICommand helperGetCommandStage3(final CommandInput commandInput,
                                            final CommandType commandType, final User user) {
        switch (commandType) {
            case WRAPPED -> {
                return new WrappedCommand(session, commandInput, user, output);
            }
            case SUBSCRIBE -> {
                return new SubscribeCommand(session, commandInput, user, output);
            }
            case GET_NOTIFICATIONS -> {
                return new GetNotificationsCommand(session, commandInput, user, output);
            }
            case BUY_MERCH -> {
                return new BuyMerchCommand(session, commandInput, user, output);
            }
            case SEE_MERCH -> {
                return new SeeMerchCommand(session, commandInput, user, output);
            }
            case BUY_PREMIUM -> {
                return new BuyPremiumCommand(session, commandInput, user, output);
            }
            case CANCEL_PREMIUM -> {
                return new CancelPremiumCommand(session, commandInput, user, output);
            }
            case UPDATE_RECOMMENDATIONS -> {
                return new UpdateRecommendationsCommand(session, commandInput, user, output);
            }
            case AD_BREAK -> {
                return new AdBreakCommand(session, commandInput, user, output);
            }
            case PREVIOUS_PAGE -> {
                return new PreviousPageCommand(session, commandInput, user, output);
            }
            case NEXT_PAGE -> {
                return new NextPageCommand(session, commandInput, user, output);
            }
            case LOAD_RECOMMENDATIONS -> {
                return new LoadRecommendationsCommand(session, commandInput, user, output);
            }
            default -> {
                PrinterBasic printer = new PrinterBasic(output, commandInput);
                printer.print("Command " + commandInput.getCommand() + " not yet implemented.");
                throw new IllegalArgumentException("Invalid command.");
            }
        }
    }


    /**
     * Traverses the user lists from the database.
     * @return User with the requested username.
     * @throws IllegalArgumentException if there is no user with the requested username
     * in the database.
     */
    private User getUser(final CommandInput commandInput) throws IllegalArgumentException {
        User user;
        try {
            user = session.getDatabase().searchUserInDatabase(commandInput.getUsername());
        } catch (IllegalArgumentException exception) {
            PrinterBasic printer = new PrinterBasic(output, commandInput);
            printer.print("The username " + commandInput.getUsername() + " doesn't exist.");

            throw exception;
        }

        return user;
    }
}
