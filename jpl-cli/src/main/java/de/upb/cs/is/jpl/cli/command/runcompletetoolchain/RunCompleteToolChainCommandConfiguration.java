package de.upb.cs.is.jpl.cli.command.runcompletetoolchain;


import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link RunCompleteToolChainCommand}. The Parameters are defined for easy handling by JCommander.
 * 
 * @author Alexander Hetzer
 *
 */
@Parameters(separators = "=", commandDescription = "Runs the complete toolchain based on a given system configuration file.")
public class RunCompleteToolChainCommandConfiguration extends ACommandConfiguration {

   @Parameter(names = { "--config", "-c" }, description = "Path to the json system configuration file.", required = true)
   private String configurationFilePath;


   @Override
   public void resetFields() {
      configurationFilePath = StringUtils.EMPTY_STRING;
   }


   @Override
   public ICommandConfiguration getCopy() {
      RunCompleteToolChainCommandConfiguration copyConfiguration = new RunCompleteToolChainCommandConfiguration();
      copyConfiguration.configurationFilePath = this.configurationFilePath;
      return copyConfiguration;
   }


   /**
    * Returns the configuration file path.
    * 
    * @return the configuration file path
    */
   public String getConfigurationFilePath() {
      return configurationFilePath;
   }
}
