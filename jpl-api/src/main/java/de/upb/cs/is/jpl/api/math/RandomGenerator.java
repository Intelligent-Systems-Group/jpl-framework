package de.upb.cs.is.jpl.api.math;


import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.util.IOUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Generator of random values with the same seed.
 * 
 * @author Tanja Tornede
 *
 */
public class RandomGenerator {

   private static final String INITIALIZING_THE_RANDOM_GENERATOR_TO_SEED = "Initializing the random generator to seed: %d .";
   private static final String INITIALIZING_RANDOM_GENERATOR = "Random number generator not initialized; initializing to 1234.";

   private static Random randomVariable = null;
   private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);


   /** The default value for the random value seed. */
   public static final int DEFAULT_SEED = 1234;


   /**
    * Hides the public constructor.
    */
   private RandomGenerator() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);

   }


   /**
    * Initializes the random generator with the given seed.
    * 
    * @param seed the random seed
    */
   public static void initializeRNG(long seed) {
      randomVariable = new Random(seed);
      logger.debug(String.format(INITIALIZING_THE_RANDOM_GENERATOR_TO_SEED, seed));
   }


   /**
    * Returns the random variable of this class.
    * 
    * @return the random variable
    */
   public static Random getRNG() {
      if (randomVariable == null) {
         logger.warn(INITIALIZING_RANDOM_GENERATOR);
         randomVariable = new Random(DEFAULT_SEED);
      }
      return randomVariable;
   }

}
