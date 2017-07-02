package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.ARankAggregationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;


/**
 * The model class is created by the {@link PlackettLuceLearningAlgorithm} after the training on the
 * rank aggregation dataset.
 * 
 * @author Andreas Kornelsen
 */
public class PlackettLuceLearningModel extends ARankAggregationLearningModel {

   private double[] mleParameters;


   /**
    * Instantiates a new Plackett-Luce model with the Maximum Likelihood Estimation parameters.
    *
    * @param mleParameters the Maximum Likelihood Estimation parameters
    * @param listOfLabels the list of labels
    */
   public PlackettLuceLearningModel(double[] mleParameters, List<Integer> listOfLabels) {
      super(listOfLabels);
      this.mleParameters = mleParameters;
   }


   @Override
   public List<Ranking> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, RankAggregationDataset.class);
      assetDatasetCompatibility(dataset);
      return Arrays.asList(sortLabelsAccordingToMleParametersInDescreasingOrder(mleParameters));
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, RankAggregationInstance.class);
      assertInstanceCompatibility(instance);
      return sortLabelsAccordingToMleParametersInDescreasingOrder(mleParameters);
   }


   /**
    * Sorting the labels in decreasing order according to the Maximum Likelihood Estimation
    * parameters (mleParameters).
    *
    * @param mleParameters the Maximum Likelihood Estimation for the Plackett-Luce Model
    * @return the ranking with labels from one to numberOfLabels sorted in decreasing order
    *         according to the mleParameters
    */
   private Ranking sortLabelsAccordingToMleParametersInDescreasingOrder(double[] mleParameters) {
      Integer[] labels = new Integer[mleParameters.length];
      for (int labelIndex = 0; labelIndex < labels.length; labelIndex++) {
         labels[labelIndex] = labelIndex;
      }

      Comparator<Integer> comperator = (Integer o1, Integer o2) -> ((Double) mleParameters[o2]).compareTo(mleParameters[o1]);
      Arrays.sort(labels, comperator);
      int[] rankingObjects = Arrays.stream(labels).mapToInt(Integer::intValue).toArray();
      return new Ranking(rankingObjects, Ranking.createCompareOperatorArrayForLabels(rankingObjects));
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((listOfLabels == null) ? 0 : listOfLabels.hashCode());
      result = prime * result + Arrays.hashCode(mleParameters);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof PlackettLuceLearningModel))
         return false;
      PlackettLuceLearningModel other = (PlackettLuceLearningModel) obj;
      if (listOfLabels == null) {
         if (other.listOfLabels != null)
            return false;
      } else if (!listOfLabels.equals(other.listOfLabels))
         return false;
      if (!Arrays.equals(mleParameters, other.mleParameters))
         return false;
      return true;
   }


}
