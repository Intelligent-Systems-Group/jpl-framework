package de.upb.cs.is.jpl.api.dataset;


import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * An interface for a parser offering methods for parsing a dataset file.
 * 
 * @author Sebastian Osterbrink
 * @author Tanja Tornede
 *
 */
public interface IDatasetParser {


   /**
    * Parses the given dataset file and returns the resulting dataset.
    * 
    * @param file the file to parse
    * @return the resulting dataset
    * @throws ParsingFailedException if the parser encounters a problem while parsing the file
    */
   public IDataset<?, ?, ?> parse(DatasetFile file) throws ParsingFailedException;


   /**
    * Parses a partial part of a dataset file and returns the dataset that it is created by reading
    * all item and context informations and the first <i>n</i> lines of the ranking information.
    * 
    * @param file the dataset file to parse.
    * @param amountOfInstances the number of lines which will be read from the ratings.data file
    * @return the resulting dataset
    * @throws ParsingFailedException if the parser encounters a problem while parsing the file
    */
   public IDataset<?, ?, ?> parsePartialOf(DatasetFile file, int amountOfInstances) throws ParsingFailedException;


   /**
    * Returns the dataset which was parsed before.
    * 
    * @return the dataset which was created in the parse operation
    */
   public IDataset<?, ?, ?> getDataset();
}
