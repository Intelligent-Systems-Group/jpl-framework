package de.upb.cs.is.jpl.cli.tools.jcommander;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;


/**
 * This class tests the commands functionality of JCommander.
 * 
 * @author Alexander Hetzer
 *
 */
public class JCommanderTest {

   private static final String OPTION_CHECK = "--check";
   private static final String OPTION_AUTHOR = "--author=Tester";

   private static final String ALGORITHM_RAKEL = "rakel";
   private static final String ALGORITHM_CLASSIFIER_CHAINS = "classifier_chains";
   private static final String AUTHOR_TESTER = "Tester";

   private static final String COMMAND_SET_ALGORITHMS = "set_algorithms";

   private JCommander jCommander;
   private TestJCommandConfiguration sampleCommandConfiguration;


   /**
    * Initializes JCommander and a {@link TestJCommandConfiguration} and adds it to the commands
    * JCommander listens to.
    */
   @Before
   public void setupJCommanderTest() {
      jCommander = new JCommander();
      sampleCommandConfiguration = new TestJCommandConfiguration();
      jCommander.addCommand(COMMAND_SET_ALGORITHMS, sampleCommandConfiguration);
   }


   /**
    * Tests if JCommander correctly parses a command.
    */
   @Test
   public void testCommandConfigurationSupport() {
      jCommander.parse(COMMAND_SET_ALGORITHMS, OPTION_CHECK, OPTION_AUTHOR, ALGORITHM_CLASSIFIER_CHAINS, ALGORITHM_RAKEL);

      assertEquals(COMMAND_SET_ALGORITHMS, jCommander.getParsedCommand());
      assertTrue(sampleCommandConfiguration.getCheckAlgorithmsOnSuitability());
      assertEquals(AUTHOR_TESTER, sampleCommandConfiguration.getAuthor());
      assertEquals(Arrays.asList(ALGORITHM_CLASSIFIER_CHAINS, ALGORITHM_RAKEL), sampleCommandConfiguration.getAlgorithms());

   }
}
