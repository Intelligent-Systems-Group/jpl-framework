package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for the
 * {@link OrdinalClassificationReductionFramework}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationReductionFrameworkConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN = "The given cost matrix identifier <%s> is unknown!";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "ordinalclassification"
         + StringUtils.FORWARD_SLASH + "ordinal_classification_reduction_framework";

   private static final String COST_MATRIX_TYPE_IDENTIFIER = "cost_matrix_type_identifier";

   @SerializedName(COST_MATRIX_TYPE_IDENTIFIER)
   protected String costMatrixTypeIdentifier = StringUtils.EMPTY_STRING;


   /**
    * Creates a default configuration for the {@link OrdinalClassificationReductionFramework}.
    */
   public OrdinalClassificationReductionFrameworkConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerIsClassifier();
      assertCorrectCostMatrixIdentifier();
   }


   /**
    * Checks weather the cost matrix identifier is known.
    * 
    * @throws ParameterValidationFailedException if the cost matrix identifier is unknown
    */
   private void assertCorrectCostMatrixIdentifier() throws ParameterValidationFailedException {
      if (ECostMatrixType.getECostMatrixTypeByIdentifier(costMatrixTypeIdentifier) == null) {
         throw new ParameterValidationFailedException(String.format(ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN, costMatrixTypeIdentifier));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      OrdinalClassificationReductionFrameworkConfiguration logisticClassificationConfiguration = (OrdinalClassificationReductionFrameworkConfiguration) configuration;
      copyCostMatrixIdentifier(logisticClassificationConfiguration);
   }


   /**
    * Copies the cost matrix identifier of the given
    * {@link OrdinalClassificationReductionFrameworkConfiguration}.
    * 
    * @param configuration the configuration to copy from
    */
   private void copyCostMatrixIdentifier(OrdinalClassificationReductionFrameworkConfiguration configuration) {
      if (!configuration.costMatrixTypeIdentifier.equals(StringUtils.EMPTY_STRING)) {
         this.costMatrixTypeIdentifier = configuration.costMatrixTypeIdentifier;
      }
   }


   /**
    * Returns the identifier of the cost matrix.
    * 
    * @return the identifier of the cost matrix
    */
   public String getCostMatrixTypeIdentifier() {
      return costMatrixTypeIdentifier;
   }


   /**
    * Sets the identifier of the cost matrix.
    * 
    * @param costMatrixTypeIdentifier the identifier of the cost matrix to set
    */
   public void setCostMatrixTypeIdentifier(String costMatrixTypeIdentifier) {
      this.costMatrixTypeIdentifier = costMatrixTypeIdentifier;
   }


   /**
    * Returns the entry of the cost matrix at position (i,j).
    * 
    * @param i the first coordinate of the cost matrix
    * @param j the second coordinate of the cost matrix
    * 
    * @return the entry of the cost matrix at the given position
    * @throws TrainModelsFailedException if the cost matrix identifier defined in this configuration
    *            is unknown
    */
   public double getCostMatrixEntry(int i, int j) throws TrainModelsFailedException {
      ECostMatrixType costMatrixType = ECostMatrixType.getECostMatrixTypeByIdentifier(costMatrixTypeIdentifier);

      if (costMatrixType == null) {
         throw new TrainModelsFailedException(ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN);
      }

      return costMatrixType.computeCostMatrixEntry(i, j);
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      OrdinalClassificationReductionFrameworkConfiguration other = (OrdinalClassificationReductionFrameworkConfiguration) obj;
      if (costMatrixTypeIdentifier == null) {
         if (other.costMatrixTypeIdentifier != null)
            return false;
      } else if (!costMatrixTypeIdentifier.equals(other.costMatrixTypeIdentifier))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((costMatrixTypeIdentifier == null) ? 0 : costMatrixTypeIdentifier.hashCode());
      return result;
   }


   @Override
   public String toString() {
      return COST_MATRIX_TYPE_IDENTIFIER + StringUtils.COLON + costMatrixTypeIdentifier + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + super.toString();
   }

}
