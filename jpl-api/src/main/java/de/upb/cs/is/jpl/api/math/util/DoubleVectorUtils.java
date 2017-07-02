package de.upb.cs.is.jpl.api.math.util;


import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class offers convenience methods for double vectors.
 * 
 * @author Tanja Tornede
 *
 */
public class DoubleVectorUtils {

   /**
    * Hides the public constructor.
    */
   private DoubleVectorUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Scales the given vector.
    * 
    * @param scalar the scalar for the vector
    * @param vector the vector to scale
    * @return the multiplication of the scalar and the vector
    */
   public static double[] scaleVector(double scalar, double[] vector) {
      double[] result = new double[vector.length];
      for (int i = 0; i < vector.length; i++) {
         result[i] = scalar * vector[i];
      }
      return result;
   }


   /**
    * Returns the multiplication of two vectors of same size.
    * 
    * @param vector1 the first vector
    * @param vector2 the second vector
    * @return the multiplication of the given vectors
    */
   public static double multiplyVectors(final double[] vector1, final double[] vector2) {
      double result = 0;
      for (int i = 0; i < vector1.length; i++) {
         result += vector1[i] * vector2[i];
      }
      return result;
   }


   /**
    * Returns the euclidian distance between the two vectors
    * 
    * @param vector1 the first vector
    * @param vector2 the second vector
    * @return the euclidian distance
    */
   public static double distance(final double[] vector1, final double[] vector2) {
      double result = 0;
      for (int i = 0; i < vector2.length; i++) {
         result += Math.pow(vector1[i] - vector2[i], 2);
      }
      return Math.sqrt(result);
   }
}
