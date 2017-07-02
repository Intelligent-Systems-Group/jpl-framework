package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineConfigurationData;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link PairwiseRankingLearningAlgorithm}.
 * 
 * @author Pritha Gupta
 *
 */
public class PairwiseRankingConfiguration extends AAlgorithmConfigurationWithBaseLearner {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "objectranking"
         + StringUtils.FORWARD_SLASH + "pairwise_ranking_algorithm";
   private static final String WRONG_VALUE_FOR_BASE_LEARNER = "The parameter base_learner must not be K-Nearest Neighbor or support vector machine with NUSVC as svmtype as we cannot have weight vector for them";
   private static final String WRONG_VALUE_FOR_PAIR_GENERATION_OPERATION = "The parameter typeOfPairs must order-svm or svor is %s:";
   private static final String WRONG_VALUE_FOR_LAMBDA = "The parameter lambda should not be negetive is %f:";

   /**
    * Defining the order-svm method to generate classification instances with objects ranked before
    * some threshold are classified as 1 else -1.
    */
   public static final String ORDER_SVM = "order-svm";
   /**
    * Defining the svor method to generate classification instances with every possible pair of
    * ranked objects are seen and for each pair one classifier instance is generated.
    */
   public static final Object SVOR = "svor";
   private static final String METHOD_TYPE = "method_type";
   private static final String LAMBDA = "lambda";

   @SerializedName(METHOD_TYPE)
   private String methodTypeToGenerateClassificationDataset = StringUtils.EMPTY_STRING;

   @SerializedName(LAMBDA)
   private double lambdaValue = Double.MAX_VALUE;


   /**
    * Creates a new configuration for the {@link PairwiseRankingConfiguration}.
    */
   public PairwiseRankingConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerIsClassifier();
      if (baseLearnerAlgorithm instanceof SupportVectorMachineClassification) {
         SupportVectorMachineConfiguration configuration = (SupportVectorMachineConfiguration) baseLearnerAlgorithm
               .getAlgorithmConfiguration();
         if (configuration.getSvmType() == SupportVectorMachineConfigurationData.NU_SVC) {
            throw new ParameterValidationFailedException(WRONG_VALUE_FOR_BASE_LEARNER);
         }
      }
      if (ebaseLearner.equals(EBaseLearner.KNEAREST_NEIGHBOUR)) {
         throw new ParameterValidationFailedException(WRONG_VALUE_FOR_BASE_LEARNER);
      }


      validateMethodTypeToGenerateClasssificationDataset();
   }


   /**
    * Validate the method to be used to generate the classification {@link BaselearnerDataset}.
    * 
    * @throws ParameterValidationFailedException if the method type of threshold for
    */
   public void validateMethodTypeToGenerateClasssificationDataset() throws ParameterValidationFailedException {
      if (!(methodTypeToGenerateClassificationDataset.equals(SVOR) || methodTypeToGenerateClassificationDataset.equals(ORDER_SVM))) {
         throw new ParameterValidationFailedException(
               String.format(WRONG_VALUE_FOR_PAIR_GENERATION_OPERATION, methodTypeToGenerateClassificationDataset));
      }
      if (methodTypeToGenerateClassificationDataset.equals(ORDER_SVM) && (Double.compare(lambdaValue, 1.0) != 0)) {
         throw new ParameterValidationFailedException(String.format(WRONG_VALUE_FOR_LAMBDA, lambdaValue));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      PairwiseRankingConfiguration castedConfiguration = (PairwiseRankingConfiguration) configuration;
      if (!castedConfiguration.methodTypeToGenerateClassificationDataset.isEmpty()) {
         this.methodTypeToGenerateClassificationDataset = castedConfiguration.methodTypeToGenerateClassificationDataset;
      }
      if (Double.compare(castedConfiguration.lambdaValue, Double.MAX_VALUE) != 0) {
         this.lambdaValue = castedConfiguration.lambdaValue;
      }

   }


   /**
    * Returns the type of method to be used to generate pairs.
    * 
    * @return the type of method to be used to generate pairs
    */
   public String getMethodTypeToGenerateClassificationDataset() {
      return methodTypeToGenerateClassificationDataset;
   }


   /**
    * Returns the lambda, i.e. the regularization hyper-parameter for order-svm.
    * 
    * @return the regularization hyper-parameter
    */
   public double getLambdaValue() {
      return lambdaValue;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(lambdaValue);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result
            + ((methodTypeToGenerateClassificationDataset == null) ? 0 : methodTypeToGenerateClassificationDataset.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      boolean valid = false;
      if (super.equals(secondObject) && secondObject instanceof PairwiseRankingConfiguration) {
         PairwiseRankingConfiguration castedObject = PairwiseRankingConfiguration.class.cast(secondObject);
         if (methodTypeToGenerateClassificationDataset.equals(castedObject.methodTypeToGenerateClassificationDataset)) {
            if (methodTypeToGenerateClassificationDataset == ORDER_SVM) {
               valid = Double.compare(lambdaValue, castedObject.lambdaValue) == 0;
            } else
               valid = true;
         }
      }
      return valid;

   }


   @Override
   public String toString() {
      return METHOD_TYPE + StringUtils.COLON + methodTypeToGenerateClassificationDataset + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + LAMBDA + StringUtils.COLON + lambdaValue + super.toString();
   }


}
