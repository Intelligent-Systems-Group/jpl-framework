package de.upb.cs.is.jpl.api.dataset;


import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The DatasetFile encapsulates the concrete file location of the selected dataset and the selection
 * of the available features.
 * 
 * @author Sebastian Osterbrink
 * @author Andreas Kornelsen
 *
 */
public class DatasetFile {

   private File file;
   private String comment = StringUtils.EMPTY_STRING;
   private List<Integer> itemFeatures;
   private List<Integer> contextFeatures;

   private static final AtomicLong NEXT_ID = new AtomicLong(0);
   final long id = NEXT_ID.getAndIncrement();


   /**
    * Creates a new DatasetFile, with the file to be parsed.
    * 
    * @param file the added dataset file
    */
   public DatasetFile(File file) {
      this.file = file;
   }


   /**
    * Creates a new DatasetFile, with the file to be parsed and list of context features and item
    * features.
    * 
    * @param file the added dataset file
    * @param contextFeatures the list of selected context features
    * @param itemFeatures the list of selected item features
    */
   public DatasetFile(File file, List<Integer> contextFeatures, List<Integer> itemFeatures) {
      this(file);
      this.itemFeatures = itemFeatures;
      this.contextFeatures = contextFeatures;
   }


   @Override
   public String toString() {
      return file.toString();
   }


   /**
    * Checks whether a set of item features was selected.
    * 
    * @return {@code true} if the set of item features is not null an is not empty
    */
   public boolean isItemFeatureSelected() {
      return itemFeatures != null && !itemFeatures.isEmpty();
   }


   /**
    * Checks whether a set of context features was selected.
    * 
    * @return {@code true} if the set of context features is not null an is not empty
    */
   public boolean isContextFeatureSelected() {
      return contextFeatures != null && !contextFeatures.isEmpty();
   }


   /*
    * Only generic getter and setter methods below.
    */

   /**
    * Returns the comment for this file.
    * 
    * @return the comment defined for this file
    */
   public String getComment() {
      return comment;
   }


   /**
    * Sets a comment for this file.
    * 
    * @param comment the comment which is to be set for this file
    */
   public void setComment(String comment) {
      this.comment = comment;
   }


   /**
    * Returns a list of item features selected for this dataset.
    * 
    * @return a list of item features. The default this method returns if no item features were
    *         selected is null.
    */
   public List<Integer> getItemFeatures() {
      return itemFeatures;
   }


   /**
    * Sets the list of selected item features.
    * 
    * @param itemFeatures the {@link List} of item features
    */
   public void setItemFeatures(List<Integer> itemFeatures) {
      this.itemFeatures = itemFeatures;
   }


   /**
    * Returns a list of context features selected for this dataset.
    * 
    * @return a {@link List} of context features. The default this method returns if no context
    *         features were selected is null.
    */
   public List<Integer> getContextFeatures() {
      return contextFeatures;
   }


   /**
    * Sets the {@link List} of selected context features.
    * 
    * @param contextFeatures The {@link List} of context features
    */
   public void setContextFeatures(List<Integer> contextFeatures) {
      this.contextFeatures = contextFeatures;
   }


   /**
    * Returns the file for which the dataset is parsed and stored.
    * 
    * @return the {@link File} object which was selected
    */
   public File getFile() {
      return file;
   }


   /**
    * Set the file for which the dataset is to be parsed and stored.
    * 
    * @param file the {@link File} containing the dataset
    */
   public void setFile(File file) {
      this.file = file;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;

      DatasetFile other = (DatasetFile) obj;
      String absolutePath = other.getFile().getAbsolutePath();
      return this.file.getAbsolutePath().equals(absolutePath);
   }


   @Override
   public int hashCode() {
      return file.getAbsolutePath().hashCode();
   }

}
