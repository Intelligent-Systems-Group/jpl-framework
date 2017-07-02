package de.upb.cs.is.jpl.cli.command;


/**
 * This is the abstract command class implementing the {@link ICommand}, capable of storing the
 * result of the command. It defines common behavior and some convenience methods for all commands.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class ACommand implements ICommand {

   private final String commandIdentifier;
   private CommandResult commandResult;


   /**
    * Creates a {@link ACommand} with the given command identifier.
    * 
    * @param commandIdentifier the identifier of the command
    */
   public ACommand(String commandIdentifier) {
      this.commandIdentifier = commandIdentifier;
   }


   /**
    * Overwrites the current command result of this command with the given one.
    * 
    * @param commandResult the new command result
    */
   protected void setCommandResult(CommandResult commandResult) {
      this.commandResult = commandResult;
   }


   /**
    * Returns the command result of this command.
    * 
    * @return the command result of this command
    */
   public CommandResult getCommandResult() {
      return commandResult;
   }


   @Override
   public String toString() {
      return commandIdentifier;
   }


}
