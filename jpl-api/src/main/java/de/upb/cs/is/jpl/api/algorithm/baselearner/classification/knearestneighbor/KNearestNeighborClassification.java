/**
 * 
 */
package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor;


import java.util.stream.Stream;

import de.jungblut.jrpt.KDTree;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleVector;
import de.jungblut.math.tuple.Tuple;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * The {@link KNearestNeighborClassification} performs classification by using the k nearest
 * neighbor classification algorithm. This algorithm works by determining the nearest neighbors and
 * querying them for their classification. The algorithm collects these classifications and performs
 * a simple majority voting, after which it return the winner.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class KNearestNeighborClassification extends ABaselearnerAlgorithm<KNearestNeighborConfiguration> {

   private int numberOfNeighbors = 0;


   /**
    * Creates a new k nearest neighbor classifier with given k from the configuration.
    */
   public KNearestNeighborClassification() {
      super(EBaseLearner.KNEAREST_NEIGHBOUR.getBaseLearnerIdentifier());
      numberOfNeighbors = configuration.getNumberOfNeighbors();
   }


   /**
    * Creates a new k nearest neighbor classifier with the provided k value.
    *
    * @param k the number of the nearest neighbors
    */
   public KNearestNeighborClassification(int k) {
      super(EBaseLearner.KNEAREST_NEIGHBOUR.getBaseLearnerIdentifier());
      this.numberOfNeighbors = k;
   }


   @Override
   protected KNearestNeighborLearningModel performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      BaselearnerDataset trainingDataset = (BaselearnerDataset) dataset;
      KDTree<Double> kdTree = new KDTree<>();
      int numberOfPoints = trainingDataset.getNumberOfInstances();

      Stream.Builder<Tuple<DoubleVector, Double>> streamBuilder = Stream.builder();
      for (int i = 0; i < numberOfPoints; i++) {
         BaselearnerInstance currentInstance = trainingDataset.getInstance(i);
         DoubleVector featureVector = new DenseDoubleVector(currentInstance.getContextFeatureVector());
         Double result = currentInstance.getRating();
         streamBuilder.add(new Tuple<DoubleVector, Double>(featureVector, result));
      }
      kdTree.constructWithPayload(streamBuilder.build());
      kdTree.balanceBySort();

      return new KNearestNeighborLearningModel(kdTree, numberOfNeighbors, trainingDataset.getNumberOfFeatures());

   }


   @Override
   public KNearestNeighborLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (KNearestNeighborLearningModel) super.train(dataset);
   }


   @Override
   public KNearestNeighborConfiguration createDefaultAlgorithmConfiguration() {
      return new KNearestNeighborConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + numberOfNeighbors;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof KNearestNeighborClassification))
         return false;
      KNearestNeighborClassification other = (KNearestNeighborClassification) obj;
      if (numberOfNeighbors != other.numberOfNeighbors)
         return false;
      return true;
   }

}
