package de.upb.cs.is.jpl.cli.command.adddataset;


import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * The {@link AddDatasetCommandHandler} creates a {@link AddDatasetCommand} and holds the
 * {@link AddDatasetCommandConfiguration} before it is passed to the Command.
 * 
 * @author Sebastian Osterbrink
 */
public class AddDatasetCommandHandler extends ACommandHandler {

   private static final String EXECUTED_SUCCESSFULLY = "Command executed successfully.";
   private static final String EXECUTION_FAILED = "Command execution failed.";

   private AddDatasetCommand command;
   private AddDatasetCommandConfiguration commandConfiguration;


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfig) {
      this.commandConfiguration = (AddDatasetCommandConfiguration) commandConfig;
      command = new AddDatasetCommand(commandConfiguration.getDatasetPath(), commandConfiguration.getContextFeatures(),
            commandConfiguration.getItemFeatures());
      return command;
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(commandResult.isExecutedSuccessfully() ? EXECUTED_SUCCESSFULLY : EXECUTION_FAILED);
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(commandResult.getAdditionalInformation());
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(commandResult.isExecutedSuccessfully() ? StringUtils.EMPTY_STRING : command.getFailureReason());
      return stringBuilder.toString();
   }


   @Override
   public void init() {
      command = null;
      commandConfiguration = null;
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new AddDatasetCommandConfiguration();
   }

}
