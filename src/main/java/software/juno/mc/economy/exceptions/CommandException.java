package software.juno.mc.economy.exceptions;

import software.juno.mc.economy.annotations.CommandExecutor;

public class CommandException extends AppException{

    public CommandException(String message) {
        super(message);
    }

}
