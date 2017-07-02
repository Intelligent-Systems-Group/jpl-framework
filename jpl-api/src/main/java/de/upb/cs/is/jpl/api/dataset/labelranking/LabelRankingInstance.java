package de.upb.cs.is.jpl.api.dataset.labelranking;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class is a label ranking instance, which is used in combination with the
 * {@link LabelRankingDataset}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingInstance extends AInstance<double[], NullType, Ranking> {


   private double[] instanceFeatures;
   private int totalNumberOfLabels;


   @Override
   public double[] getContextFeatureVector() {
      return instanceFeatures;
   }


   @Override
   public void setContextFeatureVector(double[] instanceFeatures) {
      this.instanceFeatures = instanceFeatures;
   }


   @Override
   public NullType getItemFeatureVectors() {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setItemFeatureVector(NullType itemFeatureVectors) {
      throw new UnsupportedOperationException();
   }


   /**
    * Returns the number of labels for the actual ranking.
    *
    * @return the number of labels for the actual ranking
    */
   public int getNumberOfLabels() {
      int[] objectList = getRating().getObjectList();
      if (objectList != null) {
         return objectList.length;
      }
      return 0;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(instanceFeatures);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof LabelRankingInstance))
         return false;
      LabelRankingInstance other = (LabelRankingInstance) obj;
      if (!Arrays.equals(instanceFeatures, other.instanceFeatures))
         return false;
      return true;
   }


   /**
    * Returns the total number of labels.
    *
    * @return the total number of labels
    */
   public int getTotalNumberOfLabels() {
      return totalNumberOfLabels;
   }


   /**
    * Sets the total number of labels.
    *
    * @param totalNumberOfLabels the new total number of labels
    */
   public void setTotalNumberOfLabels(int totalNumberOfLabels) {
      this.totalNumberOfLabels = totalNumberOfLabels;
   }


}
