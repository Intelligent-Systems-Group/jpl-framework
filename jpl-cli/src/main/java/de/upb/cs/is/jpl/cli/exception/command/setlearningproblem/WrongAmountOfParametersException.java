package de.upb.cs.is.jpl.cli.exception.command.setlearningproblem;


import de.upb.cs.is.jpl.api.exception.JplException;


/**
 * This exception should be used every time parsing the {@code SetLearningProblemCommand} failed
 * because of a wrong amount of parameters.
 * 
 * @author Tanja Tornede
 *
 */
public class WrongAmountOfParametersException extends JplException {

   private static final long serialVersionUID = 6407690547777669296L;


   /**
    * Default Constructor which takes exception message as argument and call the super class
    * constructor with it.
    * 
    * @param exceptionMessage the exception to pass to super class.
    */
   public WrongAmountOfParametersException(String exceptionMessage) {
      super(exceptionMessage);
   }
}
