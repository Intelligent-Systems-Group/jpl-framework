package de.upb.cs.is.jpl.cli.exception.command;


import de.upb.cs.is.jpl.api.exception.JplException;
import de.upb.cs.is.jpl.cli.command.ICommand;


/**
 * This exception indicates that an {@link ICommand} could not be executed. It includes the reason
 * why the command could not be executed as a message.
 * 
 * @author Alexander Hetzer
 *
 */
public class CommandCannotBeExecutedException extends JplException {

   private static final long serialVersionUID = -7535938216869699612L;


   /**
    * Creates a {@link CommandCannotBeExecutedException} with the given message.
    * 
    * @param message the message of the exception
    */
   public CommandCannotBeExecutedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link CommandCannotBeExecutedException} with the given message as a wrapper for the
    * given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public CommandCannotBeExecutedException(String message, Throwable throwable) {
      super(message, throwable);
   }


}
