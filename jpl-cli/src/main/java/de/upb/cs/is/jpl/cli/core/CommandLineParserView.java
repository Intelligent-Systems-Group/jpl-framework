package de.upb.cs.is.jpl.cli.core;


import java.util.Observable;
import java.util.Observer;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ICommandHandler;


/**
 * 
 * The CommandLineParserView is responsible for displaying system output to the user and fetching
 * input of the user. Its an observer of the {@link ICommandHandler}s.
 * 
 * @author Alexander Hetzer
 *
 */
public class CommandLineParserView extends Observable implements Observer {

   private static final String OUTPUT_TAG = "[JPL]";

   private static final String ERROR_UNSUPPORTED_UPDATE = "Unsupported update on CommandLineParserView with observable %S and object %S";

   private static CommandLineParserView commandLineParserView;


   /**
    * Private constructor, which is only used to create the singleton instance once.
    */
   private CommandLineParserView() {
   }


   /**
    * Returns the singleton instance of {@link CommandLineParserView}.
    * 
    * @return the singleton instance of {@link CommandLineParserView}
    */
   public static CommandLineParserView getCommandLineParserView() {
      if (commandLineParserView == null) {
         commandLineParserView = new CommandLineParserView();
         commandLineParserView.init();
      }

      return commandLineParserView;
   }


   /**
    * Initializes the {@link CommandLineParserView}.
    */
   private void init() {
      addObserver(InputControl.getInputControl());
   }


   @Override
   public void update(Observable o, Object arg) {
      if (isValidUpdateCall(o, arg)) {
         displayMessage((String) arg);
      } else {
         throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_UPDATE, o, arg));
      }
   }


   /**
    * Handles the given user input.
    * 
    * @param userInput the user input to be handled
    */
   public void handleUserInput(String[] userInput) {
      setChanged();
      notifyObservers(userInput);
   }


   /**
    * Checks whether the given observable and the given argument are allowed as parameters for an
    * update call.
    * 
    * @param observable the observable, the update was called by
    * @param argument the argument of the update
    * 
    * @return {@code true}, if the both the observable and the argument are of a valid type for the
    *         update call, otherwise {@code false}
    */
   private boolean isValidUpdateCall(Observable observable, Object argument) {
      return argument instanceof String && (isCommandHandler(observable) || isSystemStatus(observable));
   }


   /**
    * Checks whether the given observable is an instance of {@link ICommandHandler}.
    * 
    * @param observable the observable to check
    * 
    * @return {@code true} if observable instance of {@link ICommandHandler}
    */
   private boolean isCommandHandler(Observable observable) {
      return observable instanceof ICommandHandler;
   }


   /**
    * Checks whether the given observable is an instance of {@link SystemStatus}.
    * 
    * @param observable the observable to check
    * 
    * @return {@code true} if observable instance of {@link SystemStatus}
    */
   private boolean isSystemStatus(Observable observable) {
      return observable instanceof SystemStatus;
   }


   /**
    * Displays the given message to the user.
    * 
    * @param message the message to be displayed
    */
   public void displayMessage(final String message) {
      String whiteSpaceStringForIndentation = StringUtils.LINE_BREAK
            + StringUtils.getWhitespaceStringOfLength(getIndentationLengthForConsecutiveLines());
      String formattedMessage = getOutputPrefixTag() + message.replaceAll(StringUtils.LINE_BREAK, whiteSpaceStringForIndentation);
      System.out.println(formattedMessage);
   }


   /**
    * Returns the output prefix tag.
    * 
    * @return the output prefix tag
    */
   private String getOutputPrefixTag() {
      return OUTPUT_TAG + StringUtils.SINGLE_WHITESPACE;
   }


   /**
    * Returns the length of the indentation for consecutive lines, after the first line of a
    * message.
    * 
    * @return the length of the indentation for consecutive lines
    */
   private int getIndentationLengthForConsecutiveLines() {
      return getOutputPrefixTag().length();
   }

}
