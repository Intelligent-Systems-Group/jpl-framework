package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.plackettluce.PlackettLuceModelFittingAlgorithm;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.math.distribution.IDistributionSampleSet;
import de.upb.cs.is.jpl.api.math.distribution.special.ASpecialDistributionSampleSet;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This class is an implementation of the {@link IDistributionSampleSet} tailored specifically for
 * samples from the {@link PlackettLuceModel}, i.e. {@link Ranking}. This sample set is optimized
 * for the {@link PlackettLuceModelFittingAlgorithm} in such a way that statistics required by the
 * algorithm are only computed once and then cached for further usage. This means that instances of
 * this class might need a lot of memory for large sample sets and should be actively invalidated by
 * setting them to {@code null} if they are no longer used in order to speed up the garbage
 * collection process.
 * 
 * In particular two important statistics are provided. Firstly this is the number of
 * {@link Ranking}s for which a given item is ranked higher than last. Secondly this is an indicator
 * if a given item is ranked lower or equal than a given rank in a given {@link Ranking}.
 * 
 * @author Tanja Tornede
 *
 */
public class PlackettLuceModelSampleSet extends ASpecialDistributionSampleSet<Ranking> {

   private static final String ERROR_INVALID_RANKING = "The given ranking is invalid.";
   private static final String ERROR_GIVEN_INDEX_IS_NEGATIVE = "The given index is negative.";
   private static final String ERROR_GIVEN_SAMPLE_IS_NULL = "The given sample is null.";
   private static final String ERROR_GIVEN_INDEX_OUT_OF_BOUNDS = "The given index is out of bounds.";

   private List<Ranking> rankings;
   private int[] validItems;

   private RankingValidator rankingValidator;

   private Map<Integer, Integer> itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap;
   private Map<Triple<Integer, Integer, Ranking>, Boolean> itemToItemIsRankedLowerOrEqualThanMap;


   /**
    * Creates a new {@link PlackettLuceModelSampleSet} with the given items.
    * 
    * @param validItems the items to store in this sample set, which have to be valid
    */
   public PlackettLuceModelSampleSet(int[] validItems) {
      super();
      this.validItems = validItems;
      this.rankings = new ArrayList<>();
      this.itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap = new HashMap<>();
      this.itemToItemIsRankedLowerOrEqualThanMap = new HashMap<>();
      this.rankingValidator = new RankingValidator(validItems);
   }


   @Override
   public void addSample(Ranking sample) {
      if (sample == null) {
         throw new NullPointerException(ERROR_GIVEN_SAMPLE_IS_NULL);
      }
      assertRankingIsValid(sample);
      rankings.add(sample);
   }


   @Override
   public void addSampleAtIndex(Ranking sample, int index) {
      if (sample == null) {
         throw new NullPointerException(ERROR_GIVEN_SAMPLE_IS_NULL);
      }
      if (index < 0) {
         throw new IndexOutOfBoundsException(ERROR_GIVEN_INDEX_IS_NEGATIVE);
      }
      assertRankingIsValid(sample);
      rankings.add(index, sample);

   }


   @Override
   public Ranking getSample(int index) {
      if (index < 0 || index >= rankings.size()) {
         throw new IndexOutOfBoundsException(ERROR_GIVEN_INDEX_OUT_OF_BOUNDS);
      }
      return rankings.get(index);
   }


   @Override
   public int getSize() {
      return rankings.size();
   }


   /**
    * Returns the number of rankings where the item with the given {@code itemId} is ranked higher
    * than the last item in the ranking.
    * 
    * @param itemId the id of the item to check
    * @return the number of rankings where the item with the given {@code itemId} is ranked higher
    *         than the last item in the ranking
    */
   public int getNumberOfRankingsWhereItemIsRankedHigherThanLast(int itemId) {
      if (itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap.containsKey(itemId)) {
         return itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap.get(itemId);
      }

      int computedCount = computeNumberOfRankingsWhereItemIsRankedHigherThanLast(itemId);
      itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap.put(itemId, computedCount);
      return computedCount;
   }


   /**
    * Computes the number of rankings where the item with the given {@code itemId} is ranked higher
    * than the last item in the ranking.
    * 
    * @param itemId the id of the item to check
    * @return the number of rankings where the item with the given {@code itemId} is ranked higher
    *         than the last item in the ranking
    */
   private int computeNumberOfRankingsWhereItemIsRankedHigherThanLast(int itemId) {
      int count = 0;
      for (Ranking ranking : rankings) {
         if (ranking.getObjectList()[ranking.getNumberOfItems() - 1] != itemId) {
            count++;
         }
      }
      return count;
   }


