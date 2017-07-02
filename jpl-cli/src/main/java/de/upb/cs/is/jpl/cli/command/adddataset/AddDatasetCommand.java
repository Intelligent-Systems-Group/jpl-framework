package de.upb.cs.is.jpl.cli.command.adddataset;


import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * The {@link AddDatasetCommand} is responsible for adding <b>valid</b> paths to different datasets
 * to the {@link SystemConfiguration}.
 * 
 * @author Sebastian Osterbrink
 */
public class AddDatasetCommand extends ACommand {

   private static final String COMMAND_CAN_BE_EXECUTED = "The command can be executed.";
   private static final String NO_DATASET_PATH_IS_DEFINED = "No dataset path is defined. The given path is %s";
   private static final String FILE_DOES_NOT_EXIST = "The file %s does not exist.";
   private static final String PATH_IS_A_DIRECTORY = "The path %s is a directory.";
   private static final String FILE_ALREADY_EXISTS = "File %s already exists in the system configutation.";
   private static final String FILE_SUCCESSFULLY_ADDED = "File %s was successfully added to the system configutation.";

   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(AddDatasetCommand.class);

   private DatasetFile addedFile;
   private String failure;
   private String datasetPath;
   private List<Integer> itemFeatures;
   private List<Integer> contextFeatures;


   /**
    * Creates a new {@link AddDatasetCommand} which will add a dataset to the
    * {@link SystemConfiguration}.
    * 
    * @param datasetPath the path to the selected dataset file
    * @param contextFeatures a {@link List}&lt;{@link Integer}&gt; of context feature selections;
    * @param itemFeatures a {@link List}&lt;{@link Integer}&gt; of item feature selections
    */
   public AddDatasetCommand(String datasetPath, List<Integer> contextFeatures, List<Integer> itemFeatures) {
      super(ECommand.ADD_DATASET.getCommandIdentifier());
      this.datasetPath = datasetPath;

      if (contextFeatures != null && !contextFeatures.isEmpty())
         this.contextFeatures = CollectionsUtils.getDeepCopyOf(contextFeatures);
      if (itemFeatures != null && !itemFeatures.isEmpty())
         this.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatures);
      this.failure = null;
      this.addedFile = null;
   }


   /**
    * {@inheritDoc} <br>
    * Execution is possible if the path for a dataset is defined and points to a existing file. It
    * only validates that the files <b>exist</b>, but <b>doesn't</b> validate that the files are
    * valid datasets.
    */
   @Override
   public boolean canBeExecuted() {
      if (datasetPath == null || datasetPath.isEmpty()) {
         failure = String.format(NO_DATASET_PATH_IS_DEFINED, datasetPath);
         return false;
      } else {
         File file = new File(datasetPath);
         if (!file.exists() || !file.isFile()) {
            if (!file.exists()) {
               failure = String.format(FILE_DOES_NOT_EXIST, datasetPath);
            } else {
               failure = String.format(PATH_IS_A_DIRECTORY, datasetPath);
            }
            return false;
         }
      }
      return true;

   }


   /**
    * {@inheritDoc} <br>
    * Adds the information provided to the command to the global {@link SystemConfiguration} object.
    * <br>
    * <br>
    */
   @Override
   public CommandResult executeCommand() {
      String additionalInfo;
      File file = new File(datasetPath);

      DatasetFile datasetFile = new DatasetFile(file, contextFeatures, itemFeatures);
      if (SystemConfiguration.getSystemConfiguration().addDatasetFile(datasetFile)) {
         additionalInfo = String.format(FILE_SUCCESSFULLY_ADDED, datasetPath);
         addedFile = datasetFile;
      } else {
         additionalInfo = String.format(FILE_ALREADY_EXISTS, datasetPath);
      }
      return CommandResult.createSuccessCommandResultWithAdditionalInformation(datasetFile, additionalInfo);
   }


   @Override
   public String getFailureReason() {
      return canBeExecuted() ? COMMAND_CAN_BE_EXECUTED : failure;
   }


   @Override
   public void undo() {
      if (addedFile != null) {
         SystemConfiguration.getSystemConfiguration().removeDatasetFile(addedFile);
      }
   }

}
