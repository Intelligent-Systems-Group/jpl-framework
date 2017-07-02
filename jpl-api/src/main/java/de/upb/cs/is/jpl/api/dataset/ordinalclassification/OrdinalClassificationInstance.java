package de.upb.cs.is.jpl.api.dataset.ordinalclassification;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This instance is used to store a instance for ordinal classification.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationInstance extends AInstance<double[], NullType, Double> {

   private double[] featureVector;


   /**
    * Creates an {@link OrdinalClassificationDataset}.
    * 
    * @param featureVector the feature vector of the instance
    * @param rating the correct result of the instance
    */
   public OrdinalClassificationInstance(double[] featureVector, double rating) {
      super();
      this.rating = rating;
      this.featureVector = featureVector;
   }


   @Override
   public double[] getContextFeatureVector() {
      return this.featureVector;
   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      this.featureVector = Arrays.copyOf(contextFeatureVector, contextFeatureVector.length);
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
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(featureVector);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      OrdinalClassificationInstance other = (OrdinalClassificationInstance) obj;
      if (!Arrays.equals(featureVector, other.featureVector))
         return false;
      return true;
   }


}
