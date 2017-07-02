package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm;


import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.pocket.PocketLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;


/**
 * Test for the {@link PocketLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class PocketLearningAlgorithmTest extends ALinearClassificationTest {


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new PocketLearningAlgorithm();
   }


   @Override
   protected BaselearnerDataset createBaselearnerDataset() {
      double[][] trainingInstances = { { -0.4, 0.3 }, { -0.3, -0.1 }, { -0.2, 0.4 }, { -0.1, 0.1 }, { 0.1, -0.5 }, { 0.2, -0.9 },
            { 0.3, 0.2 }, { 0.4, -0.6 } };
      double[] correctResults = { 1, 1, 1, 1, -1, -1, -1, -1 };
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(trainingInstances.length, trainingInstances[0].length);
      for (int i = 0; i < correctResults.length; i++) {
         baselearnerDataset.addFeatureVectorWithResult(trainingInstances[i], correctResults[i]);
      }

      return baselearnerDataset;
   }

}
