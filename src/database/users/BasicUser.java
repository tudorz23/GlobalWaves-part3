package database.users;

import fileio.input.UserInput;
import utils.enums.LogStatus;
import utils.enums.UserType;

public final class BasicUser extends User {
    /* Constructors */
    public BasicUser(final UserInput userInput) {
        super(userInput);
        this.setType(UserType.BASIC_USER);
        this.setLogStatus(LogStatus.ONLINE);
    }

    public BasicUser(final String username, final int age, final String city) {
        super(username, age, city);
        this.setType(UserType.BASIC_USER);
        this.setLogStatus(LogStatus.ONLINE);
    }
}
