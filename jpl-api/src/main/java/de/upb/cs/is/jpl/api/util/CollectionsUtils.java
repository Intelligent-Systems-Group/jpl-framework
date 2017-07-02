package de.upb.cs.is.jpl.api.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * This util class offers convenience methods for Collections.
 * 
 * @author Tanja Tornede
 *
 */
public class CollectionsUtils {

   private static final String ERROR_GIVEN_TWO_ARRAYS_HAVE_TO_BE_OF_SAME_LENGTH = "The given two arrays have to be of the same length.";


   /**
    * Hides the public constructor.
    */
   private CollectionsUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns a deep copy of the given list.
    * 
    * @param <T> the type of the list to copy
    * 
    * @param listToCopy the list to copy
    * @return a copy of the given list
    */
   public static <T> List<T> getDeepCopyOf(List<T> listToCopy) {
      List<T> copy = new ArrayList<>(listToCopy);
      Collections.copy(copy, listToCopy);
      return copy;
   }


   /**
    * Returns a deep copy of the given Map.
    * 
    * @param <K1> the type of the key of the map
    * @param <V> the type of the value of the map
    * 
    * @param original the original map to copy
    * @return the copy of the given map
    */
   public static <K1, V> Map<K1, V> getdeepCopyOfMap(Map<K1, V> original) {
      Map<K1, V> copy = new HashMap<>();
      for (Entry<K1, V> entry : original.entrySet()) {
         copy.put(entry.getKey(), entry.getValue());
      }
      return copy;
   }


   /**
    * Copies the values of the given {@link List} into an {@code int[]} array.
    * 
    * @param integerList the values to be copied
    * @return the new {@code int[]} array
    */
   public static int[] convertIntegerListToArray(List<Integer> integerList) {
      int[] copy = new int[integerList.size()];
      for (int i = 0; i < integerList.size(); i++) {
         copy[i] = integerList.get(i);
      }
      return copy;
   }


   /**
    * Copies the values of the given {@link List} into an {@code double[]} array.
    * 
    * @param doubleList the values to be copied
    * @return the new {@code double[]} array
    */
   public static double[] convertDoubleListToArray(List<Double> doubleList) {
      double[] copy = new double[doubleList.size()];
      for (int i = 0; i < doubleList.size(); i++) {
         copy[i] = doubleList.get(i);
      }
      return copy;
   }


   /**
    * Returns the sorted key values in decreasing value order of a map.
    *
    * @param map the map with integer keys and double values
    * @return the sorted key values in decreasing value order
    */
   public static int[] getSortedKeyValuesInDecreasingValueOrder(Map<Integer, Double> map) {
      ArrayList<Integer> rankAggregationArrayList = new ArrayList<>();
      Comparator<Entry<Integer, Double>> comperatorByValueDesc = (e1, e2) -> e2.getValue().compareTo(e1.getValue());
      map.entrySet().stream().sorted(comperatorByValueDesc).forEachOrdered(e -> rankAggregationArrayList.add(e.getKey()));
      return convertIntegerListToArray(rankAggregationArrayList);
   }


   /**
    * Returns the sorted key values in decreasing value order of a map.
    *
    * @param map the map with integer keys and double values
    * @return the sorted key values in decreasing value order
    */
   public static int[] getSortedKeyValuesInIncreasingOrder(Map<Integer, Double> map) {
      ArrayList<Integer> rankAggregationArrayList = new ArrayList<>();
      Comparator<Entry<Integer, Double>> comperatorByValueDesc = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
      map.entrySet().stream().sorted(comperatorByValueDesc).forEachOrdered(e -> rankAggregationArrayList.add(e.getKey()));
      return convertIntegerListToArray(rankAggregationArrayList);
   }


   /**
    * Converts the given {@link String} array to a double array.
    * 
    * @param stringArray the {@link String} array to convert
    * @return the converted double array
    */
   public static double[] convertStringArrayToDoubleArray(String[] stringArray) {
      double[] doubleArray = new double[stringArray.length];
      for (int i = 0; i < stringArray.length; i++) {
         doubleArray[i] = Double.parseDouble(stringArray[i]);
      }
      return doubleArray;
   }


