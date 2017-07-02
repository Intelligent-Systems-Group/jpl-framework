package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * 
 * The ReadSystemConfigurationCommandConfiguration is responsible for storing the parameters of the
 * {@link ReadSystemConfigurationCommand}, given by the user calling the command.
 * 
 * @author Andreas Kornelsen
 *
 */
@Parameters(separators = "=", commandDescription = "Initializes the system configuration with the values in the given system configuration file (Json).")
public class ReadSystemConfigurationCommandConfiguration extends ACommandConfiguration {

   private static final String FILE_PATH_DESCRIPTION = "Specifies the absolute path to the system configuration (Json) file.";

   private static final String PARAMETER_KEY_FILEPATH_LONG = "--file_path";
   private static final String PARAMETER_KEY_FILEPATH_SHORT = "-p";

   @Parameter(names = { PARAMETER_KEY_FILEPATH_LONG, PARAMETER_KEY_FILEPATH_SHORT }, description = FILE_PATH_DESCRIPTION, required = true)
   private String filePath = StringUtils.EMPTY_STRING;


   @Override
   public void resetFields() {
      filePath = StringUtils.EMPTY_STRING;
   }


   @Override
   public ICommandConfiguration getCopy() {
      ReadSystemConfigurationCommandConfiguration copy = new ReadSystemConfigurationCommandConfiguration();
      copy.setFilePath(this.filePath);
      return copy;
   }


   /**
    * Sets the absolute file path of the SystemConfiguraiton.json file.
    *
    * @param filePath the file path of the SystemConfiguration.json file
    */
   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }


   /**
    * Returns the absolute file path of the SystemConfiguraiton.json file.
    *
    * @return filePath the file path of the SystemConfiguration.json file
    */
   public String getFilePath() {
      return filePath;
   }

}
