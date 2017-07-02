package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple;


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
 * Tests for the {@link SimpleOrdinalClassification}.
 * 
 * @author Tanja Tornede
 *
 */
public class SimpleOrdinalClassificationTest extends AOrdinalClassificationTest {

   private static final String REFLECTION_A_ALGORITHM_CONFIGURATION_WITH_BASE_LEARNER_CONFIGURATION_ERROR_BASE_LEARNER_IS_NOT_RETURNING_PROBABILITY = "ERROR_BASE_LEARNER_IS_NOT_RETURNING_PROBABILITY";

   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE = "learning_rate";
   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE = "0.1";


   /**
    * Creates a test for the {@link SimpleOrdinalClassification}.
    */
   public SimpleOrdinalClassificationTest() {
      super();
   }


   @Override
   public void setupTest() {
      super.setupTest();
      RandomGenerator.getRNG().setSeed(1234);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new SimpleOrdinalClassification();
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<EBaseLearner> regressionBaselearners = EBaseLearner.getProbabilityReturningBaseLearners();

      List<JsonObject> parameterList = new ArrayList<>();
      for (EBaseLearner baselearner : regressionBaselearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(baselearner.getBaseLearnerIdentifier(), new JsonObject()));
         parameterList.add(baselearnerDefinition);
      }

      JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(
            EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier(),
            JsonUtils.createJsonObjectFromKeyAndValue(CONFIGURATION_PARAMETER_LEARNING_RATE, CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE)));
      parameterList.add(baselearnerDefinition);

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<EBaseLearner> classificationBaselearners = EBaseLearner.getClassifiersEBaseLearners();
      String exceptionString = TestUtils.getStringByReflectionSafely(AAlgorithmConfigurationWithBaseLearner.class,
            REFLECTION_A_ALGORITHM_CONFIGURATION_WITH_BASE_LEARNER_CONFIGURATION_ERROR_BASE_LEARNER_IS_NOT_RETURNING_PROBABILITY);

      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      for (EBaseLearner baselearner : classificationBaselearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(baselearner.getBaseLearnerIdentifier(), new JsonObject()));
         parameterList.add(Pair.of(String.format(exceptionString, baselearner.getBaseLearnerIdentifier()), baselearnerDefinition));
      }

      return parameterList;
   }

}
