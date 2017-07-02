package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic;


import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescentConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.EGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.IGradientDescent;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NamedParameterDefinition;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This configuration stores all parameters used by {@link LogisticRegression}. The current
 * implementation makes use of a learning rate and a gradient descent procedure. The learning rate
 * is directly passed on to the gradient descent procedure which updates the weight vector based on
 * this rate. The configuration can store any implementation of {@link IGradientDescent} as a
 * gradient descent approach.
 * 
 * @author Alexander Hetzer
 *
 */
public class LogisticRegressionConfiguration extends AAlgorithmConfiguration {


   private static final String ERROR_CANNOT_CREATE_GRADIENT_DESCENT = "Gradient descent cannot be created for the identifier: %s";
   private static final String ERROR_UNKNOWN_GRADIENT_DESCENT_IDENTIFIER = "Gradient descent identifer provided is invalid: %s";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "regression"
         + StringUtils.FORWARD_SLASH + "logistic" + StringUtils.FORWARD_SLASH + "logistic_regression";

   private static final String PARAMETER_NAME_GRADIENT_DESCENT = "gradient_descent";
   private static final String PARAMETER_NAME_LEARNING_RATE = "learning_rate";

   @SerializedName(PARAMETER_NAME_GRADIENT_DESCENT)
   protected NamedParameterDefinition gradientDescentDefinition;

   protected transient IGradientDescent gradientDescent;
   protected transient EGradientDescent eGradientDescent;

   @SerializedName(PARAMETER_NAME_LEARNING_RATE)
   private double learningRate = Double.MAX_VALUE;


   /**
    * Creates a default logistic regression configuration.
    */
   public LogisticRegressionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      validateAndSetGradientDescent();
   }


   /**
    * Sets up the gradient descent for the given dataset.
    * 
    * @param dataset the dataset the gradient descent technique should work with
    */
   public void setupGradientDescent(List<Triple<IVector, Double, Double>> dataset) {
      this.gradientDescent.setDataset(dataset);
   }


   /**
    * Validates and sets the local gradient descent technique based on the gradient descent
    * definition given.
    * 
    * @throws ParameterValidationFailedException if decoding the gradient descent definition yields
    *            any problems
    */
   private void validateAndSetGradientDescent() throws ParameterValidationFailedException {
      eGradientDescent = EGradientDescent.getEGradientDescentByIdentifier(gradientDescentDefinition.getIdentifier());
      if (eGradientDescent == null) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_UNKNOWN_GRADIENT_DESCENT_IDENTIFIER, gradientDescentDefinition.getIdentifier()));
      } else {
         gradientDescent = eGradientDescent.createGradientDescent();
         if (gradientDescent == null) {
            throw new ParameterValidationFailedException(
                  String.format(ERROR_CANNOT_CREATE_GRADIENT_DESCENT, gradientDescentDefinition.getIdentifier()));
         }
         AGradientDescentConfiguration configuration = gradientDescent.getConfiguration();
         configuration.overrideConfiguration(gradientDescentDefinition.getParameters());
         gradientDescent.setConfiguration(configuration);
         gradientDescent.getConfiguration().setLearningRate(learningRate);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      LogisticRegressionConfiguration castedConfiguration = (LogisticRegressionConfiguration) configuration;
      if (castedConfiguration.gradientDescentDefinition != null) {
         this.gradientDescentDefinition = castedConfiguration.gradientDescentDefinition;
      }
      if (castedConfiguration.learningRate < Double.MAX_VALUE) {
         this.learningRate = castedConfiguration.learningRate;
      }
   }


   /**
    * Returns the used gradient descent procedure.
    * 
    * @return the used gradient descent procedure
    */
   public IGradientDescent getGradientDescent() {
      return gradientDescent;
   }


   /**
    * Sets the used gradient descent procedure to the given gradient descent procedure.
    * 
    * @param gradientDescent the new gradient descent procedure to use
    */
   public void setGradientDescent(IGradientDescent gradientDescent) {
      this.gradientDescent = gradientDescent;
   }


   /**
    * Returns the used learning rate.
    * 
    * @return the used learning rate
    */
   public double getLearningRate() {
      return learningRate;
   }


   /**
    * Sets the used learning rate to the given learning rate.
    * 
    * @param learningRate the learning rate to use
    */
   public void setLearningRate(double learningRate) {
      this.learningRate = learningRate;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_LEARNING_RATE + StringUtils.COLON + learningRate + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + PARAMETER_NAME_GRADIENT_DESCENT + StringUtils.COLON + gradientDescent.toString() + StringUtils.SINGLE_WHITESPACE
            + StringUtils.ROUND_BRACKET_OPEN + gradientDescent.getConfiguration().toString() + StringUtils.ROUND_BRACKET_CLOSE;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((gradientDescentDefinition == null) ? 0 : gradientDescentDefinition.hashCode());
      long temp;
      temp = Double.doubleToLongBits(learningRate);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      LogisticRegressionConfiguration other = (LogisticRegressionConfiguration) obj;
      if (gradientDescentDefinition == null) {
         if (other.gradientDescentDefinition != null)
            return false;
      } else if (!gradientDescentDefinition.equals(other.gradientDescentDefinition))
         return false;
      if (Double.doubleToLongBits(learningRate) != Double.doubleToLongBits(other.learningRate))
         return false;
      return true;
   }

}
