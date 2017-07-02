package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import java.util.HashMap;
import java.util.HashSet;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * This class can be used to validate an instance of the {@link Ranking} class with respect to the
 * {@link PlackettLuceModel}. Using the {@link #isRankingValid(Ranking)} method, a user can check if
 * the given {@link Ranking} exclusively includes the "is preferred to" comparative operator and
 * valid items.
 * 
 * @author Alexander Hetzer
 *
 */
public class RankingValidator {

   private HashMap<Ranking, Boolean> validationCache;

   private HashSet<Integer> validItemIds;

   private int comparativeOperatorForPreference = Ranking.COMPARABLE_ENCODING;


   /**
    * Creates a new {@link RankingValidator} with the given set of valid item ids.
    * 
    * @param validItemIds the ids of items which should be considered valid
    */
   public RankingValidator(int[] validItemIds) {
      this.validItemIds = new HashSet<>();
      initializeValidItemIds(validItemIds);
      validationCache = new HashMap<>();
   }


   /**
    * Initializes the internal {@link HashSet} to store valid items from the given array of item
    * ids.
    * 
    * @param validItemIds the array of valid item ids
    */
   private void initializeValidItemIds(int[] validItemIds) {
      for (int itemId : validItemIds) {
         this.validItemIds.add(itemId);
      }
   }


   /**
    * Checks whether the given {@link Ranking} is partial in terms of its items. In particular it
    * checks that the set of valid items in this {@link RankingValidator} is larger than the item
    * set of the given ranking and that all items from the ranking's item set are contained in the
    * set of valid items.
    * 
    * @param ranking the ranking to check
    * @return {@code true} if the given {@link Ranking} is partial in terms of its items,
    *         {@code false} otherwise
    */
   public boolean isRankingPartial(Ranking ranking) {
      int[] rankingItems = ranking.getObjectList();
      if (rankingItems.length >= validItemIds.size()) {
         return false;
      }
      for (int i = 0; i < rankingItems.length; i++) {
         if (!validItemIds.contains(rankingItems[i])) {
            return false;
         }
      }
      return true;
   }


   /**
    * Checks whether the given {@link Ranking} is valid in terms of the {@link PlackettLuceModel},
    * i.e. the given {@link Ranking} exclusively includes the "is preferred to" comparative operator
    * and valid items. Returns {@code true}, if the given ranking is valid, otherwise {@code false}.
    * 
    * @param ranking the {@link Ranking} to check
    * @return {@code true}, if the given ranking is valid, otherwise {@code false}.
    */
   public boolean isRankingValid(Ranking ranking) {
      Boolean cacheResult = validationCache.get(ranking);
      if (cacheResult != null) {
         return cacheResult;
      }

      boolean isValid = doesRankingContainOnlyValidItems(ranking) && doesRankingContainOnlyValidComparativeOperators(ranking);
      validationCache.put(ranking, isValid);
      return isValid;
   }


   /**
    * Checks whether the given ranking contains only valid comparative operators. Returns
    * {@code true} if the given ranking contains only valid comparative operators, otherwise
    * {@code false}.
    * 
    * @param ranking the ranking to check
    * @return {@code true} if the given ranking contains only valid comparative operators, otherwise
    *         {@code false}
    */
   private boolean doesRankingContainOnlyValidComparativeOperators(Ranking ranking) {
      for (int comparativeOperator : ranking.getCompareOperators()) {
         if (!isValidComparativeOperator(comparativeOperator)) {
            return false;
         }
      }
      return true;
   }


   /**
    * Checks whether the given comparative operator is valid or not. Returns {@code true} if the
    * given comparative operator is valid, otherwise {@code false}.
    * 
    * @param comparativeOperator the comparative operator to check
    * @return {@code true} if the given comparative operator is valid, otherwise {@code false}
    */
   private boolean isValidComparativeOperator(int comparativeOperator) {
      return comparativeOperator == comparativeOperatorForPreference;
   }


   /**
    * Checks whether the given ranking contains only valid items. Returns {@code true} if the given
    * ranking contains only valid items, otherwise {@code false}.
    * 
    * @param ranking the ranking to check
    * @return {@code true} if the given ranking contains only valid items, otherwise {@code false}
    */
   private boolean doesRankingContainOnlyValidItems(Ranking ranking) {
      for (int itemId : ranking.getObjectList()) {
         if (!isValidItemId(itemId)) {
            return false;
         }
      }
      return true;
   }


   /**
    * Checks whether the given item id is valid or not. Returns {@code true} if the given item id is
    * valid, {@code false} if not.
    * 
    * @param itemId the item id to check
    * @return {@code true} if the given item id is valid, {@code false} if not
    */
   private boolean isValidItemId(int itemId) {
      return validItemIds.contains(itemId);
   }


}
