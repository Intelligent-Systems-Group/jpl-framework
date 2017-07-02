package de.upb.cs.is.jpl.cli.command;


/**
 * This interface defines the functionality which each command needs to support in order to be
 * compatible with the system. Most importantly it defines methods which allow the system to check
 * if a command can be executed, to obtain a failure reason if it cannot be executed and finally to
 * execute it.
 * 
 * @author Alexander Hetzer
 *
 */
public interface ICommand {

   /**
    * Checks if this command can be executed. Should return {@code false} if either the input for
    * the command is incorrect or special prerequisites for the execution are not fulfilled.
    * 
    * @return {@code true}, if the command can be executed otherwise {@code false}, if either the
    *         input is wrong or a prerequisite is not fulfilled
    */
   public boolean canBeExecuted();


   /**
    * Executes this command.
    * 
    * @return the result of the execution in the form of a {@link CommandResult}
    */
   public CommandResult executeCommand();


   /**
    * This methods returns the appropriate failure reason, if the command cannot be executed. If
    * {@link #canBeExecuted()} returns {@code false}, this method should return a proper reason.
    * 
    * @return the reason why the command cannot be executed
    */
   public String getFailureReason();


   /**
    * Undos all changes made by this command.
    */
   public void undo();
}
