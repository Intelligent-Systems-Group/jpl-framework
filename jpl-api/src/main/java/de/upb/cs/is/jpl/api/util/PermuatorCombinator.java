package de.upb.cs.is.jpl.api.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This class provide the utils to generate the permutations and combinations for the given list of
 * object and store them in a list of integers array.
 * 
 * @author Pritha Gupta
 *
 */
public class PermuatorCombinator {


   private static List<Integer[]> permutations;
   private static List<Integer[]> combinations;


   /**
    * Hides the public constructor.
    */
   private PermuatorCombinator() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Permutate the list and store all possible permutations in the list of integer array.
    * 
    * 
    * @param listofObjects the list of objects for which all possible permutations are required
    * @param k the recursion parameter to keep track of all permutations
    */
   public static void permutate(List<Integer> listofObjects, int k) {
      for (int i = k; i < listofObjects.size(); i++) {
         Collections.swap(listofObjects, i, k);
         permutate(listofObjects, k + 1);
         Collections.swap(listofObjects, k, i);
      }
      if (k == listofObjects.size() - 1) {
         permutations.add(listofObjects.toArray(new Integer[listofObjects.size()]));
      }
   }


   /**
    * This function calculates the permutations and combinations for objects provided in list of
    * integers of length n taken r at a time (nPr and nCr) and stores them in a list of integer
    * arrays.
    * 
    * @param listofObjects the list of objects as integers
    * @param n the number of different items for which permutations or combinations to be found
    * @param r the number of them appearing in each arrangement
    * @param index the recursion parameter to keep track of all combinations, it tracks the main
    *           object list
    * @param data the list of integer objects which stores the r objects combination
    * @param i the recursion parameter to keep track of the index of main object list
    */
   private static void combinationUtil(List<Integer> listofObjects, int n, int r, int index, List<Integer> data, int i) {
      // Current combination is ready to be printed, print it
      if (index == r) {
         List<Integer> oneCombination = new ArrayList<>();
         for (int j = 0; j < r; j++) {
            oneCombination.add(data.get(j));
         }
         permutate(oneCombination, 0);
         combinations.add(oneCombination.toArray(new Integer[oneCombination.size()]));
         return;
      }

      // When no more elements are there to put in data[]
      if (i >= n)
         return;

      // current is included, put next at next location
      data.set(index, listofObjects.get(i));
      combinationUtil(listofObjects, n, r, index + 1, data, i + 1);

      // current is excluded, replace it with next (Note that i+1 is passed, but index is not
      // changed)
      combinationUtil(listofObjects, n, r, index, data, i + 1);
   }


   /**
    * The main function that calculates all combinations and permutations for listofObjects of size
    * n taken r at a time. This function mainly uses {@link PermuatorCombinator#combinationUtil}.
    * 
    * 
    * @param listofObjects the list of objects as integers
    * @param n the number of different items for which permutations or combinations to be found
    * @param r the number of them appearing in each arrangement
    */
   public static void calculatePermutationsAndCombination(List<Integer> listofObjects, int n, int r) {
      // A temporary listofObjectsay to store all combination one by one
      List<Integer> data = new ArrayList<>();
      for (int i = 0; i < r; i++) {
         data.add(0);
      }
      // Print all combination using temporary listofObjectsay 'data[]'
      combinationUtil(listofObjects, n, r, 0, data, 0);
   }


   /**
    * Calculate and get all possible permeations of r elements in a given list of objects as
    * integers of size n.
    * 
    * @param listofObjects the list of objects as integers
    * @param r the number of them appearing in each arrangement
    * @return the list of integer arrays containing all permutations
    */
   public static List<Integer[]> getPermutationsnPr(List<Integer> listofObjects, int r) {
      permutations = new ArrayList<>();
      combinations = new ArrayList<>();
      int n = listofObjects.size();
      if (r == 0) {
         permutate(listofObjects, 0);
      } else {
         calculatePermutationsAndCombination(listofObjects, n, r);
      }
      return permutations;
   }


   /**
    * Calculate and get all possible combinations of r elements in a given list of objects as
    * integers of size n.
    * 
    * @param listofObjects the list of objects as integers
    * @param r the number of them appearing in each arrangement
    * @return the list of integer arrays containing all combinations
    */
   public static List<Integer[]> getCombinationsnCr(List<Integer> listofObjects, int r) {
      combinations = new ArrayList<>();
      permutations = new ArrayList<>();
      int n = listofObjects.size();
      calculatePermutationsAndCombination(listofObjects, n, r);
      return combinations;
   }

}
