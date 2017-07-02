package de.upb.cs.is.jpl.api.math.distribution.numerical;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The {@link ApacheRandomGeneratorManager} manages the singleton instance of the
 * {@link ApacheRandomGenerator} in the jPL framework. It is responsible for creating the instance
 * and should be used to obtain it.
 * 
 * @author Alexander Hetzer
 *
 */
public class ApacheRandomGeneratorManager {

   private static RandomGenerator apacheRandomGenerator;


   /**
    * Hides the public constructor.
    */
   private ApacheRandomGeneratorManager() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the global singleton {@link ApacheRandomGenerator} instance.
    * 
    * @return the global singleton {@link ApacheRandomGenerator} instance
    */
   public static RandomGenerator getApacheRandomGenerator() {
      if (apacheRandomGenerator == null) {
         apacheRandomGenerator = new ApacheRandomGenerator();
      }
      return apacheRandomGenerator;
   }
}
