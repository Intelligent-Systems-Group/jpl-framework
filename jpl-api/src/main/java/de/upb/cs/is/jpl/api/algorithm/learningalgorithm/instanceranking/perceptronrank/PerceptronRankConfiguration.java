package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration stores all parameters of the {@link PerceptronRankLearningAlgorithm}.
 *
 * @author Sebastian Gottschalk
 */
public class PerceptronRankConfiguration extends AAlgorithmConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "instanceranking"
         + StringUtils.FORWARD_SLASH + "perceptron_rank";
   private static final String WRONG_VALUE_FOR_K = "The parameter k must be a positive integer.";

   private static final String TO_STRING_OUTPUT = "(k: %s)";


   private int k = Integer.MAX_VALUE;


   /**
    * Creates a new instance of the PRankAlgorithmConfiguration.
    */
   public PerceptronRankConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (this.k < 0) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_K);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      PerceptronRankConfiguration prankConfiguration = (PerceptronRankConfiguration) configuration;
      if (prankConfiguration.k != Integer.MAX_VALUE) {
         this.k = prankConfiguration.k;
      }
   }


   /**
    * Returns the number of categories.
    *
    * @return number of different categories
    */
   public int getK() {
      return this.k;
   }


   /**
    * Sets the number of different categories.
    *
    * @param k number of different categories
    */
   public void setK(int k) {
      this.k = k;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof PerceptronRankConfiguration) {
         PerceptronRankConfiguration prankLearningModel = (PerceptronRankConfiguration) secondObject;
         if (this.k == prankLearningModel.k) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * k;
      return hashCode;
   }


   @Override
   public String toString() {
      return String.format(TO_STRING_OUTPUT, k);
   }

}