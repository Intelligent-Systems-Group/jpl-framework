package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The abstract class for evaluating the Algorithm(s) by using percentage split technique and
 * storing the {@link EvaluationResult} of the current running evaluation. While extending this
 * class you need to provide the {@link ELearningProblem} and call super constructor with it as
 * parameter.
 * 
 * @author Pritha Gupta
 * @author Sebastian Osterbrink
 * @param <CONFIG> the {@link APercentageSplitEvaluationConfiguration} associated with this
 *           evaluation
 */
public abstract class APercentageSplitEvaluation<CONFIG extends APercentageSplitEvaluationConfiguration> extends AEvaluation<CONFIG> {
   private static final Logger logger = LoggerFactory.getLogger(APercentageSplitEvaluation.class);

   private static final String TEST_TRAIN_DATASETPAIRS_NOTCREATED = "Cannot create test and training dataset for dataset %s due to error %s";


   /**
    * Creates a percentage split evaluation with the default configuration and the learning problem
    * with which the evaluation class is associated with.
    * 
    * @param eLearningProblem the learning problem associated with the evaluation class to set
    */
   public APercentageSplitEvaluation(ELearningProblem eLearningProblem) {
      super(eLearningProblem);
   }


   /**
    * {@inheritDoc} This method splits the dataset according to the provided percentage of split in
    * the {@link APercentageSplitEvaluationConfiguration} and create such number of dataset pairs
    * for the provided number of dataset pairs in configuration.
    */
   @Override
   public List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairs(IDataset<?, ?, ?> dataset)
         throws TrainTestDatasetPairsNotCreated {
      List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testTrainPairs = new ArrayList<>();
      float percentage = configuration.getPercentage();
      int numberOfDatasets = configuration.getNumOfDatasets();
      int numberOfTrainingInstances = Math.round(dataset.getNumberOfInstances() * percentage);
      for (int i = 0; i < numberOfDatasets; i++) {
         dataset.shuffle();
         IDataset<?, ?, ?> testDataset = dataset.getPartOfDataset(0, 0);
         IDataset<?, ?, ?> trainDataset = dataset.getPartOfDataset(0, 0);
         try {
            IDataset.addShuffledInstancesToDataset(dataset, trainDataset, 0, numberOfTrainingInstances);
            IDataset.addShuffledInstancesToDataset(dataset, testDataset, numberOfTrainingInstances, dataset.getNumberOfInstances());

         } catch (InvalidInstanceException exception) {
            logger.error(exception.getMessage(), exception);
            throw new TrainTestDatasetPairsNotCreated(String.format(TEST_TRAIN_DATASETPAIRS_NOTCREATED, dataset, exception.getMessage()),
                  exception.getCause());
         }
         testDataset.setDatasetFile(dataset.getDatasetFile());
         trainDataset.setDatasetFile(dataset.getDatasetFile());
         testTrainPairs.add(Pair.of(testDataset, trainDataset));
      }
      return testTrainPairs;
   }


}
