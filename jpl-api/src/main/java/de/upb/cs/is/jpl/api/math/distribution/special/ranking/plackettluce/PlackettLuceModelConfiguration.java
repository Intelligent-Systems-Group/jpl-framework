package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.math.distribution.special.ASpecialDistributionConfiguration;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link ADistributionConfiguration} for the {@link PlackettLuceModel}.
 * It holds an {@link IVector} of parameters and a set of valid items over which the associated
 * {@link PlackettLuceModel} is defined.
 * 
 * Note that we support arbitrarily enumerated items. In order to keep the internal representation
 * of the according parameter vector efficient, we create an internal mapping from item ids to
 * indices, such that we can simply store the parameter value of the first item in the list
 * (ignoring the actual item id) in the first component of the parameter vector. This means that in
 * order to obtain the parameter value for a specific item (id), the
 * {@link #getParameterValueOfItem(int)} method should be used.
 * 
 * @author Alexander Hetzer
 *
 */
public class PlackettLuceModelConfiguration extends ASpecialDistributionConfiguration {

   private static final String ERROR_NO_PARAMETER_FOR_ITEM = "The given itemId '%d' is unknown and therefore has no parameter.";
   private static final String ERROR_UNEQUAL_AMOUNT_OF_ITEMS_AND_PARAMETERS = "The amount of parameters and items have to coincide!";
   private static final String ERROR_NULL_PARAMETER_VECTOR = "Parameter vector must not be initialized with null.";
   private static final String ERROR_NULL_VALID_ITEMS = "The set of valid items has to be non null.";

   private static final String DEFAULT_CONFIGURATION_FILENAME = "plackett_luce";

   private static final String PARAMETERS_NAME = "parameters";
   private static final String VALID_ITEMS_NAME = "item_set";

   private Map<Integer, Integer> itemIdsToParameterIndexMap;


   @SerializedName(PARAMETERS_NAME)
   private double[] parametersAsArray = null;

   @SerializedName(VALID_ITEMS_NAME)
   private int[] validItems = null;


   /**
    * Creates a new {@link PlackettLuceModelConfiguration} whose parameters are initialized with
    * default values.
    */
   public PlackettLuceModelConfiguration() {
      super(DEFAULT_CONFIGURATION_FILENAME);
   }


   /**
    * Creates a new {@link PlackettLuceModelConfiguration} initialized with the given parameters.
    * 
    * @param parameters the parameters to use for the initialization
    * @param validItems the set of items over which the associated {@link PlackettLuceModel} should
    *           work
    * @throws ParameterValidationFailedException if the given array of parameters is {@code null}
    */
   public PlackettLuceModelConfiguration(double[] parameters, int[] validItems) throws ParameterValidationFailedException {
      this(new DenseDoubleVector(parameters), validItems);
   }


   /**
    * Creates a new {@link PlackettLuceModelConfiguration} initialized with the given parameter
    * vector.
    * 
    * @param parameterVector the parameter vector to use for the initialization
    * @param validItems the set of items over which the associated {@link PlackettLuceModel} should
    *           work
    * @throws ParameterValidationFailedException if the given array of parameters is {@code null}
    */
   public PlackettLuceModelConfiguration(IVector parameterVector, int[] validItems) throws ParameterValidationFailedException {
      this();
      this.parametersAsArray = Arrays.copyOf(parameterVector.asArray(), parameterVector.length());
      this.validItems = Arrays.copyOf(validItems, validItems.length);
      setParameterVector(parameterVector);
      initializeItemToParameterMapping();
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (parametersAsArray == null) {
         throw new ParameterValidationFailedException(ERROR_NULL_PARAMETER_VECTOR);
      }
      if (validItems == null) {
         throw new ParameterValidationFailedException(ERROR_NULL_VALID_ITEMS);
      }
      if (validItems.length != parametersAsArray.length) {
         throw new ParameterValidationFailedException(ERROR_UNEQUAL_AMOUNT_OF_ITEMS_AND_PARAMETERS);
      }
   }


   /**
    * Initializes the mapping from items to parameter indices. Note: This method has to be called,
    * before this configuration is fully functional.
    */
   private void initializeItemToParameterMapping() {
      itemIdsToParameterIndexMap = new HashMap<>();
      for (int i = 0; i < validItems.length; i++) {
         itemIdsToParameterIndexMap.put(validItems[i], i);
      }
   }


   /**
    * Returns the parameter value assigned to the item with the given id.
    * 
    * @param itemId the id of the item which the parameter value should be returned for
    * @return the parameter value assigned to the item with the given id
    * @throws IllegalArgumentException if the given item id is unknown
    */
   public double getParameterValueOfItem(int itemId) throws IllegalArgumentException {
      if (!itemIdsToParameterIndexMap.containsKey(itemId)) {
         throw new IllegalArgumentException(String.format(ERROR_NO_PARAMETER_FOR_ITEM, itemId));
      }
      return parametersAsArray[itemIdsToParameterIndexMap.get(itemId)];
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      PlackettLuceModelConfiguration castedConfiguration = (PlackettLuceModelConfiguration) configuration;
      if (castedConfiguration.parametersAsArray != null) {
         this.parametersAsArray = Arrays.copyOf(castedConfiguration.parametersAsArray, castedConfiguration.parametersAsArray.length);
      }
      if (castedConfiguration.validItems != null) {
         this.validItems = Arrays.copyOf(castedConfiguration.validItems, castedConfiguration.validItems.length);
      }
      initializeItemToParameterMapping();
   }


   /**
    * Returns the parameter vector of this configuration.
    * 
    * @return the parameter vector of this configuration
    * @throws NullPointerException if the parameter vector is not yet initialized correctly
    */
   public DenseDoubleVector getParameterVector() {
      return new DenseDoubleVector(parametersAsArray);
   }


   /**
    * Sets the parameter vector of this configuration to the given vector.
    * 
    * @param parameterVector the new parameter vector to use
    */
   public void setParameterVector(IVector parameterVector) {
      this.parametersAsArray = parameterVector.asArray();
   }


   /**
    * Returns the parameters of the associated {@link PlackettLuceModel} as an array.
    * 
    * @return the parameters of the associated {@link PlackettLuceModel} as an array
    */
   public double[] getParametersAsArray() {
      return parametersAsArray;
   }


   /**
    * Sets the parameters to the given array of parameters.
    * 
    * @param parametersAsArray the new parameters to use
    */
   public void setParametersAsArray(double[] parametersAsArray) {
      this.parametersAsArray = parametersAsArray;
   }


   /**
    * Returns the valid items over which the associated {@link PlackettLuceModel} is defined.
    * 
    * @return the valid items over which the associated {@link PlackettLuceModel} is defined
    */
   public int[] getValidItems() {
      return validItems;
   }


   /**
    * Sets the valid items to the given array of valid items.
    * 
    * @param validItems the new valid items to use
    */
   public void setValidItems(int[] validItems) {
      this.validItems = validItems;
   }


   /**
    * Returns the number of parameters of this configuration.
    * 
    * @return the number of parameters of this configuration
    */
   public int getNumberOfParameters() {
      return getValidItems().length;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(parametersAsArray);
      result = prime * result + Arrays.hashCode(validItems);
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
      PlackettLuceModelConfiguration other = (PlackettLuceModelConfiguration) obj;
      if (!Arrays.equals(parametersAsArray, other.parametersAsArray))
         return false;
      if (!Arrays.equals(validItems, other.validItems))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETERS_NAME + StringUtils.COLON + Arrays.toString(parametersAsArray) + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + VALID_ITEMS_NAME + StringUtils.COLON + Arrays.toString(validItems);
   }


}
