/**
 * 
 */
package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This Object contains the data for one relative ranking by encoding it into two arrays: The
 * compared objects and the compare operators.
 * 
 * The compared objects are identified by their IDs.
 * 
 * The encodings for compare operators are:
 * <ul>
 * <li>1 : The Objects are not comparable</li>
 * <li>2 : The Objects are equal</li>
 * <li>3 : The Objects are in a ordered relation</li>
 * <li>4 : Opening brackets</li>
 * <li>5 : closing brackets</li>
 * </ul>
 * In the case of two operators following each other, e.g. " &gt; {" these corresponding values of
 * "3" and "5" are concatenated into a "35".
 * 
 * @author Sebastian Osterbrink
 */
public class Ranking {


   /**
    * The character for a "not comparable" operator.
    */
   public static final char NOT_COMPARABLE_OPERATOR = ',';
   /**
    * The character for an "equal" operator.
    */
   public static final char EQUAL_OPERATOR = '=';
   /**
    * The character for a "comparable" operator.
    */
   public static final char COMPARABLE_OPERATOR = '>';
   /**
    * The character for an "opening brackets" operator.
    */
   public static final char OPENING_BRACKETS_OPERATOR = '{';
   /**
    * The character for a "closing brackets" operator.
    */
   public static final char CLOSING_BRACKETS_OPERATOR = '}';

   /**
    * The internal encoding for a "not comparable" operator.
    */
   public static final int NOT_COMPARABLE_ENCODING = 1;
   /**
    * The internal encoding for an "equal" operator.
    */
   public static final int EQUALS_ENCODING = 2;
   /**
    * The internal encoding for a "comparable" operator.
    */
   public static final int COMPARABLE_ENCODING = 3;
   /**
    * The internal encoding for an "opening brackets" operator.
    */
   public static final int OPENING_BRACKETS_ENCODING = 4;
   /**
    * The internal encoding for a "closing brackets" operator.
    */
   public static final int CLOSING_BRACKETS_ENCODING = 5;


   protected int[] objectList;

   protected int[] compareOperators;

   protected static final String UNKNOWN_COMPARE_OPERATOR = "Unknown compare operator: \"%s\" Please check your dataset file!";


   /**
    * Creates a {@link Ranking} object which encodes ranking information. It consists of an array of
    * compared objects and the array of used compare operators.
    * 
    * @param objectList an array of object IDs which are ranked
    * @param compareOperators an array of the used compared operators
    */
   public Ranking(int[] objectList, int[] compareOperators) {
      this.objectList = objectList;
      this.compareOperators = compareOperators;
   }


   /**
    * Convert the given character to the internal encoding used by {@link Ranking}.
    * 
    * @param currentChar the character to convert
    * @return the encoded character
    * @throws ParsingFailedException if the character is not a valid character for {@link Ranking}
    */
   public static String encodeOperator(char currentChar) throws ParsingFailedException {
      // Blank spaces do nothing
      if (Character.isWhitespace(currentChar))
         return StringUtils.EMPTY_STRING;
      switch (currentChar) {
         case COMPARABLE_OPERATOR:
            return Integer.toString(Ranking.COMPARABLE_ENCODING);
         case EQUAL_OPERATOR:
            return Integer.toString(Ranking.EQUALS_ENCODING);
         case NOT_COMPARABLE_OPERATOR:
            return Integer.toString(Ranking.NOT_COMPARABLE_ENCODING);
         case OPENING_BRACKETS_OPERATOR:
            return Integer.toString(Ranking.OPENING_BRACKETS_ENCODING);
         case CLOSING_BRACKETS_OPERATOR:
            return Integer.toString(Ranking.CLOSING_BRACKETS_ENCODING);
         default:
            throw new ParsingFailedException(String.format(UNKNOWN_COMPARE_OPERATOR, currentChar));
      }
   }


