package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The abstract class for evaluating the in sample errors on Algorithm(s) and storing the
 * evaluationResult of the current running evaluation. While extending this class you need to
 * provide the {@link ELearningProblem} and call super constructor with it as parameter.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the {@link AEvaluationConfiguration} associated with this evaluation
 */
public abstract class AInSampleEvaluation<CONFIG extends AInSampleEvaluationConfiguration> extends AEvaluation<CONFIG> {


   /**
    * Creates an in sample evaluation with the default configuration and the learning problem with
    * which the evaluation class is associated with.
    * 
    * @param eLearningProblem the learning problem associated with the evaluation class to set
    */
   public AInSampleEvaluation(ELearningProblem eLearningProblem) {
      super(eLearningProblem);
   }


   @Override
   public List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairs(IDataset<?, ?, ?> dataset)
         throws TrainTestDatasetPairsNotCreated {
      List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> trainTestPairs = new ArrayList<>();
      trainTestPairs.add(Pair.of(dataset, dataset));
      return trainTestPairs;
   }

}
