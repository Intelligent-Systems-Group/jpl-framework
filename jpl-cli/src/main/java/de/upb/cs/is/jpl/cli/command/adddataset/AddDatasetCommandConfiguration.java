package de.upb.cs.is.jpl.cli.command.adddataset;


import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * The {@link AddDatasetCommandConfiguration} encapsulates all necessary and/or possible combination
 * of parameters for the {@link AddDatasetCommand}. The parameters are defined for easy handling by
 * {@link JCommander}.
 * 
 * @author Sebastian Osterbrink
 */
@Parameters(separators = "=", commandDescription = "Adds one dataset to the set of datasets, on which the algorithms wil be trained on.")
public class AddDatasetCommandConfiguration extends ACommandConfiguration {

   @Parameter(names = { "--dataset", "-d" }, description = "Path to the GPRF dataset file", required = true)
   private String datasetPath;

   @Parameter(names = { "--item_features",
         "-i" }, variableArity = true, description = "Integer List of item feature numbers to be used in the learning algorithms. "
               + "This feature is only active, if at least one feature number has been selected.")
   private List<Integer> itemFeatures;

   @Parameter(names = { "--context_features",
         "-c" }, variableArity = true, description = "Integer List of context feature numbers to be used in the learning algorithms. "
               + "This feature is only active, if at least one feature number has been selected.")
   private List<Integer> contextFeatures;


   /**
    * Creates an {@link AddDatasetCommandConfiguration} initialized with default values.
    */
   public AddDatasetCommandConfiguration() {
      resetFields();
   }


   @Override
   public void resetFields() {
      datasetPath = StringUtils.EMPTY_STRING;
      itemFeatures = null;
      contextFeatures = null;
   }


   @Override
   public ICommandConfiguration getCopy() {
      AddDatasetCommandConfiguration copy = new AddDatasetCommandConfiguration();
      copy.setDatasetPath(datasetPath);
      if (contextFeatures != null && !contextFeatures.isEmpty())
         copy.setContextFeatures(CollectionsUtils.getDeepCopyOf(contextFeatures));
      if (itemFeatures != null && !itemFeatures.isEmpty())
         copy.setItemFeatures(CollectionsUtils.getDeepCopyOf(itemFeatures));
      return copy;
   }


   /*
    * only generic getters and setters below
    */

   /**
    * Returns the path to the dataset which was set earlier. If no path has been set yet, it returns
    * an empty {@link String}.
    * 
    * @return the path to the dataset
    */
   public String getDatasetPath() {
      return datasetPath;
   }


   /**
    * Sets the path to the dataset file.
    * 
    * @param datasetPath the path to the selected dataset
    */
   public void setDatasetPath(String datasetPath) {
      this.datasetPath = datasetPath;
   }


   /**
    * Returns the {@link List} of selected item features if one was which was set earlier. If no
    * item features have been selected yet, it returns {@code null}
    * 
    * @return the {@link List} of item features .
    */
   public List<Integer> getItemFeatures() {
      return itemFeatures;
   }


   /**
    * Sets a {@link List} of item features which are selected out of all possible features.
    * 
    * @param itemFeatures the {@link List} of selected item features
    */
   public void setItemFeatures(List<Integer> itemFeatures) {
      this.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatures);
   }


   /**
    * Get the {@link List} of selected context features which was set earlier. If no context
    * features have been selected yet, it returns {@code null}.
    * 
    * @return the {@link List} of selected context features
    */
   public List<Integer> getContextFeatures() {
      return contextFeatures;
   }


   /**
    * Sets a {@link List} of selected features which are selected out of all possible features.
    * 
    * @param contextFeatures the {@link List} of selected context features
    */
   public void setContextFeatures(List<Integer> contextFeatures) {
      this.contextFeatures = CollectionsUtils.getDeepCopyOf(contextFeatures);
   }

}
