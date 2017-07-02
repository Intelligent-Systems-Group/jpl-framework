package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class encapsulates the required configuration parameters required for the
 * {@link KNearestNeighborClassification} BaseLearner.
 * 
 * @author Sebastian Osterbrink
 */
public class KNearestNeighborConfiguration extends AAlgorithmConfiguration {


   private static final String NOT_A_POSITIVE_VALUE = "The parameter k (number of neighbors checked) is not a positive value!"
         + " You need to consider at least one neighbor for neighbor-based learning.";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "knearestneighbor" + StringUtils.FORWARD_SLASH + "k_nearest_neighbors";
   private static final String K = "k";
   @SerializedName(K)
   protected int numberOfNeighbors = Integer.MAX_VALUE;


   /**
    * Creates a new {@link KNearestNeighborConfiguration} object with the default configuration
    * loaded.
    */
   public KNearestNeighborConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (numberOfNeighbors <= 0) {
         throw new ParameterValidationFailedException(NOT_A_POSITIVE_VALUE);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      KNearestNeighborConfiguration castedConfiguration = (KNearestNeighborConfiguration) configuration;
      if (castedConfiguration.numberOfNeighbors < Integer.MAX_VALUE) {
         this.numberOfNeighbors = castedConfiguration.numberOfNeighbors;
      }
   }


   /**
    * Returns the selected number of neighbors.
    * 
    * @return the currently selected value for the number of neighbors (k)
    */
   public int getNumberOfNeighbors() {
      return numberOfNeighbors;
   }


   /**
    * Sets the selected number of neighbors.
    * 
    * @param k the new selection for the number of neighbors (k)
    */
   public void setNumberOfNeighbors(int k) {
      this.numberOfNeighbors = k;
   }


   @Override
   public String toString() {
      return K + StringUtils.COLON + numberOfNeighbors;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + numberOfNeighbors;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof KNearestNeighborConfiguration))
         return false;
      KNearestNeighborConfiguration other = (KNearestNeighborConfiguration) obj;
      if (numberOfNeighbors != other.numberOfNeighbors)
         return false;
      return true;
   }


}
