package de.upb.cs.is.jpl.api.exception.evaluation;


/**
 * This exception indicates for cross validation evaluations pairs of tests and training cannot be
 * created.
 * 
 * @author Pritha Gupta
 *
 */
public class TrainTestDatasetPairsNotCreated extends EvaluationFailedException {


   private static final long serialVersionUID = -854232325324390543L;


   /**
    * Creates a new {@link TrainTestDatasetPairsNotCreated} with a given exception message.
    * 
    * @param message the message of the exception
    */
   public TrainTestDatasetPairsNotCreated(String message) {
      super(message);
   }


   /**
    * Creates a new {@link TrainTestDatasetPairsNotCreated} as a wrapper with a given exception
    * message and the given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable which should be wrapped
    */
   public TrainTestDatasetPairsNotCreated(String message, Throwable throwable) {
      super(message, throwable);
   }


   /**
    * Creates a general {@link TrainTestDatasetPairsNotCreated} as a wrapper the given throwable.
    * 
    * @param throwable the throwable which should be wrapped
    */
   public TrainTestDatasetPairsNotCreated(Throwable throwable) {
      super(throwable);
   }

}
