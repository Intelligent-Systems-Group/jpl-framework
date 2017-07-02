package de.upb.cs.is.jpl.cli.exception.command.loadlearningalgorithms;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that an algorithm could not be loaded properly from the json or command
 * line definition.
 * 
 * @author Alexander Hetzer
 *
 */
public class LoadLearningAlgorithmsFailedException extends JplException {

   private static final long serialVersionUID = -4996209165492270921L;


   /**
    * Creates a {@link LoadLearningAlgorithmsFailedException} with the given message.
    * 
    * @param message the message of the exception
    */
   public LoadLearningAlgorithmsFailedException(String message) {
      super(message);
   }


   /**
    * Creates a {@link LoadLearningAlgorithmsFailedException} with the given message as a wrapper
    * for the given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public LoadLearningAlgorithmsFailedException(String message, Throwable throwable) {
      super(message, throwable);
   }


}
