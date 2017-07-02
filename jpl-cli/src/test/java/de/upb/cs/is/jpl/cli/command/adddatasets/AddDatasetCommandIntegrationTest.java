package de.upb.cs.is.jpl.cli.command.adddatasets;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommand;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Integration test for the {@link AddDatasetCommand}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class AddDatasetCommandIntegrationTest extends AAddDatasetCommandTest {

   private static final String COMMAND_CAN_BE_EXECUTED = "COMMAND_CAN_BE_EXECUTED";
   private static final String FILE_SUCCESSFULLY_ADDED = "FILE_SUCCESSFULLY_ADDED";
   private static final String FILE_DOES_NOT_EXIST = "FILE_DOES_NOT_EXIST";

   private static final String DATASET_CMD_PARAMETER = "-d=";

   private static final String ABC_TXT = "abc.txt";
   private static final String DATASET_DEFAULTDATASET_DATASET_GPRF = "dataset.gprf";


   /**
    * Creates a new integration test for the {@link AddDatasetCommand}.
    */
   public AddDatasetCommandIntegrationTest() {
      super();
   }


   /**
    * Test the command line output for handling a correct path input.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    * 
    */
   @Test
   public void testAddingCorrectDatasetToSystemConfig()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String[] commandLineInput = { ECommand.ADD_DATASET.getCommandIdentifier(),
            DATASET_CMD_PARAMETER + getTestRessourcePathFor(DATASET_DEFAULTDATASET_DATASET_GPRF) };
      TestUtils.simulateCommandLineInput(commandLineInput);

      Pair<ICommand, CommandResult> commandAndResultPair = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();

      ICommand command = commandAndResultPair.getFirst();
      assertEquals(command.getFailureReason(), TestUtils.getStringByReflection(AddDatasetCommand.class, COMMAND_CAN_BE_EXECUTED));

      String expectedOutput = String.format(TestUtils.getStringByReflection(AddDatasetCommand.class, FILE_SUCCESSFULLY_ADDED),
            getTestRessourcePathFor(DATASET_DEFAULTDATASET_DATASET_GPRF));
      CommandResult commandResult = commandAndResultPair.getSecond();

      TestUtils.assertSuccessfulCommandResultWithResultAndAdditionalInformation(commandResult, DatasetFile.class, expectedOutput);
   }


   /**
    * Test whether it's possible to add a non-existing file and checks the command line output.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testAddingNonExistingFile()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String[] commandLineInput = { ECommand.ADD_DATASET.getCommandIdentifier(), DATASET_CMD_PARAMETER + getTestRessourcePathFor(ABC_TXT) };

      String errorMessage = String.format(TestUtils.getStringByReflection(AddDatasetCommand.class, FILE_DOES_NOT_EXIST),
            getTestRessourcePathFor(ABC_TXT));
      String expectedOutput = TestUtils.getInfoCommandCannotBeExecuted(ECommand.ADD_DATASET, errorMessage).trim();
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(commandLineInput);

      assertEquals(expectedOutput, consoleOutput);

      Pair<ICommand, CommandResult> commandAndCommandResultPair = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(commandAndCommandResultPair.getFirst(), AddDatasetCommand.class);
      TestUtils.assertCorrectFailedEmptyCommandResult(commandAndCommandResultPair.getSecond(), CommandCannotBeExecutedException.class);
   }

}
