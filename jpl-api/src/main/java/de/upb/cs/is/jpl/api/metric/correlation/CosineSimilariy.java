package de.upb.cs.is.jpl.api.metric.correlation;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.EMetric;


/**
 * Determines the cosine similarity between two vectors.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CosineSimilariy extends ACorrelation<IVector> {


   /**
    * Creates a new {@link CosineSimilariy}.
    */
   public CosineSimilariy() {
      super(EMetric.COSINE_SIMILARITY.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector basis, IVector compare) throws LossException {
      double similarity = compare.dotProduct(basis);
      similarity = similarity / (compare.euclideanNorm() * basis.euclideanNorm());
      return similarity;
   }


}