   /**
    * Returns {@code true} if the item with the given {@code itemId} is ranked lower or equal than
    * the given {@code rank} in the given {@code ranking}.
    * 
    * @param itemId the id of the item to check
    * @param rank the rank to check the item for
    * @param ranking the ranking where the item is in
    * @return {@code true} if the item with the given {@code itemId} is ranked lower or equal than
    *         the given {@code rank} in the given {@code ranking}
    */
   public boolean isItemRankedLowerOrEqualThan(int itemId, int rank, Ranking ranking) {
      Triple<Integer, Integer, Ranking> parameterTriple = Triple.of(itemId, rank, ranking);
      if (itemToItemIsRankedLowerOrEqualThanMap.containsKey(parameterTriple)) {
         return itemToItemIsRankedLowerOrEqualThanMap.get(parameterTriple);
      }
      boolean computedResult = computeIfItemIsRankedLowerOrEqualThan(itemId, rank, ranking);
      itemToItemIsRankedLowerOrEqualThanMap.put(parameterTriple, computedResult);
      return computedResult;
   }


   /**
    * Computes if the item with the given {@code itemId} is ranked lower or equal than the given
    * {@code rank} in the given {@code ranking}.
    * 
    * @param itemId the id of the item to check
    * @param rank the rank to check the item for
    * @param ranking the ranking where the item is in
    * @return {@code true} if the item with the given {@code itemId} is ranked lower or equal than
    *         the given {@code rank} in the given {@code ranking}
    */
   private boolean computeIfItemIsRankedLowerOrEqualThan(int itemId, int rank, Ranking ranking) {
      for (int i = rank; i < ranking.getNumberOfItems(); i++) {
         if (ranking.getObjectList()[i] == itemId) {
            return true;
         }
      }
      return false;
   }


   /**
    * Returns the number of items.
    * 
    * @return the number of items
    */
   public int getNumberOfItems() {
      return validItems.length;
   }


   /**
    * Returns the valid items.
    * 
    * @return the valid items
    */
   public int[] getValidItems() {
      return validItems;
   }


   /**
    * Returns the id of the item at the given position.
    * 
    * @param index the position of the item to return the id of
    * @return the id of the item at the given position
    * @throws IndexOutOfBoundsException if the given index is either below 0 or above the amount of
    *            valid items
    */
   public int getItemIdForItemAtIndex(int index) {
      if (index < 0 || index >= validItems.length) {
         throw new IndexOutOfBoundsException(ERROR_GIVEN_INDEX_OUT_OF_BOUNDS);
      }
      return validItems[index];
   }


   /**
    * Asserts that the given {@link Ranking} is valid in terms of the {@link PlackettLuceModel}. See
    * {@link RankingValidator} for more details on when a ranking is valid. If this is not the case
    * a {@link IllegalArgumentException} is thrown.
    * 
    * @param ranking the {@link Ranking} to check
    * @throws IllegalArgumentException if the given {@link Ranking} is invalid
    */
   private void assertRankingIsValid(Ranking ranking) {
      if (!rankingValidator.isRankingValid(ranking)) {
         throw new IllegalArgumentException(ERROR_INVALID_RANKING);
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((itemToItemIsRankedLowerOrEqualThanMap == null) ? 0 : itemToItemIsRankedLowerOrEqualThanMap.hashCode());
      result = prime * result + ((itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap == null) ? 0
            : itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap.hashCode());
      result = prime * result + ((rankings == null) ? 0 : rankings.hashCode());
      result = prime * result + Arrays.hashCode(validItems);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      PlackettLuceModelSampleSet other = (PlackettLuceModelSampleSet) obj;
      if (itemToItemIsRankedLowerOrEqualThanMap == null) {
         if (other.itemToItemIsRankedLowerOrEqualThanMap != null) {
            return false;
         }
      } else if (!itemToItemIsRankedLowerOrEqualThanMap.equals(other.itemToItemIsRankedLowerOrEqualThanMap)) {
         return false;
      }
      if (itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap == null) {
         if (other.itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap != null) {
            return false;
         }
      } else if (!itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap
            .equals(other.itemToNumberOfRankingsWhereItemIsRankedHigherThanLastMap)) {
         return false;
      }
      if (rankings == null) {
         if (other.rankings != null) {
            return false;
         }
      } else if (!rankings.equals(other.rankings)) {
         return false;
      }
      if (!Arrays.equals(validItems, other.validItems)) {
         return false;
      }
      return true;
   }

}
