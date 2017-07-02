package de.upb.cs.is.jpl.cli.util;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.core.InputControl;
import de.upb.cs.is.jpl.cli.core.SystemStatus;
import de.upb.cs.is.jpl.cli.exception.command.NullException;


/**
 * This class provides some useful methods which can be used to write tests.
 * 
 * @author Tanja Tornede
 *
 */
public class TestUtils {

   private static final String ERROR_COMMAND_NOT_INSTANCE_OF_X = "The command is not instance of %s.";
   private static final String ERROR_COMMAND_EXECUTED_SUCCESSFULLY = "The given command is executed successfully, but should not be.";
   private static final String ERROR_COMMAND_NOT_EXECUTED_SUCCESSFULLY = "The command is not executed successfully.";

   private static final String ERROR_RESULT_OF_COMMAND_RESULT_IS_NOT_INSTANCE_OF_X = "The result of the given command result is not instance of %s.";
   private static final String ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_BE_NULL = "The result of the command result is not null, but should be null.";
   private static final String ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_NOT_BE_NULL = "The result of the given command result is null, but should not be null.";

   private static final String ERROR_ADDITIONAL_INFORMATION_OF_COMMAND_RESULT_NOT_EXPECTED = "The additional information of the given command result is not the expected value: \"%s\" ";
   private static final String ERROR_EXCEPTION_OF_COMMAND_RESULT_NOT_INSTANCE_OF_X = "The exception of the given command result is not instance of %S, but should so.";

   private static final String COMMANDLINE_OUTPUT_JPL = "\\[JPL\\] ";
   private static final String REGEX_AT_LEAST_ONE_WHITESPACE = "(  )+";

   private static final String REFLECTION_SYSTEMSTATUS_COMMAND_HISTORY = "commandHistory";
   private static final String REFLECTION_SYSTEMSTATUS_WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER = "WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER";
   private static final String REFLECTION_SYSTEMSTATUS_INFO_STARTING_COMMAND_EXECUTION = "INFO_STARTING_COMMAND_EXECUTION";

   private static PrintStream correctOutputStream = System.out;

   /** A delta value to compare doubles. */
   public static final double DOUBLE_DELTA = 0.0001;


   /**
    * Redirects the output stream to a {@link ByteArrayOutputStream}.
    * 
    * @return the new source for output
    */
   private static ByteArrayOutputStream redirectOutputStream() {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(byteArrayOutputStream);
      correctOutputStream = System.out;
      System.setOut(ps);
      return byteArrayOutputStream;
   }


   /**
    * Undoes the redirection of the output stream.
    */
   private static void undoOutputStreamRedirection() {
      System.out.flush();
      System.setOut(correctOutputStream);
   }


   /**
    * Returns the list of pairs of {@link ICommand} and {@link CommandResult} of the command history
    * of the system status.
    * 
    * @return a list of pairs of {@link ICommand} and {@link CommandResult} of the command history
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @SuppressWarnings("unchecked")
   public static List<Pair<ICommand, CommandResult>> getCommandHistoryOfSystemStatus()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      SystemStatus systemStatus = SystemStatus.getSystemStatus();
      Field commandHistoryField = systemStatus.getClass().getDeclaredField(REFLECTION_SYSTEMSTATUS_COMMAND_HISTORY);
      commandHistoryField.setAccessible(true);
      List<Pair<ICommand, CommandResult>> commandHistory = (List<Pair<ICommand, CommandResult>>) commandHistoryField.get(systemStatus);
      return commandHistory;
   }


   /**
    * Returns the latest pair of {@link ICommand} and {@link CommandResult} of the command history
    * of the system status.
    * 
    * @return the latest pair of {@link ICommand} and {@link CommandResult} of the command history
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   public static Pair<ICommand, CommandResult> getLatestPairOfCommandAndCommandResultInCommandHistory()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      List<Pair<ICommand, CommandResult>> commandHistory = getCommandHistoryOfSystemStatus();
      return commandHistory.get(commandHistory.size() - 1);
   }


   /**
    * Simulates the given command line input as user input.
    * 
    * @param commandLineInput the user input to be simulated
    */
   public static void simulateCommandLineInput(String commandLineInput[]) {
      InputControl.getInputControl().simulateUserInput(commandLineInput);
   }


