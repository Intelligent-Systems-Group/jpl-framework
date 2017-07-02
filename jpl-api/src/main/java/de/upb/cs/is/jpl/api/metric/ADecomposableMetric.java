package de.upb.cs.is.jpl.api.metric;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;


/**
 * The abstract implementation for instance wise decomposable metric. The instance wise decomposable
 * metric can have a loss on pair of single rating and these losses on single rating can be
 * aggregated by iterating over the pair of predicted and the expected rating.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the generic type extending AEvaluationMetricConfiguration, configuration
 *           associated with the Evaluation Metric class
 * @param <INPUT> the input type of the metric
 * @param <OUTPUT> the result type of the metric, i.e. Double
 * 
 */
public abstract class ADecomposableMetric<CONFIG extends AMetricConfiguration, INPUT, OUTPUT> extends AMetric<CONFIG, INPUT, OUTPUT> {

   /**
    * Creates new {@link ADecomposableMetric} with the default metric configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public ADecomposableMetric(String metricIdentifer) {
      super(metricIdentifer);
   }


   @Override
   public List<OUTPUT> getLossForRatings(List<INPUT> expectedRatings, List<INPUT> predictedRatings) throws LossException {
      assertListsAreNotNullAndHaveSameSize(expectedRatings, predictedRatings);
      List<OUTPUT> losses = new ArrayList<>();
      for (int i = 0; i < expectedRatings.size(); i++) {
         losses.add(getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i)));
      }
      return losses;
   }

}
