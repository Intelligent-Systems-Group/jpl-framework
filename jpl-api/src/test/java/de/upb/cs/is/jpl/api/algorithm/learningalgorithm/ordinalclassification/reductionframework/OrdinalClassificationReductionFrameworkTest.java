package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.BaseLearnerJsonObjectCreator;
import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.AOrdinalClassificationTest;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for the {@link OrdinalClassificationReductionFramework}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationReductionFrameworkTest extends AOrdinalClassificationTest {

   private static final String CONFIGURATION_PARAMETER_WRONG_COST_MATRIX_TYPE_CLASSIFICATION = "classification";
   private static final String CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_ABSOLUTE_COST_MATRIX = "absolute_cost_matrix";
   private static final String CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_CLASSIFICATION_MATRIX = "classification_matrix";
   private static final String REFLECTION_ORIDNAL_CLASSIFICATION_REDUCTION_FRAMEWORK_ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN = "ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN";
   private static final String CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_IDENTIFIER = "cost_matrix_type_identifier";


   private static final String REFLECTION_A_ALGORITHM_CONFIGURATION_WITH_BASE_LEARNER_CONFIGURATION_ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER = "ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER";

   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE = "threshold";
   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE = "0.6";


   /**
    * Creates a test for the {@link OrdinalClassificationReductionFramework}.
    */
   public OrdinalClassificationReductionFrameworkTest() {
      super();
   }


   @Override
   public void setupTest() {
      super.setupTest();
      RandomGenerator.getRNG().setSeed(1234);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new OrdinalClassificationReductionFramework();
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> parameterList = new ArrayList<>();

      List<EBaseLearner> classificationBaselearners = EBaseLearner.getClassifiersEBaseLearners();
      String[] parameterNames = { CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_IDENTIFIER,
            CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_IDENTIFIER };
      String[] parameterValues = { CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_CLASSIFICATION_MATRIX,
            CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_ABSOLUTE_COST_MATRIX };
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], parameterValues[i]));
      }

      for (EBaseLearner baselearner : classificationBaselearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(baselearner.getBaseLearnerIdentifier(), new JsonObject()));
         parameterList.add(baselearnerDefinition);
      }
      JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(
            EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier(),
            JsonUtils.createJsonObjectFromKeyAndValue(CONFIGURATION_PARAMETER_LEARNING_RATE, CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE)));
      parameterList.add(baselearnerDefinition);

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();

      String[] parameterNames = { CONFIGURATION_PARAMETER_COST_MATRIX_TYPE_IDENTIFIER };
      String[] parameterValues = { CONFIGURATION_PARAMETER_WRONG_COST_MATRIX_TYPE_CLASSIFICATION };
      String[] parameterExceptionMessages = { String.format(
            TestUtils.getStringByReflectionSafely(OrdinalClassificationReductionFrameworkConfiguration.class,
                  REFLECTION_ORIDNAL_CLASSIFICATION_REDUCTION_FRAMEWORK_ERROR_GIVEN_COST_MATRIX_IDENTIFIER_UNKNOWN),
            CONFIGURATION_PARAMETER_WRONG_COST_MATRIX_TYPE_CLASSIFICATION) };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(parameterExceptionMessages[i],
               JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      List<EBaseLearner> regressionBaselearners = EBaseLearner.getRegressionEBaseLearners();
      String exceptionString = TestUtils.getStringByReflectionSafely(AAlgorithmConfigurationWithBaseLearner.class,
            REFLECTION_A_ALGORITHM_CONFIGURATION_WITH_BASE_LEARNER_CONFIGURATION_ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER);
      for (EBaseLearner baselearner : regressionBaselearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(baselearner.getBaseLearnerIdentifier(), new JsonObject()));
         parameterList.add(Pair.of(String.format(exceptionString, baselearner.getBaseLearnerIdentifier()), baselearnerDefinition));
      }

      return parameterList;
   }

}
