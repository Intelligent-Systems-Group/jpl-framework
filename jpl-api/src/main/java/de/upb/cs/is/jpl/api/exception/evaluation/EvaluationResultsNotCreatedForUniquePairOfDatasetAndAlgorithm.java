package de.upb.cs.is.jpl.api.exception.evaluation;


import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;


/**
 * This exception will be thrown by the evaluation if for a set of {@link EvaluationResult}
 * generated from k folds or some number of iterations generating randomized pairs of training and
 * testing dataset formed using percentage-split of the dataset.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm extends EvaluationFailedException {


   private static final long serialVersionUID = 1L;


   /**
    * Creates a general {@link EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm}
    * exception with a given exception message.
    * 
    * @param message the message of the exception
    */
   public EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm(String message) {
      super(message);
   }


   /**
    * Creates a general {@link EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm} as a
    * wrapper with a given exception message and the given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable which should be wrapped
    */
   public EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm(String message, Throwable throwable) {
      super(message, throwable);
   }


   /**
    * Creates a general {@link EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm} as a
    * wrapper the given throwable.
    * 
    * @param throwable the throwable which should be wrapped
    */
   public EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm(Throwable throwable) {
      super(throwable);
   }
}