   /**
    * Simulates the given command line input as user input and returns the console output.
    * 
    * @param commandLineInput the user input to be simulated
    * @return the console output of the given command while execution
    */
   public static String simulateCommandLineInputAndReturnConsoleOutput(String commandLineInput[]) {
      ByteArrayOutputStream outputStream = redirectOutputStream();
      InputControl.getInputControl().simulateUserInput(commandLineInput);
      undoOutputStreamRedirection();
      return outputStream.toString().replaceAll(COMMANDLINE_OUTPUT_JPL, StringUtils.EMPTY_STRING).trim()
            .replaceAll(REGEX_AT_LEAST_ONE_WHITESPACE, StringUtils.EMPTY_STRING);
   }


   /**
    * Checks if the given {@link ICommand} is instance of the given class.
    * 
    * @param command the {@link ICommand} to check
    * @param type the class the given #command is instance of
    */
   public static void assertCorrectCommandType(ICommand command, Class<?> type) {
      if (!(type.isInstance(command))) {
         Assert.fail(String.format(ERROR_COMMAND_NOT_INSTANCE_OF_X, type.getName()));
      }
   }


   /**
    * Checks if the given {@link CommandResult} is executed successfully, if its result is not null
    * and if the additional information are empty.
    * 
    * @param commandResult the {@link CommandResult} to check
    */
   public static void assertCorrectSuccessfulCommandResult(CommandResult commandResult) {
      if (!commandResult.isExecutedSuccessfully()) {
         Assert.fail(ERROR_COMMAND_NOT_EXECUTED_SUCCESSFULLY);
      }
      if (!NullException.class.isInstance(commandResult.getException())) {
         Assert.fail(String.format(ERROR_EXCEPTION_OF_COMMAND_RESULT_NOT_INSTANCE_OF_X, NullException.class.getName()));
      }
      if (commandResult.getResult() == null) {
         Assert.fail(ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_NOT_BE_NULL);
      }
      if (!commandResult.getAdditionalInformation().equals(StringUtils.EMPTY_STRING)) {
         Assert.fail(String.format(ERROR_ADDITIONAL_INFORMATION_OF_COMMAND_RESULT_NOT_EXPECTED, StringUtils.EMPTY_STRING));
      }
   }


   /**
    * Checks if the given {@link CommandResult} is executed successfully, if its result is not null
    * and if it is instance of the given {@code resultType}, and if the additional information are
    * empty.
    * 
    * @param commandResult the {@link CommandResult} to check
    * @param resultType the type the result of the given {@code commandResult} has to be instance of
    */
   public static void assertCorrectSuccessfulCommandResultWhichIsNotEmpty(CommandResult commandResult, Class<?> resultType) {
      assertSuccessfulCommandResultWithResultAndAdditionalInformation(commandResult, resultType, StringUtils.EMPTY_STRING);
   }


   /**
    * Checks if the given {@link CommandResult} is executed successfully, if its result is not null
    * and if it is instance of the given {@code resultType}, and if the additional information
    * contains the expected String.
    * 
    * @param commandResult the {@link CommandResult} to check
    * @param resultType the type the result of the given {@code commandResult} has to be instance of
    * @param additionalInformation the expected content of the additional information field
    */
   public static void assertSuccessfulCommandResultWithResultAndAdditionalInformation(CommandResult commandResult, Class<?> resultType,
         String additionalInformation) {
      if (!commandResult.isExecutedSuccessfully()) {
         Assert.fail(ERROR_COMMAND_NOT_EXECUTED_SUCCESSFULLY);
      }
      if (!NullException.class.isInstance(commandResult.getException())) {
         Assert.fail(String.format(ERROR_EXCEPTION_OF_COMMAND_RESULT_NOT_INSTANCE_OF_X, NullException.class.getName()));
      }
      if (commandResult.getResult() == null) {
         Assert.fail(ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_NOT_BE_NULL);
      }
      if (!resultType.isInstance(commandResult.getResult())) {
         Assert.fail(String.format(ERROR_RESULT_OF_COMMAND_RESULT_IS_NOT_INSTANCE_OF_X, resultType.getName()));
      }
      if (!commandResult.getAdditionalInformation().equals(additionalInformation)) {
         Assert.fail(String.format(ERROR_ADDITIONAL_INFORMATION_OF_COMMAND_RESULT_NOT_EXPECTED, additionalInformation));
      }
   }


   /**
    * Checks if the every {@link CommandResult} of the given list of {@code CommandResult}'s is
    * executed successfully, if its result is not null and instance of the given class, and if the
    * additional information are empty.
    * 
    * @param commandResultList a list of {@link CommandResult}'s to check
    * @param resultType the class the result of the command result has to be instance of
    */
   public static void assertCorrectSuccessfulListOfCommandResultsWhichAreNotEmpty(List<CommandResult> commandResultList,
         Class<?> resultType) {
      for (CommandResult commandResult : commandResultList) {
         assertCorrectSuccessfulCommandResultWhichIsNotEmpty(commandResult, resultType);
      }
   }


