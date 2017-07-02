package de.upb.cs.is.jpl.api.tool.gson;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;


/**
 * This class represents an algorithm configuration implemented for testing. It is used in
 * combination with the {@link GsonTest}. The components of this class do not need to make any sense
 * in particular.
 * 
 * @author Alexander Hetzer
 *
 */
public class TestAlgorithmConfiguration extends AAlgorithmConfiguration {

   private static final String TEST_ALGORITHM_CONFIGURATION = "test_algorithm_configuration";

   private int t = Integer.MAX_VALUE;

   private double p = Double.MAX_VALUE;

   private String gradientStepIdentifier = null;

   private JsonObject jsonObject;


   /**
    * Creates a test algorithm configuration.
    */
   public TestAlgorithmConfiguration() {
      super(TEST_ALGORITHM_CONFIGURATION);
   }


   @Override
   protected void validateParameters() {
      // empty as not needed for testing
   }


   /**
    * Returns the parameter t.
    * 
    * @return the parameter t
    */
   public int getT() {
      return t;
   }


   /**
    * Sets the parameter t.
    * 
    * @param t the new value for t
    */
   public void setT(int t) {
      this.t = t;
   }


   /**
    * Returns the parameter p.
    * 
    * @return the parameter p
    */
   public double getP() {
      return p;
   }


   /**
    * Sets the parameter p to the given value.
    * 
    * @param p the new value of p
    */
   public void setP(double p) {
      this.p = p;
   }


   /**
    * Return the gradient step identifier.
    * 
    * @return the gradient step identifier
    */
   public String getGradientStepIdentifier() {
      return gradientStepIdentifier;
   }


   /**
    * Set local gradient step identifier to the given identifier.
    * 
    * @param gradientStepIdentifer the identifier to set the local one to
    */
   public void setGradientStepIdentifier(String gradientStepIdentifer) {
      this.gradientStepIdentifier = gradientStepIdentifer;
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      TestAlgorithmConfiguration testConfig = (TestAlgorithmConfiguration) configuration;
      if (testConfig.t < Integer.MAX_VALUE) {
         t = testConfig.getT();
      }
      if (testConfig.p < Double.MAX_VALUE) {
         p = testConfig.getP();
      }
      gradientStepIdentifier = testConfig.getGradientStepIdentifier();
   }


   /**
    * Returns the json object parsed by GSON.
    * 
    * @return the json object parsed by GSON
    */
   public JsonObject getJsonObject() {
      return jsonObject;
   }


}
