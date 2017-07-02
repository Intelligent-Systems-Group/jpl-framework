package de.upb.cs.is.jpl.api.dataset.instanceranking;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * A instance specified for problems of instance ranking. Each instances holds a list of its context
 * features in form of a array of double values and an optional rating.
 *
 * @author Sebastian Gottschalk
 */
public class InstanceRankingInstance extends AInstance<double[], NullType, Integer> {
   private double[] featureList;


   /**
    * Creates a new instance by setting a feature list and a rating.
    * 
    * @param contextID context id of the instance
    * @param featureList context feature list of the instance
    * @param rating rating of the instance
    */
   public InstanceRankingInstance(int contextID, double[] featureList, Integer rating) {
      setContextId(contextID);
      setContextFeatureVector(featureList);
      setRating(rating);
   }


   @Override
   public double[] getContextFeatureVector() {
      return featureList;

   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      featureList = contextFeatureVector;

   }


   @Override
   public NullType getItemFeatureVectors() {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setItemFeatureVector(NullType itemFeatureVectors) {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean equals(Object secondInstance) {
      if (secondInstance instanceof InstanceRankingInstance) {
         return rating.equals(InstanceRankingInstance.class.cast(secondInstance).getRating())
               && Arrays.equals(featureList, InstanceRankingInstance.class.cast(secondInstance).getContextFeatureVector());
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = 1;
      hashCode = 31 * hashCode + (rating == null ? 0 : rating.hashCode());
      hashCode = 31 * hashCode + (featureList == null ? 0 : Arrays.hashCode(featureList));
      return hashCode;
   }

}
