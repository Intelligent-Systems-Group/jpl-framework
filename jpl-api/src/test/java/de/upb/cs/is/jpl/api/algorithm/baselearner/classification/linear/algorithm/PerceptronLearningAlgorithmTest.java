package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm;


import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.perceptron.PerceptronLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;


/**
 * Test for the {@link PerceptronLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class PerceptronLearningAlgorithmTest extends ALinearClassificationTest {

   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new PerceptronLearningAlgorithm();
   }


   @Override
   protected BaselearnerDataset createBaselearnerDataset() {
      double[][] trainingInstances = { { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };
      double[] correctResults = { 1, 1, 1, -1 };
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(trainingInstances.length, trainingInstances[0].length);
      for (int i = 0; i < correctResults.length; i++) {
         baselearnerDataset.addFeatureVectorWithResult(trainingInstances[i], correctResults[i]);
      }

      return baselearnerDataset;
   }

}
