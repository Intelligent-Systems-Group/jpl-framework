package de.upb.cs.is.jpl.cli.command;


import java.util.Observable;

import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * This is the abstract command handler class, which defines common behavior and convenience methods
 * for all command handlers. Every command handler in the tool should subclass this class.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class ACommandHandler extends Observable implements ICommandHandler {

   protected SystemConfiguration systemConfiguration;
   private ICommandConfiguration defaultCommandConfiguration;


   /**
    * Creates an abstract command handler. All constructors of subclasses should call this
    * constructor.
    */
   public ACommandHandler() {
      init();
   }


   /**
    * Interprets the given command result, which was produced by the given command and extracts the
    * required information. This required information should be given as a message, which will be
    * displayed to the user later on.
    * 
    * @param command the command producing the command result
    * @param commandResult the command result to be interpreted
    * 
    * @return a message containing the interpretation result of the given command result which can
    *         be displayed to the user
    */
   public abstract String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult);


   /**
    * Initialization method which is called in the constructor of every {@link ACommandHandler}.
    * Initialization of object variables should be done here.
    */
   public abstract void init();


   /**
    * Creates a default command configuration instance fitting the command, which this handler is
    * responsible for.
    * 
    * @return the default command configuration
    */
   protected abstract ICommandConfiguration createDefaultCommandConfiguration();


   @Override
   public void interpretCommandResult(ICommand command, CommandResult commandResult) {
      String commandResultInterpretationOutput = getCommandResultInterpretationOutput(command, commandResult);
      displayMessageToUser(commandResultInterpretationOutput);
   }


   /**
    * Getter method for the default command configuration of the command, this handler is
    * responsible for.
    * 
    * @return the default command configuration
    */
   @Override
   public ICommandConfiguration getDefaultCommandConfiguration() {
      if (defaultCommandConfiguration == null) {
         defaultCommandConfiguration = createDefaultCommandConfiguration();
      }
      return defaultCommandConfiguration;
   }


   /**
    * Displays the given message to the user.
    * 
    * @param message a message to be displayed to the user
    */
   protected void displayMessageToUser(String message) {
      setChanged();
      notifyObservers(message);
   }
}
