package de.upb.cs.is.jpl.api.math.distribution.numerical;


import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;


/**
 * The {@link ApacheRandomGenerator} is an implementation of the {@link RandomGenerator} interface
 * of the Apache Math 3 Collection. It uses redirects all calls to a {@link Random}, which is
 * obtained from the {@link de.upb.cs.is.jpl.api.math.RandomGenerator}. As of this reason, this
 * class can be seen as a wrapper for the global random number generator of the jPL framework in
 * order to make it compatible with the {@link RandomGenerator} interface of Apache math library.
 * 
 * Note that this class is intentionally not implemented using a singleton pattern, in order to
 * provide the user with the freedom to create their own instances of this class, e.g. for testing.
 * Nevertheless it is treated inside the framework as if it was implemented using a singleton
 * pattern for performance reasons. The current global singleton instance of this class can be
 * obtained using the {@link ApacheRandomGeneratorManager}.
 * 
 * @author Alexander Hetzer
 *
 */
public class ApacheRandomGenerator implements RandomGenerator {

   private Random randomVariable;


   /**
    * Creates a new {@link ApacheRandomGenerator} wrapping the global random generator. For
    * performance reasons we suggest not to use the constructor manually, but to obtain a reference
    * using the {@link ApacheRandomGeneratorManager}.
    */
   public ApacheRandomGenerator() {
      this.randomVariable = de.upb.cs.is.jpl.api.math.RandomGenerator.getRNG();
   }


   @Override
   public void setSeed(int seed) {
      randomVariable.setSeed(seed);
   }


   @Override
   public void setSeed(int[] seed) {
      randomVariable.setSeed(RandomGeneratorFactory.convertToLong(seed));
   }


   @Override
   public void setSeed(long seed) {
      randomVariable.setSeed(seed);
   }


   @Override
   public void nextBytes(byte[] bytes) {
      randomVariable.nextBytes(bytes);
   }


   @Override
   public int nextInt() {
      return randomVariable.nextInt();
   }


   @Override
   public int nextInt(int n) {
      return randomVariable.nextInt(n);
   }


   @Override
   public long nextLong() {
      return randomVariable.nextLong();
   }


   @Override
   public boolean nextBoolean() {
      return randomVariable.nextBoolean();
   }


   @Override
   public float nextFloat() {
      return randomVariable.nextFloat();
   }


   @Override
   public double nextDouble() {
      return randomVariable.nextDouble();
   }


   @Override
   public double nextGaussian() {
      return randomVariable.nextGaussian();
   }

}

