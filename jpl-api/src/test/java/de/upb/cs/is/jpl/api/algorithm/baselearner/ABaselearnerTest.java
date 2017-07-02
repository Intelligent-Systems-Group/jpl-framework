package de.upb.cs.is.jpl.api.algorithm.baselearner;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.ATrainableAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;


/**
 * This abstract class is used for all tests of the baselearner algorithms.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of the
 *           algorithm
 * @param <ITEM> the type of the item feature vector used in the implementation of the algorithm
 * @param <RATING> the type of the rating used in the implementation of the algorithm
 */
public abstract class ABaselearnerTest<CONTEXT, ITEM, RATING> extends ATrainableAlgorithmTest<CONTEXT, ITEM, RATING> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "baselearner" + File.separator;


   /**
    * Creates a new unit test for baselearner algorithms with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ABaselearnerTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Returns an {@link BaselearnerDataset} with the given feature vecotors and the given ratings.
    * 
    * @param featureVectors the feature vectors of the instances
    * @param ratings the ratings of the instances
    * 
    * @return a dataset containing the given feature vectors and ratings
    */
   protected BaselearnerDataset createBaselearnerDataset(double[][] featureVectors, double[] ratings) {
      if (featureVectors.length != ratings.length) {
         return null;
      }
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(ratings.length, featureVectors[0].length);
      for (int i = 0; i < ratings.length; i++) {
         baselearnerDataset.addFeatureVectorWithResult(featureVectors[i], ratings[i]);
      }
      return baselearnerDataset;
   }


   /**
    * Returns an {@link BaselearnerDataset} with the given feature vecotors, the given ratings and
    * the given weights.
    * 
    * @param featureVectors the feature vectors of the instances
    * @param ratings the ratings of the instances
    * @param weights the weights of the instances
    * 
    * @return a dataset containing the given feature vectors, ratings and weights
    */
   protected BaselearnerDataset createBaselearnerDataset(double[][] featureVectors, double[] ratings, double[] weights) {
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(ratings.length, featureVectors[0].length);
      for (int i = 0; i < ratings.length; i++) {
         baselearnerDataset.addFeatureVectorWithResultAndWeight(featureVectors[i], ratings[i], weights[i]);
      }
      return baselearnerDataset;
   }
}
