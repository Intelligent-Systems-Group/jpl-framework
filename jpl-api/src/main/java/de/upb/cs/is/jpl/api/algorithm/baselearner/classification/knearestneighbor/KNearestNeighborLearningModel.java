package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor;


import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jungblut.jrpt.KDTree;
import de.jungblut.jrpt.VectorDistanceTuple;
import de.jungblut.math.dense.DenseDoubleVector;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.UnsupportedOperationException;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * Predicts the class of an instance based on the k-nearest neighbors algorithm. Collects the
 * classes of the k nearest neighbors (based on euclidean distance) and lets them perform a simple
 * majority voting. If there is a tie between two classes, it picks the first one it encounters.
 * Basically this means it picks one at random.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class KNearestNeighborLearningModel extends ABaseLearningModel<Double> {

   private static final String OPERATION_NOT_SUPPORTED_FOR_KNN = "Operation not supported for KNN";
   private static final String VOTES = "Collected Votes: {}";
   private static final String FOUND_NEIGHBORS = "Found Neighbors: {}";

   private static final Logger logger = LoggerFactory.getLogger(KNearestNeighborLearningModel.class);

   private KDTree<Double> kdTree;

   private int k;


   /**
    * Creates a new {@link KNearestNeighborLearningModel} object.
    * 
    * @param kdTree the dataset on which the neighborhood relation is defined in the form of a
    *           K-D-tree
    * @param k the number of neighbors to be considered
    * @param dimension the dimension of the prediction vectors
    */
   public KNearestNeighborLearningModel(KDTree<Double> kdTree, int k, int dimension) {
      super(dimension);
      this.kdTree = kdTree;
      this.k = k;
   }


   /**
    * {@inheritDoc}
    * 
    * @return {@code null} if there are no neighbors in the dataset, the majority vote of the
    *         nearest neighbors otherwise and in the case of a tie, it returns the first result
    *         which reached that number of votes
    * @throws PredictionFailedException if the {@code instanceParameter} was not a
    *            {@link BaselearnerInstance}
    */
   @Override
   public Double predict(IInstance<?, ?, ?> instanceParameter) throws PredictionFailedException {
      assertInstanceHasCorrectType(instanceParameter, BaselearnerInstance.class);
      BaselearnerInstance instance = (BaselearnerInstance) instanceParameter;
      List<VectorDistanceTuple<Double>> neighbors;
      neighbors = kdTree.getNearestNeighbours(new DenseDoubleVector(instance.getContextFeatureVector()), k);

      logger.debug(FOUND_NEIGHBORS, neighbors.toString());
      Bag<Double> voteBag = new HashBag<>();
      for (VectorDistanceTuple<Double> pair : neighbors) {
         Double vote = pair.getValue();
         voteBag.add(vote);
      }

      logger.debug(VOTES, voteBag.toString());
      Double result = null;
      int winningCount = 0;
      for (Double object : voteBag.uniqueSet()) {
         if (voteBag.getCount(object) > winningCount) {
            winningCount = voteBag.getCount(object);
            result = object;
         }
      }
      return result;
   }


   /**
    * Returns the indices of the nearest neighbors of the base learner dataset. This method works
    * only if the base learner contains indices as classification.
    *
    * @param featureVector the feature vector of the instance
    * @return the indices of the base learner dataset
    */
   public int[] getNeighborIndeces(double[] featureVector) {
      List<VectorDistanceTuple<Double>> neighbors = kdTree.getNearestNeighbours(new DenseDoubleVector(featureVector), k);

      int[] indices = new int[neighbors.size()];
      int index = 0;
      for (VectorDistanceTuple<Double> pair : neighbors) {
         indices[index++] = pair.getValue().intValue();
      }
      return indices;
   }


   @Override
   public double getBias() throws UnsupportedOperationException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_FOR_KNN);
   }


   @Override
   public IVector getWeightVector() throws UnsupportedOperationException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_FOR_KNN);

   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + k;
      result = prime * result + ((kdTree == null) ? 0 : kdTree.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof KNearestNeighborLearningModel))
         return false;
      KNearestNeighborLearningModel other = (KNearestNeighborLearningModel) obj;
      if (k != other.k)
         return false;
      if (kdTree == null) {
         if (other.kdTree != null)
            return false;
      } else if (!kdTree.equals(other.kdTree))
         return false;
      return true;
   }


}
