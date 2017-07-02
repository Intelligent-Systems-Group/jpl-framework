package de.upb.cs.is.jpl.api.tool.gson;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * This is a small tool test, testing the basic features of GSON.
 * 
 * @author Alexander Hetzer
 *
 */
public class GsonTest {

   /**
    * Tests if a custom algorithm configuration is initialized correctly from the default file.
    */
   @Test
   public void testCustomAlgorithmConfiguration() {
      TestAlgorithmConfiguration config = new TestAlgorithmConfiguration();
      config.initializeDefaultConfiguration();


      assertEquals(3.0, config.getP(), 0.1);
      assertEquals(5, config.getT());
      assertEquals("hallo", config.getGradientStepIdentifier());
   }


   /**
    * Tests if GSON can be used to overwrite only those values from the default configuration file,
    * which are defined by the user.
    */
   @Test
   public void testGsonOverwritingCapabilities() {
      Gson gson = new Gson();
      TestAlgorithmConfiguration testAlgorithmConfiguration = new TestAlgorithmConfiguration();
      try {
         String jsonString = "{\"t\":1,\"p\":2,\"gradientStepIdentifier\":\"test\",\"l\":2, \"jsonObject\" = {\"a\":\"test\"}}";
         testAlgorithmConfiguration = gson.fromJson(jsonString, TestAlgorithmConfiguration.class);

         assertEquals("test", testAlgorithmConfiguration.getJsonObject().get("a").getAsString());

         String jsonString2 = "{\"t\":1,\"gradientStepIdentifier\":\"test\",\"l\":2}";
         TestAlgorithmConfiguration config2 = gson.fromJson(jsonString2, TestAlgorithmConfiguration.class);
         testAlgorithmConfiguration.copyValues(config2);
      } catch (JsonSyntaxException ex) {
         ex.printStackTrace();
      }
      assertEquals("test", testAlgorithmConfiguration.getGradientStepIdentifier());
      assertEquals(2, testAlgorithmConfiguration.getP(), 0.1);
      assertEquals(1, testAlgorithmConfiguration.getT());
   }


   /**
    * Tests if a learning algorithm configuration is initialized correctly from the default values.
    */
   @Test
   public void testDefaultLearningAlgorithmConfigurationInitialization() {
      TestAlgorithmConfiguration config = new TestAlgorithmConfiguration();
      config.initializeDefaultConfiguration();
      assertEquals("hallo", config.getGradientStepIdentifier());
      assertEquals(3, config.getP(), 0.1);
      assertEquals(5, config.getT());
   }

}