   /**
    * Convert the given character encoding back to the real value.
    * 
    * @param operator the character to convert
    * @return the encoded character
    */
   public static String decodeOperator(int operator) {
      if (operator > 10) {
         return decodeOperator(operator / 10) + decodeOperator(operator % 10);
      } else {
         switch (operator) {
            case COMPARABLE_ENCODING:
               return COMPARABLE_OPERATOR + StringUtils.EMPTY_STRING;
            case EQUALS_ENCODING:
               return EQUAL_OPERATOR + StringUtils.EMPTY_STRING;
            case NOT_COMPARABLE_ENCODING:
               return NOT_COMPARABLE_OPERATOR + StringUtils.EMPTY_STRING;
            case OPENING_BRACKETS_ENCODING:
               return OPENING_BRACKETS_OPERATOR + StringUtils.EMPTY_STRING;
            case CLOSING_BRACKETS_ENCODING:
               return CLOSING_BRACKETS_OPERATOR + StringUtils.EMPTY_STRING;
            default:
               return StringUtils.EMPTY_STRING;
         }
      }

   }


   /**
    * Returns the array of compared objects for ranking.
    * 
    * @return the compared objects
    */
   public int[] getObjectList() {
      return this.objectList;
   }


   /**
    * Returns the ordering for ranking. The ranking objects should start from 1 in increasing order.
    * This method is not applicable for partial ranking.
    * 
    * @return the ordering for ranking
    */
   public int[] getOrderingForRanking() {
      return getOrderingFromRanking(objectList);
   }


   /**
    * Returns the ordering for the given ranking. The ranking objects should start from 1 in
    * increasing order. This method is not applicable for partial ranking.
    *
    * @param rankingObjectList the ranking object list
    * @return the ordering for ranking
    */
   public static int[] getOrderingFromRanking(int[] rankingObjectList) {
      int[] orderingForRanking = new int[rankingObjectList.length];

      for (int i = 0; i < orderingForRanking.length; i++) {
         orderingForRanking[rankingObjectList[i]] = i;
      }
      return orderingForRanking;
   }


   /**
    * Returns the array of compare operators .
    * 
    * @return the compare operators
    */
   public int[] getCompareOperators() {
      return this.compareOperators;
   }


   /**
    * Returns the number of items.
    * 
    * @return the number of items
    */
   public int getNumberOfItems() {
      return objectList.length;
   }


   @Override
   public String toString() {
      StringBuilder rankingString = new StringBuilder();
      int operatorOffset = 0;
      if (compareOperators[0] == OPENING_BRACKETS_ENCODING) {
         rankingString.append(OPENING_BRACKETS_OPERATOR);
         rankingString.append(StringUtils.SINGLE_WHITESPACE);
         operatorOffset = 1;
      }
      for (int i = 0; i < objectList.length; i++) {
         rankingString.append(ADatasetParser.ITEM_MARKER);
         rankingString.append(objectList[i]);
         if (compareOperators.length > i + operatorOffset) {
            rankingString.append(decodeOperator(compareOperators[i + operatorOffset]));
         }
      }
      return rankingString.toString();
   }


   @Override
   public boolean equals(Object comparedObject) {
      if (!(comparedObject instanceof Ranking))
         return false;

      Ranking secondRanking = (Ranking) comparedObject;
      for (int i = 0; i < compareOperators.length; i++) {
         if (compareOperators[i] != secondRanking.compareOperators[i])
            return false;
      }
      for (int i = 0; i < objectList.length; i++) {
         if (objectList[i] != secondRanking.objectList[i])
            return false;
      }
      return true;
   }


   @Override
   public int hashCode() {
      int hashCode = 1;
      hashCode = 31 * hashCode + (Arrays.hashCode(compareOperators));
      hashCode = 31 * hashCode + (Arrays.hashCode(objectList));
      return hashCode;
   }


   /**
    * Creates an array of compare operator for the given labels of ranking.
    *
    * @param labels the labels of the an ranking
    * @return the compare operator for the ranking
    */
   public static int[] createCompareOperatorArrayForLabels(final int[] labels) {
      int[] comparisonOperators = new int[labels.length - 1];
      for (int index = 0; index < comparisonOperators.length; index++) {
         comparisonOperators[index] = COMPARABLE_ENCODING;
      }
      return comparisonOperators;
   }

}
