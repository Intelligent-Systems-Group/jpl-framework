package de.upb.cs.is.jpl.cli.command;


import de.upb.cs.is.jpl.cli.core.IObservable;


/**
 * This interface defines the common functionality of a command handler in order to be compatible
 * with the system. Most importantly every command handler needs to be able to create a command
 * instance, given an according command configuration and to interpret a {@link CommandResult}
 * created by a command, which was created by this handler.
 * 
 * @author Alexander Hetzer
 *
 */
public interface ICommandHandler extends IObservable {

   /**
    * Handles the given {@link ICommandConfiguration} and creates the correct {@link ICommand}.
    * 
    * @param commandConfiguration the command configuration to create the command for
    * 
    * @return an {@code ICommand} initialized from the command configuration
    */
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration);


   /**
    * Interprets the given command result, which was produced by the given {@link ICommand} and
    * extracts the required information to notify the observers over this change.
    * 
    * @param command the command producing the command result
    * @param commandResult the command result to be interpreted
    */
   public void interpretCommandResult(final ICommand command, final CommandResult commandResult);


   /**
    * Returns the default {@link ICommandConfiguration} of the {@link ICommand}, this handler is
    * responsible for.
    * 
    * @return the default {@link ICommandConfiguration} of the {@link ICommand}, this handler is
    *         responsible for
    */
   public ICommandConfiguration getDefaultCommandConfiguration();
}
