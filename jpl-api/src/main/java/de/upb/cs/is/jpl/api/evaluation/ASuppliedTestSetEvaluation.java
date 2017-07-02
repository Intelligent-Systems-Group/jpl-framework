package de.upb.cs.is.jpl.api.evaluation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The abstract class for evaluating the Algorithm(s) by using supplied test set technique and
 * storing the evaluationResult of the current running evaluation. While extending this class you
 * need to provide the {@link ELearningProblem} and call super constructor with it as parameter.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the {@link ASuppliedTestSetEvaluationConfiguration} associated with this
 *           evaluation
 * 
 */
public abstract class ASuppliedTestSetEvaluation<CONFIG extends ASuppliedTestSetEvaluationConfiguration> extends AEvaluation<CONFIG> {
   private static final Logger logger = LoggerFactory.getLogger(ASuppliedTestSetEvaluation.class);

   private static final String TEST_SET_FILE_PATH_NOT_SET_FOR_DATASET = "Test dataset file paths are not set for the dataset %s ";
   private static final String TEST_SET_FILE_NOT_EXIST_FOR_DATASET = "Test dataset file does not exist at path %s for the dataset %s ";
   private static final String TEST_DATASET_FILE_CANNOT_BE_PARSED = "Test dataset file cannot be parsed %s for the path %s ";
   private List<DatasetFile> testDataSetFiles;


   /**
    * Creates a supplied test set evaluation with the default configuration and the learning problem
    * with which the evaluation class is associated with.
    * 
    * @param eLearningProblem the learning problem associated with the evaluation class to set
    */
   public ASuppliedTestSetEvaluation(ELearningProblem eLearningProblem) {
      super(eLearningProblem);
      testDataSetFiles = new ArrayList<>();
   }


   @Override
   public int setupSingleEvaluationDatasetAndAlgorithm(int setNumber, DatasetFile datasetFile, ILearningAlgorithm learningAlgorithm,
         List<IMetric<?, ?>> evaluationMetrics) {
      int setSize = setNumber;
      try {
         datasetParser = learningAlgorithm.getDatasetParser();
         if (datasetParser != null) {
            IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);
            logger.debug(String.format(ADDING_DATASET_MESSAGE, datasetFile.getFile()));
            List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testTrainDatasetPairs = getTestTrainPairs(dataset);
            for (int i = 0; i < testTrainDatasetPairs.size(); i++) {
               List<EvaluationSetting> evaluationSettings = new ArrayList<>();
               ILearningModel<?> learningModel = learningAlgorithm.train(testTrainDatasetPairs.get(i).getSecond());
               EvaluationSetting evaluationSetting = new EvaluationSetting(testTrainDatasetPairs.get(i).getFirst(), learningAlgorithm,
                     learningModel, evaluationMetrics);
               evaluationSettings.add(evaluationSetting);
               getEvaluationConfiguration().addEvaluationSettingsWithSetNumber(setSize, evaluationSettings);
               setSize++;
            }

         } else {
            logger.warn(String.format(DATASET_PARSER_NOT_SET_WARNING_MESSAGE, learningAlgorithm));
         }
      } catch (ParsingFailedException parsingFailedException) {
         String errorMessage = String.format(DATASET_PARSING_WARNING_MESSAGE, datasetFile.getFile(), learningAlgorithm,
               parsingFailedException.getMessage());
         logger.error(errorMessage, parsingFailedException);
      } catch (TrainModelsFailedException trainModelsFailedException) {
         logger.error(String.format(MODEL_CANNOT_BE_TRAINED_WARNING_MESSAGE, datasetFile.getFile(), learningAlgorithm,
               trainModelsFailedException.getMessage()), trainModelsFailedException);
      } catch (TrainTestDatasetPairsNotCreated exception) {
         logger.error(exception.getMessage(), exception);
      }
      return setSize;
   }


   @Override
   public List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairs(IDataset<?, ?, ?> dataset)
         throws TrainTestDatasetPairsNotCreated {
      List<String> testSetFilePaths = configuration.getTestSetPathsForTrainingDatasetFile(dataset.getDatasetFile());
      if (testSetFilePaths.isEmpty())
         throw new TrainTestDatasetPairsNotCreated(String.format(TEST_SET_FILE_PATH_NOT_SET_FOR_DATASET, dataset.toString()));
      ArrayList<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> pairs = new ArrayList<>();

      for (String testSetFilePath : testSetFilePaths) {
         DatasetFile testSetDatasetSetFile = new DatasetFile(new File(testSetFilePath));
         if (!testSetDatasetSetFile.getFile().exists())
            throw new TrainTestDatasetPairsNotCreated(
                  String.format(TEST_SET_FILE_NOT_EXIST_FOR_DATASET, testSetFilePath, dataset.toString()));
         if (!testDataSetFiles.contains(testSetDatasetSetFile))
            testDataSetFiles.add(testSetDatasetSetFile);

         IDataset<?, ?, ?> testDataset;
         try {
            testDataset = datasetParser.parse(testSetDatasetSetFile);
         } catch (ParsingFailedException exception) {
            throw new TrainTestDatasetPairsNotCreated(String.format(TEST_DATASET_FILE_CANNOT_BE_PARSED, testSetFilePath), exception);
         }
         pairs.add(Pair.of(testDataset, dataset));
      }
      return pairs;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((testDataSetFiles == null) ? 0 : testDataSetFiles.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ASuppliedTestSetEvaluation<?>) {
         ASuppliedTestSetEvaluation<?> castedEvaluation = ASuppliedTestSetEvaluation.class.cast(secondObject);
         if (testDataSetFiles == null) {
            if (castedEvaluation.testDataSetFiles != null)
               return false;
         } else if (testDataSetFiles.equals(castedEvaluation.testDataSetFiles))
            return true;
      }
      return false;
   }


}
