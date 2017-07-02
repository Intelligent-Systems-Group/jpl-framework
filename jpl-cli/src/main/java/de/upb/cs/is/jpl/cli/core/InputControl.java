package de.upb.cs.is.jpl.cli.core;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandHandler;


/**
 * 
 * The {@link InputControl} is one of the core components of the command line interface. It is
 * responsible for finding the correct {@link ICommandHandler}s for a specific user input and
 * passing the input to these handlers in a form they can handle. This is done via
 * {@link ICommandConfiguration}, automatically created from the user input by JCommander.
 * 
 * @author Alexander Hetzer
 *
 */
public class InputControl implements Observer {
   private static final Logger logger = LoggerFactory.getLogger(InputControl.class);

   private static final String TAG_INFO = "[INFO]";
   private static final String TAG_ERROR = "[ERROR]";

   private static final String WARNING_PARAMETER_PROBLEM = "A problem with the given parameters occured: %s";
   private static final String WARNING_UNKNOWN_COMMAND = "Unknown command: %s";
   private static final String WARNING_MULTIPLE_COMMAND_HANDLERS = "Multiple command handlers are registered for command {}. Conflicting handlers are {} and {}.";

   private static final String ERROR_UNSUPPORTED_UPDATE = "Unsupported update on input control with observable %s and object %s";

   private Map<String, ICommandHandler> commandIdentifierToCommandHandlerMap;
   private CommandLineParserView commandLineParserView;
   private SystemStatus systemStatus;

   private JCommander jCommander;

   private static InputControl inputControl;


   /**
    * Private default constructor to make sure the singleton pattern is enforced.
    */
   private InputControl() {
   }


   /**
    * Returns the singleton instance of the {@link InputControl}.
    * 
    * @return the singleton instance of the {@link InputControl}
    */
   public static InputControl getInputControl() {
      if (inputControl == null) {
         inputControl = new InputControl();
         inputControl.init();
      }

      return inputControl;
   }


   /**
    * Initializes the {@link InputControl}.
    */
   private void init() {
      commandIdentifierToCommandHandlerMap = new HashMap<>();
      commandLineParserView = CommandLineParserView.getCommandLineParserView();
      systemStatus = SystemStatus.getSystemStatus();
      jCommander = new JCommander();
      jCommander.setAllowAbbreviatedOptions(true);
      jCommander.setAllowParameterOverwriting(true);
      registerCommandHandlers();
      registerCommandConfigurationsInJCommander();
   }


