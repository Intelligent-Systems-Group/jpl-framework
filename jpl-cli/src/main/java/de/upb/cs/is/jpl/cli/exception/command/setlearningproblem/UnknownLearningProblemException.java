package de.upb.cs.is.jpl.cli.exception.command.setlearningproblem;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception should be used every time a {@code ELearningProblem} cannot be identified by a
 * given identifier.
 * 
 * @author Tanja Tornede
 *
 */
public class UnknownLearningProblemException extends JplException {

   private static final long serialVersionUID = -7477146481719370484L;


   /**
    * Default Constructor which takes exception message as argument and call the super class
    * constructor with it.
    * 
    * @param exceptionMessage the exception to pass to super class.
    */
   public UnknownLearningProblemException(String exceptionMessage) {
      super(exceptionMessage);
   }
}
