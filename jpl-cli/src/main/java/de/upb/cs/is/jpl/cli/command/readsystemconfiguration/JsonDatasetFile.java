package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfigurationKeyValue;


/**
 * 
 * This class is required for parsing of the json array 'input_files' from SystemConfiguration.json
 * file. Each item from the json array is converted to a {@code JsonDatasetFile}.
 * 
 * 
 * @author Andreas Kornelsen
 */
public class JsonDatasetFile {


   @SerializedName(SystemConfigurationKeyValue.DESCRIPTION)
   private String description;

   @SerializedName(SystemConfigurationKeyValue.FILE_PATH)
   private String filePath;

   @SerializedName(SystemConfigurationKeyValue.SELECTED_CONTEXT_FEATURES)
   private List<Integer> selectedContextFeatures;

   @SerializedName(SystemConfigurationKeyValue.SELECTED_ITEM_FEATURES)
   private List<Integer> selectedItemFeatures;


   /**
    * Returns the selected context features of the {@link IDataset}.
    *
    * @return the selected context features of the dataset
    */
   public List<Integer> getSelectedContextFeatures() {
      return selectedContextFeatures;
   }


   /**
    * Sets the selected context features of the {@link IDataset}.
    *
    * @param selectedContextFeatures the new selected context features of the dataset
    */
   public void setSelectedContextFeatures(List<Integer> selectedContextFeatures) {
      this.selectedContextFeatures = selectedContextFeatures;
   }


   /**
    * Returns the selected item feature of the {@link IDataset}.
    *
    * @return the selected item feature of the dataset
    */
   public List<Integer> getSelectedItemFeature() {
      return selectedItemFeatures;
   }


   /**
    * Sets the selected item feature of the {@link IDataset}.
    *
    * @param selectedItemFeatures the new selected item feature of the dataset
    */
   public void setSelectedItemFeature(List<Integer> selectedItemFeatures) {
      this.selectedItemFeatures = selectedItemFeatures;
   }


   /**
    * Returns the description of the {@link IDataset}.
    *
    * @return the description of the dataset
    */
   public String getDescription() {
      return description;
   }


   /**
    * Sets the description of the {@link IDataset}.
    *
    * @param description the new description of the dataset
    */
   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Returns the file path of the {@link IDataset}.
    *
    * @return the file path of the dataset
    */
   public String getFilePath() {
      return filePath;
   }


   /**
    * Sets the file path of the {@link IDataset}.
    *
    * @param filePath the new file path of the dataset
    */
   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }
}
