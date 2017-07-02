package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent;


import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.batch.BatchGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.stochastic.StochasticGradientDescent;


/**
 * This enumeration lists all implementations of {@link IGradientDescent}, which can be selected by
 * the user via the algorithm configuration or the command line. This enumeration is responsible for
 * creating the link between the identifier string of a gradient descent technique and its
 * implementation.
 * 
 * If the developer looks for the implementation of a gradient descent approach, given a identifier
 * string, he should call {@link #getEGradientDescentByIdentifier(String)} and later on
 * {@link #createGradientDescent()} on the obtained enumeration instance.
 * 
 * @author Alexander Hetzer
 *
 */
public enum EGradientDescent {

   /**
    * Represents the {@link BatchGradientDescent} algorithm.
    */
   BATCH_GRADIENT_DESCENT("batch_gradient_descent") {
      @Override
      public IGradientDescent createGradientDescent() {
         return new BatchGradientDescent();
      }
   },
   /**
    * Represents the {@link StochasticGradientDescent} algorithm.
    */
   STOCHASTIC_GRADIENT_DESCENT("stochastic_gradient_descent") {
      @Override
      public IGradientDescent createGradientDescent() {
         return new StochasticGradientDescent();
      }
   };

   private String identifier;


   /**
    * Creates a new gradient descent enumeration value.
    * 
    * @param identifier a string identifying this gradient descent procedure
    */
   private EGradientDescent(String identifier) {
      this.identifier = identifier;
   }


   /**
    * Creates an instance of the implementation of this gradient descent.
    * 
    * @return the instance of the implementation of this gradient descent
    */
   public abstract IGradientDescent createGradientDescent();


   /**
    * Returns the identifier of this gradient descent approach.
    * 
    * @return the identifier of this gradient descent approach
    */
   public String getIdentifier() {
      return identifier;
   }


   /**
    * Returns the according enumeration instance linked to the given identifier. If no instance fits
    * the given identifier, {@code null} is returned.
    * 
    * @param identifier the identifier of the gradient step approach to search for
    * @return the enumeration instance of the gradient descent linked to the given identifier,
    *         {@code null} if none is found
    */
   public static EGradientDescent getEGradientDescentByIdentifier(String identifier) {
      for (EGradientDescent eGradientDescent : values()) {
         if (eGradientDescent.identifier.equals(identifier)) {
            return eGradientDescent;
         }
      }
      return null;
   }

}
