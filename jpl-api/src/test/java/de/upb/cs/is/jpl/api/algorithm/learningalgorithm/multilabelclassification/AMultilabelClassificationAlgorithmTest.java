package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.BaseLearnerJsonObjectCreator;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDatasetParser;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class is the base class for all tests of multilabel classification learning algorithms.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AMultilabelClassificationAlgorithmTest extends ALearningAlgorithmTest<double[], NullType, SparseDoubleVector> {

   private static final Logger logger = LoggerFactory.getLogger(AMultilabelClassificationAlgorithmTest.class);

   private static final String ERROR_COULD_NOT_CREATE_DATASET = "Could not create test dataset: %s.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "multilabelclassification" + File.separator;

   private static final String EMOTIONS_DATASET_NAME = "emotions/emotions-arff.gprf";

   protected static final String CONFIGURATION_PARAMETER_THRESHOLD = "threshold";
   protected static final String CONFIGURATION_PARAMETER_THRESHOLD_VALUE = "0.6";

   private static final String REFLECTION_WRONG_IDENTIFIER_FOR_BASE_LEARNER = "ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER";

   protected MultilabelClassificationDataset correctDataset;


   /**
    * Creates a {@link AMultilabelClassificationAlgorithmTest}.
    */
   public AMultilabelClassificationAlgorithmTest() {
      this(StringUtils.EMPTY_STRING);
   }


   /**
    * Creates a {@link AMultilabelClassificationAlgorithmTest} with the additional resource path
    * given.
    * 
    * @param additionalResourcePath the additional resource path to add
    */
   public AMultilabelClassificationAlgorithmTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
      MultilabelClassificationDatasetParser datasetParser = new MultilabelClassificationDatasetParser();
      try {
         datasetParser.parse(new DatasetFile(new File(getTestRessourcePathFor(EMOTIONS_DATASET_NAME))));
         correctDataset = datasetParser.getDataset();
      } catch (ParsingFailedException e) {
         logger.error(e.getMessage(), e);
         Assert.fail(String.format(ERROR_COULD_NOT_CREATE_DATASET, EMOTIONS_DATASET_NAME));
      }

   }


   @Override
   @Before
   public void setupTest() {
      super.setupTest();
      RandomGenerator.getRNG().setSeed(29562389457893L);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDatasets = new ArrayList<>();
      wrongDatasets.add(new DefaultAbsoluteDataset());
      wrongDatasets.add(new ObjectRankingDataset());
      return wrongDatasets;
   }


   @Override
   public List<IDataset<double[], NullType, SparseDoubleVector>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, SparseDoubleVector>> correctDatasets = new ArrayList<>();
      correctDatasets.add(correctDataset);
      return correctDatasets;
   }


   @Override
   protected boolean areRatingListsEqual(List<SparseDoubleVector> firstList, List<SparseDoubleVector> secondList) {
      if (firstList == null || secondList == null) {
         throw new IllegalArgumentException(ERROR_UNINITIALIZED_LIST);
      }
      if (firstList.size() != secondList.size()) {
         return false;
      }
      if (firstList.isEmpty()) {
         return true;
      }
      return CollectionsUtils.areSparseDoubleVectorListsEqual(firstList, secondList);
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<EBaseLearner> classificationBaseLearners = EBaseLearner.getClassifiersEBaseLearners();

      List<JsonObject> baseLearnerParameters = new ArrayList<>();
      for (EBaseLearner baseLearner : classificationBaseLearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(baseLearner.getBaseLearnerIdentifier(), new JsonObject()));
         baseLearnerParameters.add(baselearnerDefinition);
      }

      JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
            .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier(),
                  JsonUtils.createJsonObjectFromKeyAndValue(CONFIGURATION_PARAMETER_THRESHOLD, CONFIGURATION_PARAMETER_THRESHOLD_VALUE)));
      baseLearnerParameters.add(baselearnerDefinition);

      return baseLearnerParameters;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      String exceptionString = TestUtils.getStringByReflectionSafely(AAlgorithmConfigurationWithBaseLearner.class,
            REFLECTION_WRONG_IDENTIFIER_FOR_BASE_LEARNER);

      List<EBaseLearner> invalidBaselearners = EBaseLearner.getRegressionEBaseLearners();
      List<Pair<String, JsonObject>> baselearnerParameters = new ArrayList<>();
      for (EBaseLearner invalidBaselearner : invalidBaselearners) {
         JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator
               .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(invalidBaselearner.getBaseLearnerIdentifier(), new JsonObject()));
         baselearnerParameters
               .add(Pair.of(String.format(exceptionString, invalidBaselearner.getBaseLearnerIdentifier()), baselearnerDefinition));
      }
      return baselearnerParameters;
   }


}
