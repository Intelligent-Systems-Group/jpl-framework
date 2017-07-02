package de.upb.cs.is.jpl.cli.command.adddatasets;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommand;
import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommandConfiguration;
import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommandHandler;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * 
 * Unit test for the {@link AddDatasetCommand}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class AddDatasetCommandUnitTest extends AAddDatasetCommandTest {

   private static final String FILE_SUCCESSFULLY_ADDED = "FILE_SUCCESSFULLY_ADDED";
   private static final String DATA_SET_DUMMY = "DataSetDummy.gprf";
   private static final String INVALID_PATH = "abc";
   private AddDatasetCommandHandler commandHandler;
   private SystemConfiguration systemConfig;


   /**
    * Creates a new unit test for the {@link DetermineApplicableAlgorithmsCommand}.
    */
   public AddDatasetCommandUnitTest() {
      super();
   }


   /**
    * Initialize all variables.
    */
   @Before
   public void setUp() {
      systemConfig = SystemConfiguration.getSystemConfiguration();
      systemConfig.resetSystemConfiguration();
      commandHandler = new AddDatasetCommandHandler();
   }


   /**
    * Unit test to test behavior on trying to add a path that's not a file.
    */
   @Test
   public void executeCommandNotAFile() {
      AddDatasetCommandConfiguration config = new AddDatasetCommandConfiguration();
      config.setDatasetPath(getTestRessourcePath());

      ICommand command = commandHandler.handleUserCommand(config);
      assertFalse(command.canBeExecuted());
   }


   /**
    * Unit test to test behavior on trying to add an invalid path.
    */
   @Test
   public void executeCommandInvalidPath() {
      AddDatasetCommandConfiguration config = new AddDatasetCommandConfiguration();
      config.setDatasetPath(INVALID_PATH);

      ICommand command = commandHandler.handleUserCommand(config);
      assertFalse(command.canBeExecuted());
   }


   /**
    * Unit test to test behavior on successful execution. Adds a dataset to the
    * {@link SystemConfiguration} and checks the {@link CommandResult}.
    * 
    * @throws IllegalAccessException if the reflections didn't work
    * @throws IllegalArgumentException if the reflections didn't work
    * @throws SecurityException if the reflections didn't work
    * @throws NoSuchFieldException if the reflections didn't work
    */
   @Test
   public void executeCommandSuccessfully()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {

      AddDatasetCommandConfiguration config = new AddDatasetCommandConfiguration();
      String path = getTestRessourcePathFor(DATA_SET_DUMMY);
      config.setDatasetPath(path);

      ICommand command = commandHandler.handleUserCommand(config);
      assertTrue(command.canBeExecuted());
      CommandResult commandResult = command.executeCommand();

      String expectedAdditionalInformation = TestUtils.getStringByReflection(AddDatasetCommand.class, FILE_SUCCESSFULLY_ADDED);
      TestUtils.assertSuccessfulCommandResultWithResultAndAdditionalInformation(commandResult, DatasetFile.class,
            String.format(expectedAdditionalInformation, path));
   }


}