   /**
    * Returns the index of the integer element in the provided array of integers.
    * 
    * @param array the {@link Integer} array to convert
    * @param object the object to be searched
    * @return the index of object double array
    */
   public static int indexOfObjectInIntegerArray(int[] array, int object) {
      int index = -1;
      for (int i = 0; i < array.length; i++) {
         if (array[i] == object)
            index = i;
      }
      return index;
   }


   /**
    * Extends the given array by the given value.
    * 
    * @param originalArray the array to extend
    * @param additionalValue the value to add at the end of the array
    * 
    * @return an array containing the given original array, extended by the given additional value
    */
   public static double[] extendArray(double[] originalArray, double additionalValue) {
      double[] newArray = new double[originalArray.length + 1];
      for (int i = 0; i < originalArray.length; i++) {
         newArray[i] = originalArray[i];
      }
      newArray[originalArray.length] = additionalValue;
      return newArray;
   }


   /**
    * Creates a list of integers from 0,1,,,n-1 for provided value of n.
    * 
    * @param n the number of objects to be placed
    * @return the list of indexes
    */
   public static List<Integer> createListOfIndexesForProvidedNumber(int n) {
      List<Integer> listOfIndexes = new ArrayList<>();
      for (int i = 0; i < n; i++) {
         listOfIndexes.add(i);
      }
      return listOfIndexes;
   }


   /**
    * Creates a list of {@link SparseDoubleVector}s from a two dimensional array.
    * 
    * @param array the two dimensional array to create the list of {@link SparseDoubleVector}s from
    * @return a list of {@link SparseDoubleVector}s created from the two dimensional array
    */
   public static List<SparseDoubleVector> createListOfSparseDoubleVectorFromTwoDimensionalArray(double[][] array) {
      List<SparseDoubleVector> sparseDoubleVectors = new ArrayList<>();
      for (int i = 0; i < array.length; i++) {
         sparseDoubleVectors.add(new SparseDoubleVector(array[i]));
      }
      return sparseDoubleVectors;
   }


   /**
    * Checks if the given two lists of {@link SparseDoubleVector}s are equal or not.
    * 
    * @param firstList the first list to check
    * @param secondList the second list to check
    * @return {@code true}, if the given two lists of {@link SparseDoubleVector}s are equal
    */
   public static boolean areSparseDoubleVectorListsEqual(List<SparseDoubleVector> firstList, List<SparseDoubleVector> secondList) {
      for (int i = 0; i < firstList.size(); i++) {
         SparseDoubleVector vectorFromFirstList = firstList.get(i);
         SparseDoubleVector vectorFromSecondList = secondList.get(i);
         if (!vectorFromFirstList.equals(vectorFromSecondList)) {
            return false;
         }
      }
      return true;
   }


   /**
    * Returns the indices of the given array sorted in an decreasing order according to the value
    * located inside the cells.
    * 
    * @param values the array of which the sorted indices should be determined
    * @return the indices of the given array sorted in an decreasing order according to the value
    *         located inside the cells
    */
   public static int[] getIndicesSortedAccordingToValue(double[] values) {
      return IntStream.range(0, values.length).boxed().sorted((i, j) -> Double.compare(values[j], values[i])).mapToInt(value -> value)
            .toArray();
   }


   /**
    * Normalizes the given {@code vector}.
    * 
    * @param vector the vector to normalize
    */
   public static void normalize(double[] vector) {
      double sum = 0;
      for (double entry : vector) {
         sum += entry;
      }
      for (int i = 0; i < vector.length; i++) {
         vector[i] = vector[i] / sum;
      }
   }


   /**
    * Returns the absolute difference between the given vectors.
    * 
    * @param vector1 the first vector
    * @param vector2 the second vector
    * @return the absolute difference between the given vectors
    */
   public static double getAbsoluteDifferenceBetween(double[] vector1, double[] vector2) {
      if (vector1.length != vector2.length) {
         throw new IllegalArgumentException(ERROR_GIVEN_TWO_ARRAYS_HAVE_TO_BE_OF_SAME_LENGTH);
      }
      double difference = 0;
      for (int i = 0; i < vector1.length; i++) {
         difference += Math.abs(vector1[i] - vector2[i]);
      }
      return difference;
   }

}
