package de.upb.cs.is.jpl.cli.exception.command.addlearningalgorithm;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception indicates that the learning algorithm identifier given by the user is not mapped
 * to any learning algorithm in the tool.
 * 
 * @author Alexander Hetzer
 *
 */
public class UnknownLearningAlgorithmIdentifierException extends JplException {

   private static final long serialVersionUID = -1062488609013212393L;


   /**
    * Creates an {@link UnknownLearningAlgorithmIdentifierException} with the given message.
    * 
    * @param message the message of the exception
    */
   public UnknownLearningAlgorithmIdentifierException(String message) {
      super(message);
   }


   /**
    * Creates an {@link UnknownLearningAlgorithmIdentifierException} with the given message as a
    * wrapper for the given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public UnknownLearningAlgorithmIdentifierException(String message, Throwable throwable) {
      super(message, throwable);
   }


}
