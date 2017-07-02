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
 * The abstract class for evaluating the Algorithm(s) by using cross-validation technique and
 * storing the list of {@link EvaluationResult} of the current running evaluation. While extending
 * this class you need to provide the {@link ELearningProblem} and call the super constructor with
 * it as parameter. You should always call up to your superclass when implementing the init()
 * method.
 * 
 * @author Pritha Gupta
 * @author Sebastian Osterbrink
 * @param <CONFIG> the {@link ACrossValidationEvaluationConfiguration} associated with this
 *           evaluation
 */
public abstract class ACrossValidationEvaluation<CONFIG extends ACrossValidationEvaluationConfiguration> extends AEvaluation<CONFIG> {

   private static final Logger logger = LoggerFactory.getLogger(ACrossValidationEvaluation.class);
   private static final String TEST_TRAIN_DATASETPAIRS_NOTCREATED = "Cannot create test and training dataset for dataset %s due to error %s";
   private static final String NUM_OF_INSTANCES_LESS_THAN_FOLDS = "Number of instances are less than number of folds in dataset %s";


   /**
    * Creates a cross validation evaluation with the default configuration and the learning problem
    * with which the evaluation class is associated with.
    * 
    * @param eLearningProblem the learning problem associated with the evaluation class to set
    */
   public ACrossValidationEvaluation(ELearningProblem eLearningProblem) {
      super(eLearningProblem);
   }


   /**
    * {@inheritDoc} This method splits the dataset according to the provided number of folds in the
    * {@link ACrossValidationEvaluationConfiguration}. Divides all the samples in k groups of
    * samples, called folds (if k = number of instances in dataset, this is equivalent to the Leave
    * One Out strategy), of equal sizes (if possible). Training dataset contains k - 1 folds, and
    * the fold left out is used in testing.
    * 
    */
   @Override
   public List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairs(IDataset<?, ?, ?> dataset)
         throws TrainTestDatasetPairsNotCreated {
      List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testTrainPairs = new ArrayList<>();
      int folds = configuration.getFolds();
      if (folds > dataset.getNumberOfInstances()) {
         throw new TrainTestDatasetPairsNotCreated(String.format(NUM_OF_INSTANCES_LESS_THAN_FOLDS, dataset));
      }
      if (folds == 1) {
         folds = dataset.getNumberOfInstances() - 1;
      }
      dataset.shuffle();
      for (int currentFold = 0; currentFold < folds; currentFold++) {
         int numberOfTestingInstances = dataset.getNumberOfInstances() / folds;
         IDataset<?, ?, ?> testDataset = dataset.getPartOfDataset(0, 0);
         IDataset<?, ?, ?> trainDataset = dataset.getPartOfDataset(0, 0);
         try {
            // for the last instance
            if (currentFold == folds - 1) {
               numberOfTestingInstances = dataset.getNumberOfInstances() - (folds - 1) * numberOfTestingInstances;
               int startIndex = dataset.getNumberOfInstances() - numberOfTestingInstances;
               IDataset.addShuffledInstancesToDataset(dataset, testDataset, startIndex, startIndex + numberOfTestingInstances);
               IDataset.addShuffledInstancesToDataset(dataset, trainDataset, 0, startIndex);
            } else {
               IDataset.addShuffledInstancesToDataset(dataset, trainDataset, 0, currentFold * numberOfTestingInstances);
               IDataset.addShuffledInstancesToDataset(dataset, testDataset, currentFold * numberOfTestingInstances,
                     (currentFold + 1) * numberOfTestingInstances);
               IDataset.addShuffledInstancesToDataset(dataset, trainDataset, (currentFold + 1) * numberOfTestingInstances,
                     dataset.getNumberOfInstances());

            }
            trainDataset.setDatasetFile(dataset.getDatasetFile());
            testDataset.setDatasetFile(dataset.getDatasetFile());

         } catch (InvalidInstanceException exception) {
            logger.error(exception.getMessage(), exception);
            throw new TrainTestDatasetPairsNotCreated(String.format(TEST_TRAIN_DATASETPAIRS_NOTCREATED, dataset, exception.getMessage()),
                  exception.getCause());
         }
         testTrainPairs.add(Pair.of(testDataset, trainDataset));
      }

      return testTrainPairs;
   }

}