   /**
    * Checks if the given {@link CommandResult} is not executed successfully as of the given
    * exception type, if its result is null and if the additional information are empty.
    * 
    * @param commandResult the {@link CommandResult} to check
    * @param exceptionType the exception type as of the given command result is not executed
    *           successfully
    */
   public static void assertCorrectFailedEmptyCommandResult(CommandResult commandResult, Class<?> exceptionType) {
      assertCorrectFailedCommandResult(commandResult, exceptionType);
      if (commandResult.getResult() != null) {
         Assert.fail(ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_BE_NULL);
      }
   }


   /**
    * Checks if the given {@link CommandResult} is not executed successfully as of the given
    * exception type, if its result is not null and if the additional information are empty.
    * 
    * @param commandResult the {@link CommandResult} to check
    * @param exceptionType the exception type as of the given command result is not executed
    *           successfully
    */
   public static void assertCorrectFailedEmptyCommandResultHavingResult(CommandResult commandResult, Class<?> exceptionType) {
      assertCorrectFailedCommandResult(commandResult, exceptionType);
      if (commandResult.getResult() == null) {
         Assert.fail(ERROR_RESULT_OF_COMMAND_RESULT_SHOULD_NOT_BE_NULL);
      }
   }


   /**
    * Checks if the given {@link CommandResult} is not executed successfully as of the given
    * exception type and if the additional information are empty.
    * 
    * @param commandResult the {@link CommandResult} to check
    * @param exceptionType the exception type as of the given command result is not executed
    *           successfully
    */
   private static void assertCorrectFailedCommandResult(CommandResult commandResult, Class<?> exceptionType) {
      if (commandResult.isExecutedSuccessfully()) {
         Assert.fail(ERROR_COMMAND_EXECUTED_SUCCESSFULLY);
      }
      if (!exceptionType.isInstance(commandResult.getException())) {
         Assert.fail(String.format(ERROR_EXCEPTION_OF_COMMAND_RESULT_NOT_INSTANCE_OF_X, exceptionType));
      }
      if (!commandResult.getAdditionalInformation().equals(StringUtils.EMPTY_STRING)) {
         Assert.fail(ERROR_ADDITIONAL_INFORMATION_OF_COMMAND_RESULT_NOT_EXPECTED);
      }
   }


   /**
    * Returns the info string that is printed to the console at the beginning of execution of a
    * command.
    * 
    * @param eCommand the enum of the command which was executed
    * @return info string that is printed to the console at the beginning of execution of a command
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   public static String getInfoStartingCommandExecution(ECommand eCommand)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_INFO_STARTING_COMMAND_EXECUTION = getStringByReflection(SystemStatus.class,
            REFLECTION_SYSTEMSTATUS_INFO_STARTING_COMMAND_EXECUTION);

      StringBuilder builder = new StringBuilder();
      builder.append(String.format(reflectionString_INFO_STARTING_COMMAND_EXECUTION, eCommand.getCommandIdentifier()));
      builder.append(StringUtils.LINE_BREAK);

      return builder.toString();
   }


   /**
    * Returns the info string that is printed to the console if a command cannot be executed.
    * 
    * @param eCommand the enum of the command which was executed
    * @param exceptionMessage the expected reason why the command cannot be executed
    * @return info string that is printed to the console if a command cannot be executed
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   public static String getInfoCommandCannotBeExecuted(ECommand eCommand, String exceptionMessage)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER = getStringByReflection(SystemStatus.class,
            REFLECTION_SYSTEMSTATUS_WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER);

      StringBuilder builder = new StringBuilder();
      builder.append(String.format(reflectionString_WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER, eCommand.getCommandIdentifier(),
            exceptionMessage));
      builder.append(StringUtils.LINE_BREAK);

      return builder.toString();
   }


   /**
    * Returns the string form the given declared field of the given class to reflect.
    * 
    * @param classToReflect the class containing a member variable with the given name
    * @param declaredFieldToReflect the declared field of the given class to reflect
    * @return the string reflected from the given class
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   public static String getStringByReflection(Class<?> classToReflect, String declaredFieldToReflect)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Field f = classToReflect.getDeclaredField(declaredFieldToReflect);
      f.setAccessible(true);
      return ((String) f.get(null));
   }

}
