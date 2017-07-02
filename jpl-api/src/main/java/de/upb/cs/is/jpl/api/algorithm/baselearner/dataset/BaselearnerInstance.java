package de.upb.cs.is.jpl.api.algorithm.baselearner.dataset;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * An instance class which is intended to be used in combination with the {@link BaselearnerDataset}
 * . It allows storing a double valued feature vector and a real valued result.
 * 
 * @author Alexander Hetzer
 *
 */
public class BaselearnerInstance extends AInstance<double[], NullType, Double> {

   private double[] featureVector;
   private double weight;


   /**
    * Creates an instance with a feature vector only, but no rating.
    * 
    * @param contextFeatureVector the feature vector to initialize the instance with
    */
   public BaselearnerInstance(double[] contextFeatureVector) {
      this(contextFeatureVector, Double.MIN_VALUE, 1.0);
   }


   /**
    * Creates an instance with a feature vector and a weight, but no rating.
    * 
    * @param contextFeatureVector the feature vector to initialize the instance with
    * @param weight the weight of the feature vector to add
    */
   public BaselearnerInstance(double weight, double[] contextFeatureVector) {
      this(contextFeatureVector, Double.MIN_VALUE, weight);
   }


   /**
    * Creates an instance with both a feature vector and a rating.
    * 
    * @param contextFeatureVector the feature vector of this instance
    * @param rating the correct prediction for this instance
    * @param weight the weight of the instance
    */
   public BaselearnerInstance(double[] contextFeatureVector, double rating, double weight) {
      this.rating = rating;
      this.featureVector = Arrays.copyOf(contextFeatureVector, contextFeatureVector.length);
      this.weight = weight;
   }


   /**
    * Creates an instance with both a feature vector and a rating.
    * 
    * @param contextFeatureVector the feature vector of this instance
    * @param rating the correct prediction for this instance
    */
   public BaselearnerInstance(double[] contextFeatureVector, double rating) {
      this(contextFeatureVector, rating, 1.0);
   }


   @Override
   public double[] getContextFeatureVector() {
      return featureVector;
   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      this.featureVector = contextFeatureVector;
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
      long temp;
      temp = Double.doubleToLongBits(weight);
      result = prime * result + (int) (temp ^ (temp >>> 32));
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
      BaselearnerInstance other = (BaselearnerInstance) obj;
      if (!Arrays.equals(featureVector, other.featureVector))
         return false;
      if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
         return false;
      return true;
   }


   /**
    * Returns the feature vector of this instance.
    * 
    * @return the feature vector of this instance
    */
   public double[] getFeatureVector() {
      return featureVector;
   }


   /**
    * Sets the feature vector of this instance to the given vector.
    * 
    * @param featureVector the feature vector to set
    */
   public void setFeatureVector(double[] featureVector) {
      this.featureVector = featureVector;
   }


   /**
    * Returns the weight associated with this instance.
    * 
    * @return the weight associated with this instance
    */
   public double getWeight() {
      return weight;
   }


   /**
    * Sets the weight of this instance to the given value.
    * 
    * @param weight the weight to assign to this instance
    */
   public void setWeight(double weight) {
      this.weight = weight;
   }


}