   @Override
   public void update(Observable o, Object arg) {
      if (o instanceof CommandLineParserView && arg instanceof String[]) {
         String[] userInput = (String[]) arg;
         handleUserInput(userInput);
      } else {
         throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_UPDATE, o, arg));
      }
   }


   /* === Handling of user input === */

   /**
    * Handles the complete input of the user.
    * 
    * @param userInput the input of the user
    */
   private void handleUserInput(final String... userInput) {
      if (isUserInputNotEmpty(userInput)) {
         handleNonEmptyUserInput(userInput);
      } else {
         postWarning(String.format(WARNING_UNKNOWN_COMMAND, StringUtils.EMPTY_STRING));
         printUsage();
      }
   }


   /**
    * Checks if the given user input array is {@code null} or empty.
    * 
    * @param userInput the user input as array, split at whitespace
    * @return {@code true} if the input array is neither {@code null} nor empty
    */
   private boolean isUserInputNotEmpty(final String... userInput) {
      return userInput != null && userInput.length > 0;
   }


   /**
    * Handles a single command (including options) supplied by the user.
    * 
    * @param userCommandWithOptions the command supplied by the user including options.
    */
   private void handleNonEmptyUserInput(String... userCommandWithOptions) {
      String commandIdentifier = extractCommandIdentifierFromCommandWithOptions(userCommandWithOptions);
      ICommandHandler handlerForUserCommand = getRegisteredCommandHandlerForCommand(commandIdentifier);
      if (handlerForUserCommand == null) {
         postWarning(String.format(WARNING_UNKNOWN_COMMAND, commandIdentifier));
         printUsage();
         return;
      }

      try {
         ICommandConfiguration commandConfiguration = parseCommandConfiguration(handlerForUserCommand, userCommandWithOptions);
         ICommand command = handlerForUserCommand.handleUserCommand(commandConfiguration.getCopy());
         pushCommandToExecution(command, handlerForUserCommand);
         commandConfiguration.resetFields();
      } catch (MissingCommandException exception) {
         String warningMessage = String.format(WARNING_UNKNOWN_COMMAND, commandIdentifier);
         logger.error(warningMessage, exception);
         postWarning(warningMessage);
      } catch (ParameterException exception) {
         String warningMessage = String.format(WARNING_PARAMETER_PROBLEM, exception.getMessage());
         logger.error(warningMessage, exception);
         postWarning(warningMessage);
      }
   }


   /**
    * Prints the usage string generated by JCommander.
    */
   public void printUsage() {
      StringBuilder usageStringBuilder = new StringBuilder();
      jCommander.usage(usageStringBuilder);
      passMessageToView(usageStringBuilder.toString());
   }


   /**
    * Passes the given command to JCommander and obtains the resulting configuration object.
    * 
    * @param handlerForUserCommand the handler which should handle the command
    * @param userCommandWithOptions the command given by the user
    * 
    * @return the {@link ICommandConfiguration} of this command
    */
   private ICommandConfiguration parseCommandConfiguration(ICommandHandler handlerForUserCommand, String... userCommandWithOptions) {
      jCommander.parse(userCommandWithOptions);
      return handlerForUserCommand.getDefaultCommandConfiguration();
   }


   /**
    * Pushes the given command to the {@link SystemStatus} for execution.
    * 
    * @param command the command object to push
    * @param commandHandler the handler which created the command
    */
   private void pushCommandToExecution(ICommand command, ICommandHandler commandHandler) {
      systemStatus.pushCommand(command, commandHandler);
   }


   /**
    * Extracts the command identifier from the complete command including options.
    * 
    * @param userCommandWithOptions the command given by the user, split at whitespace
    * 
    * @return the command identifier extracted from the complete command
    */
   private String extractCommandIdentifierFromCommandWithOptions(final String... userCommandWithOptions) {
      return userCommandWithOptions[0];
   }


   /* === Posting to the view === */

   /**
    * Posts the given warning message to the log and the view.
    * 
    * @param warningMessage the warning message to post
    */
   private void postWarning(String warningMessage) {
      logger.warn(warningMessage);
      passErrorToView(warningMessage);
   }


   /**
    * Displays the given error message to the view.
    * 
    * @param errorMessage the error message to display
    */
   private void passErrorToView(String errorMessage) {
      StringBuilder stringBuilder = new StringBuilder(TAG_ERROR);
      stringBuilder.append(errorMessage);
      commandLineParserView.displayMessage(stringBuilder.toString());
   }


   /**
    * Displays the given message to the view.
    * 
    * @param message the message to display
    */
   private void passMessageToView(String message) {
      StringBuilder stringBuilder = new StringBuilder(TAG_INFO);
      stringBuilder.append(message);
      commandLineParserView.displayMessage(stringBuilder.toString());
   }


   /* === JCommander Command Configuration Registration === */

   /**
    * Registers the command configurations for all supported commands from the {@link ECommand} in
    * combination with their according command identifiers to JCommander.
    * 
    * Note: This method should only be called once at the initialization of the {@link InputControl}
    * . If this method is not called, command line parsing will be disabled. This method should only
    * be called AFTER the command handlers have been registered via
    * {@link #registerCommandHandlers()}.
    */
   private void registerCommandConfigurationsInJCommander() {
      List<ECommand> supportedCommands = ECommand.getECommands();
      for (ECommand command : supportedCommands) {
         String commandIdentifier = command.getCommandIdentifier();
         ICommandHandler commandHandler = getRegisteredCommandHandlerForCommand(commandIdentifier);
         registerCommandConfigurationInJCommander(commandIdentifier, commandHandler);
      }
   }


   /**
    * Registers the default command configuration obtained via the command handler in combination
    * with the command identifier to JCommander.
    * 
    * @param commandIdentifier the identifier of the command
    * @param commandHandler the handler for the command, used to obtain the command configuration
    */
   private void registerCommandConfigurationInJCommander(String commandIdentifier, ICommandHandler commandHandler) {
      ICommandConfiguration commandConfiguration = commandHandler.getDefaultCommandConfiguration();
      jCommander.addCommand(commandIdentifier, commandConfiguration);
   }


   /* === Command Handler Registration === */

   /**
    * Registers according command handlers for all commands listed in {@link ECommand}.
    */
   private void registerCommandHandlers() {
      if (commandIdentifierToCommandHandlerMap.isEmpty()) {
         List<ECommand> supportedCommands = ECommand.getECommands();
         for (ECommand command : supportedCommands) {
            ICommandHandler commandHandler = command.createCommandHandler();
            String commandIdentifier = command.getCommandIdentifier();
            registerCommandHandler(commandIdentifier, commandHandler);
         }
      }
   }


   /**
    * Registers the given command handler for the given command identifier.
    * 
    * @param commandIdentifier the command identifier to register
    * @param commandHandler the command handler to register
    */
   private void registerCommandHandler(String commandIdentifier, ICommandHandler commandHandler) {
      checkForHandlerConflictWhileRegistering(commandIdentifier, commandHandler);
      mapCommandHandlerToCommandIdentifier(commandIdentifier, commandHandler);
      commandHandler.addObserver(commandLineParserView);
   }


   /**
    * Puts the given command identifier and the command handler into
    * {@link InputControl#commandIdentifierToCommandHandlerMap}.
    * 
    * @param commandIdentifier the command identifier to use as key
    * @param commandHandler the command handler to use as value
    */
   private void mapCommandHandlerToCommandIdentifier(String commandIdentifier, ICommandHandler commandHandler) {
      commandIdentifierToCommandHandlerMap.put(commandIdentifier, commandHandler);
   }


   /**
    * Checks whether another command handler has already been registered for the given command. If
    * so a warning is logged.
    * 
    * @param commandHandler the command handler to register
    * @param commandIdentifier the command identifier to register
    */
   private void checkForHandlerConflictWhileRegistering(String commandIdentifier, ICommandHandler commandHandler) {
      if (isCommandHandlerRegisteredForIdentifier(commandIdentifier)) {
         logger.warn(WARNING_MULTIPLE_COMMAND_HANDLERS, commandIdentifier, commandHandler,
               commandIdentifierToCommandHandlerMap.get(commandIdentifier));
      }
   }


   /**
    * Checks whether a command handler is already register for the according identifier.
    * 
    * @param commandIdentifier the identifier to check for
    * @return {@code true}, if a handler is already registered, otherwise {@code false}
    */
   private boolean isCommandHandlerRegisteredForIdentifier(String commandIdentifier) {
      return commandIdentifierToCommandHandlerMap.containsKey(commandIdentifier);
   }


   /**
    * Returns the command handler registered for the given command.
    * 
    * @param command the identifier to get the handler for
    * 
    * @return the registered command handler, if one was found, otherwise a
    *         {@link NullCommandHandler} if none was found.
    */
   private ICommandHandler getRegisteredCommandHandlerForCommand(final String command) {
      if (isCommandHandlerRegisteredForIdentifier(command)) {
         return commandIdentifierToCommandHandlerMap.get(command);
      }
      return null;
   }


   /**
    * Simulates that the user input the given input into the tool.
    * 
    * @param userInput the input to be simulated
    */
   public void simulateUserInput(String[] userInput) {
      this.update(CommandLineParserView.getCommandLineParserView(), userInput);
   }
}
