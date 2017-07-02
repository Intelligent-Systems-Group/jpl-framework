package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep;


import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.adam.AdamGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.fixedlearningrate.FixedLearningRateGradientStep;


/**
 * This enumeration lists all implementations of {@link IGradientStep}, which can be selected by the
 * user via the algorithm configuration or the command line. This enumeration is responsible for
 * creating the link between the identifier string of a gradient step technique and its
 * implementation.
 * 
 * If the developer looks for the implementation of a gradient step approach, given a identifier
 * string, he should call {@link #getEGradientStepByIdentifier(String)} and later on
 * {@link #createGradientStep()} on the obtained enumeration instance.
 * 
 * @author Alexander Hetzer
 *
 */
public enum EGradientStep {

   /**
    * Represents the fixed learning rate gradient step procedure.
    */
   FIXED_LEARNING_RATE_GRADIENT_STEP("fixed_learning_rate") {
      @Override
      public IGradientStep createGradientStep() {
         return new FixedLearningRateGradientStep();
      }
   },
   /**
    * Represents the {@link AdamGradientStep} procedure.
    */
   ADAM_GRADIENT_STEP("adam") {
      @Override
      public IGradientStep createGradientStep() {
         return new AdamGradientStep();
      }
   };

   private String identifier;


   /**
    * Creates a new gradient step enumeration value.
    * 
    * @param identifier a string identifying this gradient step procedure
    */
   private EGradientStep(String identifier) {
      this.identifier = identifier;
   }


   /**
    * Creates an instance of the implementation of this gradient step.
    * 
    * @return the instance of the implementation of this gradient step
    */
   public abstract IGradientStep createGradientStep();


   /**
    * Returns the identifier of this gradient step approach.
    * 
    * @return the identifier of this gradient step approach
    */
   public String getIdentifier() {
      return identifier;
   }


   /**
    * Returns the according enumeration instance linked to the given identifier. If no instance fits
    * the given identifier, {@code null} is returned.
    * 
    * @param identifier the identifier of the gradient step approach searched for
    * 
    * @return the enumeration instance of the gradient step link to the given identifier,
    *         {@code null} if none is found
    */
   public static EGradientStep getEGradientStepByIdentifier(String identifier) {
      for (EGradientStep eGradientStep : values()) {
         if (eGradientStep.identifier.equals(identifier)) {
            return eGradientStep;
         }
      }
      return null;
   }
}
