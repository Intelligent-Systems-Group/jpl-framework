package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased.UserBasedFilteringLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased.UserBasedFilteringLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for the {@link UserBasedFilteringLearningAlgorithm} algorithm. <br>
 * 
 * @author Sebastian Osterbrink
 *
 */
public class UserBasedFilteringLearningAlgorithmTest extends ALearningAlgorithmTest<IVector, IVector, Double> {


   private final static Logger logger = LoggerFactory.getLogger(UserBasedFilteringLearningAlgorithmTest.class);

   private static final String RESOURCE_DIRECTORY_LEVEL = "collaborativefiltering" + File.separator;
   private static final String WRONG_DATASET_PATH = "relativeDataset.gprf";
   private static final String MOVIELENS_DATASET_PATH = "movielens.gprf";

   private static final String PREDICTION = "Prediction:";


   /**
    * Creates the {@link UserBasedFilteringLearningAlgorithmTest}.
    */
   public UserBasedFilteringLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new UserBasedFilteringLearningAlgorithm();
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
            getTestRessourcePathFor(MOVIELENS_DATASET_PATH)));
      return result;
   }


   @Override
   public List<Pair<IDataset<IVector, IVector, Double>, List<Double>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<IVector, IVector, Double>, List<Double>>> datasetAndPredictions = new ArrayList<>();
      IDataset<IVector, IVector, Double> dataset = new CollaborativeFilteringDataset(1, 1);
      List<Double> predictions = new ArrayList<>();

      // TODO Fill this with values

      datasetAndPredictions.add(Pair.of(dataset, predictions));
      return datasetAndPredictions;
   }


   /**
    * Short Test, will be replaced later.
    * 
    * @throws TrainModelsFailedException if the training operation failed
    * @throws PredictionFailedException if the prediction operation failed
    */
   @Test
   public void shortTest() throws TrainModelsFailedException, PredictionFailedException {
      UserBasedFilteringLearningAlgorithm algo = new UserBasedFilteringLearningAlgorithm();

      CollaborativeFilteringDataset trainingData = (CollaborativeFilteringDataset) createDatasetOutOfFile(algo.getDatasetParser(),
            getTestRessourcePathFor(MOVIELENS_DATASET_PATH));

      UserBasedFilteringLearningModel model = algo.train(trainingData);

      Double result = model.predict(new CollaborativeFilteringInstance(0, 0, 0.0, trainingData));
      logger.debug(PREDICTION + result);
   }


}
