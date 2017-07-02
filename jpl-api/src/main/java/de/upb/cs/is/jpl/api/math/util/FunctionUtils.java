package de.upb.cs.is.jpl.api.math.util;


import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class offers utility methods in functions.
 * 
 * @author Alexander Hetzer
 *
 */
public class FunctionUtils {

   /**
    * Hides the public constructor.
    */
   private FunctionUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Computes the value of the logistic function for the given argument.
    * 
    * @param argument the argument of the logistic function
    * 
    * @return the value of the logistic function for the given argument
    */
   public static double logisticFunction(double argument) {
      // make sure we do not risk a double over- or underflow leading to a NaN result here
      if (argument >= 709.78) {
         return 1.0;
      } else if (argument <= -744.44) {
         return 0.0;
      }
      return Math.exp(argument) / (1 + Math.exp(argument));
   }

}
