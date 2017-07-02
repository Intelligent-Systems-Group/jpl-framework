package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for {@link MatrixFactorizationLearningAlgorithm}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class MatrixFactorizationLearningAlgorithmTest extends ALearningAlgorithmTest<IVector, IVector, Double> {

   private final static Logger logger = LoggerFactory.getLogger(MatrixFactorizationLearningAlgorithmTest.class);

   private static final String RESOURCE_DIRECTORY_LEVEL = "collaborativefiltering" + File.separator;
   private static final String WRONG_DATASET_PATH = "relativeDataset.gprf";
   private static final String MOVIELENS_DATASET_PATH = "movielens.gprf";
   private static final String SIMPLE_DATASET_PATH = "simpleDataset.gprf";

   private static final String PREDICTION = "Prediction:";


   /**
    * Default constructor for the test. Includes the additional path to the ressources directory.
    */
   public MatrixFactorizationLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      ITrainableAlgorithm algorithm = new MatrixFactorizationLearningAlgorithm();
      try {
         algorithm.setParameters(JsonUtils.createJsonObjectFromKeyAndValue("n_max", "500"));
      } catch (ParameterValidationFailedException e) {
         Assert.fail();
      }
      return algorithm;
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> result = new ArrayList<>();
      result.add(createDatasetOutOfFile(new DefaultRelativeDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_PATH)));
      return result;
   }


   @SuppressWarnings("unchecked")
   @Override
   public List<IDataset<IVector, IVector, Double>> getCorrectDatasetList() {
      List<IDataset<IVector, IVector, Double>> result = new ArrayList<>();
      result.add((IDataset<IVector, IVector, Double>) createDatasetOutOfFile(new CollaborativeFilteringParser(),
            getTestRessourcePathFor(SIMPLE_DATASET_PATH)));
      return result;
   }


   @SuppressWarnings("unchecked")
   @Override
   public List<Pair<IDataset<IVector, IVector, Double>, List<Double>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<IVector, IVector, Double>, List<Double>>> datasetAndPredictions = new ArrayList<>();
      IDataset<IVector, IVector, Double> dataset = (IDataset<IVector, IVector, Double>) createDatasetOutOfFile(
            new CollaborativeFilteringParser(), getTestRessourcePathFor(SIMPLE_DATASET_PATH));
      List<Double> predictions = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         predictions.add(dataset.getInstance(i).getRating());
      }
      Pair<IDataset<IVector, IVector, Double>, List<Double>> datasetAndPredictionsPair = Pair.of(dataset, predictions);
      datasetAndPredictions.add(datasetAndPredictionsPair);
      return datasetAndPredictions;
   }


   /**
    * Short Test, will be replaced later.
    * 
    * @throws TrainModelsFailedException if the training operation failed
    * @throws PredictionFailedException if the prediction operation failed
    */
   @Test
   @Ignore
   public void shortTest() throws TrainModelsFailedException, PredictionFailedException {
      CollaborativeFilteringDataset trainingData = (CollaborativeFilteringDataset) createDatasetOutOfFile(
            new CollaborativeFilteringParser(), getTestRessourcePathFor(MOVIELENS_DATASET_PATH));

      MatrixFactorizationConfiguration config = new MatrixFactorizationConfiguration();
      config.setStepSize(0.0001);
      MatrixFactorizationLearningAlgorithm algo = new MatrixFactorizationLearningAlgorithm();
      MatrixFactorizationLearningModel model = algo.train(trainingData);
      Double result = model.predict(new CollaborativeFilteringInstance(1, 256, null, trainingData));
      logger.debug(PREDICTION + result);

      result = model.predict(new CollaborativeFilteringInstance(1, 0, null, trainingData));
      logger.debug(PREDICTION + result);
      logger.debug(model.toString());
   }


   @Override
   protected boolean areDoublesEqual(double firstValue, double secondValue) {
      return TestUtils.areDoublesEqual(firstValue, secondValue, 0.1);
   }

}
