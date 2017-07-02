package de.upb.cs.is.jpl.api.metric.crossentropy;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableDoubleDoubleMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * This class represents the cross-entropy error used for logistic regression (as defined in the
 * book Learning From Data by Hsuan-Tien Lin, Yaser Abu-Mostafa and Malik Magdon-Ismail). In order
 * to avoid the risk of running into a double over- or underflow by passing too large or small
 * numbers to {@link Math#exp(double)}, slight numerical incorrectness is introduced for such
 * problematic arguments.
 * 
 * @author Alexander Hetzer
 *
 */
public class CrossEntropyError extends AAverageDecomposableDoubleDoubleMetric<EmptyMetricConfiguration> {

   /**
    * Creates a new {@link CrossEntropyError}.
    */
   public CrossEntropyError() {
      super(EMetric.CROSS_ENTROPY_ERROR.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(Double expectedRating, Double predictedRating) throws LossException {
      double exponentialArgument = -expectedRating * predictedRating;
      // make sure we do not cause a double overflow in Math.exp by introducing slight numerical
      // incorrectness
      if (exponentialArgument > 709.78) {
         return exponentialArgument;
      }
      // make sure we do not cause a double underflow in Math.exp by introducing slight numerical
      // incorrectness
      if (exponentialArgument <= -744.44) {
         return 0.0;
      }

      return Math.log(1 + Math.exp(exponentialArgument));
   }


   @Override
   protected EmptyMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }

}
