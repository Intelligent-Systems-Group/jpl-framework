package de.upb.cs.is.jpl.api.dataset.rankaggregation;


import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class is a rank aggregation instance, which is used in combination with the
 * {@link RankAggregationDataset}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class RankAggregationInstance extends AInstance<Integer, NullType, Ranking> {

   private int countRanking;


   @Override
   public Integer getContextFeatureVector() {
      return countRanking;
   }


   @Override
   public void setContextFeatureVector(Integer contextFeatureVector) {
      this.countRanking = contextFeatureVector;
   }


   @Override
   public NullType getItemFeatureVectors() {
      throw new UnsupportedOperationException();
   }


   @Override
   public void setItemFeatureVector(NullType itemFeatureVectors) {
      throw new UnsupportedOperationException();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + countRanking;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof RankAggregationInstance))
         return false;
      RankAggregationInstance other = (RankAggregationInstance) obj;
      if (countRanking != other.countRanking)
         return false;
      return true;
   }


}
