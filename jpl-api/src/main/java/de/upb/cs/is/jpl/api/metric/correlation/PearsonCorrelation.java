package de.upb.cs.is.jpl.api.metric.correlation;


import java.util.HashMap;
import java.util.Map;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.EMetric;


/**
 * Determines the Pearson Correllation of two vectors.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class PearsonCorrelation extends ACorrelation<IVector> {
   private Map<IVector, Double> averages = new HashMap<>();


   /**
    * Creates a new {@link PearsonCorrelation}.
    */
   public PearsonCorrelation() {
      super(EMetric.PEARSON_CORRELATION.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector basis, IVector compare) throws LossException {
      if (!averages.containsKey(basis)) {
         averages.put(basis, basis.average());
      }
      if (!averages.containsKey(compare)) {
         averages.put(compare, compare.average());
      }
      double basisAverage = averages.get(basis);
      double compareAverage = averages.get(basis);

      double result = 0;
      for (int i = 0; i < basis.length(); i++) {
         if (basis.getValue(i) > 0 && compare.getValue(i) > 0) {
            result += (basis.getValue(i) - basisAverage) * (compare.getValue(i) - compareAverage);
         }
      }
      double dividend1 = 0;
      double dividend2 = 0;
      for (int i = 0; i < basis.length(); i++) {
         if (basis.getValue(i) > 0 && compare.getValue(i) > 0) {
            dividend1 += Math.pow(basis.getValue(i) - basisAverage, 2);
            dividend2 += Math.pow(compare.getValue(i) - compareAverage, 2);
         }
      }
      result = result / Math.sqrt(dividend1 * dividend2);
      return result;


   }


}
