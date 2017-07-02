package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased.UserBasedFilteringConfiguration;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for the {@link CollaborativeFilteringPercentageSplitEvaluation}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringPercentageSplitTest extends AEvaluationTest {

   private static final String MOVIELENS_GPRF = "movielens.gprf";
   private static final String COULD_NOT_SPLIT_THE_DATASET_INTO_TRAINING_AND_TESTING_DATSET = "Could not split the dataset into training and testing datset";
   private static final String COLLABORATIVEFITERING = "collaborativefiltering" + File.separator;


   /**
    * Creates a new {@link CollaborativeFilteringPercentageSplitTest}.
    */
   public CollaborativeFilteringPercentageSplitTest() {
      super(COLLABORATIVEFITERING);
   }


   @Override
   public IEvaluation getEvaluation() {
      return new CollaborativeFilteringPercentageSplitEvaluation();
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> testedAlgorithms = new ArrayList<>();
      ILearningAlgorithm userBasedFiltering = ELearningAlgorithm.USER_BASED_FILTERING.createLearningAlgorithm();
      UserBasedFilteringConfiguration config = (UserBasedFilteringConfiguration) userBasedFiltering.getDefaultAlgorithmConfiguration();
      config.setNumberOfNeighbors(10);
      userBasedFiltering.setAlgorithmConfiguration(config);
      testedAlgorithms.add(userBasedFiltering);

      ILearningAlgorithm matrix = ELearningAlgorithm.MATRIX_FACTORIZATION.createLearningAlgorithm();
      ((MatrixFactorizationConfiguration) matrix.getAlgorithmConfiguration()).setRandomSeed(1234);
      testedAlgorithms.add(matrix);


      return testedAlgorithms;
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics.add(EMetric.MEAN_ABSOLUTE_ERROR.createEvaluationMetric());
      evaluationMetrics.add(EMetric.MEAN_SQUARED_ERROR.createEvaluationMetric());
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      List<Pair<EvaluationSetting, EvaluationResult>> listOfPairs = new ArrayList<>();

      double[][] expectedResults = { { 0.826, 1.085 }, { 0.767, 0.944 } };
      double[] percentages = { 0.999, 0.8 };

      List<ILearningAlgorithm> learningAlgorithms = getLearningAlgorithms();
      for (int j = 0; j < learningAlgorithms.size(); j++) {
         ILearningAlgorithm learningAlgorithm = learningAlgorithms.get(j);
         IDataset<?, ?, ?> movieLensDataset = this.createDatasetOutOfFile(learningAlgorithm.getDatasetParser(),
               getTestRessourcePathFor(MOVIELENS_GPRF));

         try {
            CollaborativeFilteringPercentageSplitEvaluationConfiguration evaluationConfiguration = (CollaborativeFilteringPercentageSplitEvaluationConfiguration) evaluation
                  .getEvaluationConfiguration();
            evaluationConfiguration.setPercentage((float) percentages[j]);
            List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testAndTrainPairs = evaluation.getTestTrainPairs(movieLensDataset);
            for (Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>> testAndTrainPair : testAndTrainPairs) {
               ILearningModel<?> learningModel = getLearningModelForAlgorithmOnDataset(learningAlgorithm, testAndTrainPair.getSecond());
               EvaluationSetting evaluationSetting = new EvaluationSetting(testAndTrainPair.getFirst(), learningAlgorithm, learningModel,
                     evaluationMetrics);
               EvaluationResult evaluationResult = new EvaluationResult();
               evaluationResult.setDataset(movieLensDataset);
               evaluationResult.setLearningAlgorithm(learningAlgorithm);
               for (int i = 0; i < evaluationMetrics.size(); i++) {
                  evaluationResult.addLossWithMetric(expectedResults[j][i], evaluationMetrics.get(i));
               }
               listOfPairs.add(Pair.of(evaluationSetting, evaluationResult));
            }

         } catch (TrainTestDatasetPairsNotCreated e) {
            Assert.fail(COULD_NOT_SPLIT_THE_DATASET_INTO_TRAINING_AND_TESTING_DATSET);
         }
      }
      return listOfPairs;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      List<Pair<EvaluationSetting, EvaluationResult>> wrongParameters = new ArrayList<>();
      ILearningAlgorithm algorithm = new MatrixFactorizationLearningAlgorithm();
      IDataset<?, ?, ?> dataset = createDatasetOutOfFile(algorithm.getDatasetParser(), getTestRessourcePathFor(MOVIELENS_GPRF));
      try {
         List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testAndTrainPairs = evaluation.getTestTrainPairs(dataset);

         ILearningModel<?> learningModel = getLearningModelForAlgorithmOnDataset(algorithm, testAndTrainPairs.get(0).getSecond());
         EvaluationSetting evaluationSetting = new EvaluationSetting(testAndTrainPairs.get(0).getFirst(), algorithm, learningModel,
               evaluationMetrics);
         EvaluationResult evaluationResult = new EvaluationResult();
         evaluationResult.setDataset(dataset);
         evaluationResult.setLearningAlgorithm(algorithm);
         for (int i = 0; i < evaluationMetrics.size(); i++) {
            evaluationResult.addLossWithMetric(0.0, evaluationMetrics.get(i));
         }
         evaluationResult.setExtraEvaluationInformation(StringUtils.SINGLE_WHITESPACE);
         wrongParameters.add(Pair.of(evaluationSetting, evaluationResult));
      } catch (TrainTestDatasetPairsNotCreated e) {
         Assert.fail(COULD_NOT_SPLIT_THE_DATASET_INTO_TRAINING_AND_TESTING_DATSET);
      }
      return wrongParameters;
   }
}
