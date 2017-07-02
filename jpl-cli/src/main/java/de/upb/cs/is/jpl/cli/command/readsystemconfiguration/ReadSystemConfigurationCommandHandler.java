package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * The ReadSystemConfigurationCommandHandler is responsible for maintaining the
 * {@link ReadSystemConfigurationCommand}.
 *
 * @author Andreas Kornelsen
 */
public class ReadSystemConfigurationCommandHandler extends ACommandHandler {

   private static final Logger logger = LoggerFactory.getLogger(ReadSystemConfigurationCommandHandler.class);

   private static final String WARN_MESSAGE = "The system configuration file can't be accessed or doesn't exist, filePath %s.";
   private static final String COMMAND_FAILED_MESSAGE = "The %s has failed.";
   private static final String COMMAND_SUCCESS_MESSAGE = "The %s was executed successfully. Detailed Information can be found in the log file. The following parameters are set:";

   private String absolutFilePathToSystemConfiguration = "";


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      ReadSystemConfigurationCommandConfiguration readSystemConfigurationCommandConfiguration = (ReadSystemConfigurationCommandConfiguration) commandConfiguration;
      absolutFilePathToSystemConfiguration = readSystemConfigurationCommandConfiguration.getFilePath();

      File file = new File(absolutFilePathToSystemConfiguration);
      if (!file.isFile()) {
         logger.warn(String.format(WARN_MESSAGE, absolutFilePathToSystemConfiguration));
      }

      return new ReadSystemConfigurationCommand(absolutFilePathToSystemConfiguration);
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      boolean executedSuccessfully = commandResult.isExecutedSuccessfully();
      String additionalInformation = commandResult.getAdditionalInformation();


      String messageCreatedFromCmdResult = String.format(COMMAND_FAILED_MESSAGE, command.toString());
      if (executedSuccessfully) {
         messageCreatedFromCmdResult = String.format(COMMAND_SUCCESS_MESSAGE, command.toString()) + StringUtils.LINE_BREAK
               + additionalInformation;
      }
      return messageCreatedFromCmdResult;
   }


   @Override
   public void init() {
      // There is no init procedure required.
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new ReadSystemConfigurationCommandConfiguration();
   }


}
