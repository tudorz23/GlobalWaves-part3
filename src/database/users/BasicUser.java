package database.users;

import database.Database;
import fileio.input.UserInput;
import utils.enums.LogStatus;
import utils.enums.UserType;

public final class BasicUser extends User {
    /* Constructors */
    public BasicUser(final UserInput userInput, Database database) {
        super(userInput, database);
        this.setType(UserType.BASIC_USER);
        this.setLogStatus(LogStatus.ONLINE);
    }

    public BasicUser(final String username, final int age, final String city,
                     Database database) {
        super(username, age, city, database);
        this.setType(UserType.BASIC_USER);
        this.setLogStatus(LogStatus.ONLINE);
    }
}
