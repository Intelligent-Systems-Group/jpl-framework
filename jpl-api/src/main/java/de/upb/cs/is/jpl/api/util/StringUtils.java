package de.upb.cs.is.jpl.api.util;


/**
 * This util class offers convenience methods for {@code String}.
 * 
 * @author Tanja Tornede
 *
 */
public class StringUtils {

   /** Constant empty string. */
   public static final String EMPTY_STRING = "";

   /** Constant single whitespace. */
   public static final String SINGLE_WHITESPACE = " ";

   /** Constant system dependent line break. */
   public static final String LINE_BREAK = System.getProperty("line.separator");

   /** A single colon. */
   public static final String COLON = ":";

   /** Constant colon with a single whitespace behind. */
   public static final String COLON_WITH_SINGLE_WHITESPACE_BEHIND = ": ";

   /** Constant equals sign. */
   public static final String EQUALS_SIGN = "=";

   /** Constant tab space. */
   public static final String TAB_SPACE = "\t";

   /** Constant dot. */
   public static final String DOT = ".";
   /** Constant start. */
   public static final String STAR = "*";
   /** Constant dash. */
   public static final String DASH = "-";
   /** Constant comma. */
   public static final String COMMA = ",";
   /** A comma with a single whitespace behind. */
   public static final String COMMA_WITH_SINGLE_WHITESPACE_BEHIND = ", ";
   /** Constant bitwise OR. */
   public static final String BITWISEOR = "|";

   /** Constant slash. */
   public static final String FORWARD_SLASH = "/";
   /** The opening round bracket: ( */
   public static final String ROUND_BRACKET_OPEN = "(";
   /** The closing round bracket: ) */
   public static final String ROUND_BRACKET_CLOSE = ")";
   /** The exception message to hide the utility class constructor */
   public static final String EXCEPTION_MESSAGE_ACCESS_ERROR = "Utility class";


   /**
    * Hides the public constructor.
    */
   private StringUtils() {
      throw new IllegalAccessError(EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Replaces multiple white spaces with a single one in the given string and returns the result as
    * string.
    * 
    * @param string the string to replace whitespaces at
    * @return the edited string
    */
   public static String removeMultipleWhitespaces(String string) {
      return string.trim().replaceAll("\\s+", SINGLE_WHITESPACE);
   }


   /**
    * Merges a string array into a single string where elements are separated by the given separator
    * string.
    * 
    * @param array the array to merge
    * @param seperator the separator to use
    * @return the merged string
    */
   public static String mergeStringArrayToStringWithSeperator(String[] array, String seperator) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < array.length; i++) {
         builder.append(array[i]);
         if (i < array.length - 1) {
            builder.append(seperator);
         }
      }
      return builder.toString();
   }


   /**
    * Returns a string consisting only of whitespace of the given length.
    * 
    * @param length the length of the whitespace string to be produced
    * @return the string consisting only of whitespace of the given length
    */
   public static String getWhitespaceStringOfLength(int length) {
      return repeat(StringUtils.SINGLE_WHITESPACE, length);
   }


   /**
    * Returns a string consisting only of the repetitions of the given string, based on the given
    * number of repetitions.
    * 
    * @param string the string to be repeated
    * @param numberOfRepetitions the number of repetitions
    * @return a string consisting of the given number of repetitions of the given string
    */
   public static String repeat(String string, int numberOfRepetitions) {
      StringBuilder outputBuilder = new StringBuilder(numberOfRepetitions);
      for (int i = 0; i < numberOfRepetitions; i++) {
         outputBuilder.append(string);
      }
      return outputBuilder.toString();
   }
}
