package de.upb.cs.is.jpl.api.dataset.defaultdataset;


import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * A parser for dataset file. The only thing missing in this class is the parsing of the ratings.
 * This is done in the {@link DefaultRelativeDatasetParser} and {@link DefaultAbsoluteDatasetParser}
 * classes.
 * 
 * @author Sebastian Osterbrink
 * @param <T> the type of the resulting {@link DefaultDataset}
 */
public abstract class DefaultDatasetParser<T> extends ADatasetParser {

   protected static final String PROBLEM_BY_PARSING_LINE = "Problem while parsing line \"%s\".";
   protected static final String ERROR_IN_LINE_PARSING_FEATURES = PROBLEM_BY_PARSING_LINE + " The values must only be double values.";

   protected DefaultDataset<T> parseResult;


   @Override
   public void finishParse() {
      // nothing to do
   }


   /**
    * Enters the currently set values for context and item feature declarations into the dataset.
    * This allows it the dataset to know the dimension of the assigned feature vectors.
    */
   protected void sendFeatureDeclarationsToDataset() {
      for (int featureNumber = 0; featureNumber < contextFeatures.size(); featureNumber++) {
         parseResult.setContextFeature(featureNumber, contextFeatures.get(featureNumber));
         parseResult.setContextFeatureAllowedValues(featureNumber, allowedContextFeatureValues.get(featureNumber));
      }
      for (int featureNumber = 0; featureNumber < itemFeatures.size(); featureNumber++) {
         parseResult.setItemFeature(featureNumber, itemFeatures.get(featureNumber));
         parseResult.setItemFeatureAllowedValues(featureNumber, allowedItemFeatureValues.get(featureNumber));
      }
   }


   @Override
   public DefaultDataset<T> getDataset() {
      return parseResult;
   }


   @Override
   public void parseItemVectorLine(String line) throws ParsingFailedException {
      try {
         String[] itemLine = line.split(ID_DIVIDER);
         Integer id = Integer.parseInt(itemLine[0].substring(itemLine[0].indexOf(ITEM_MARKER) + 1));
         IVector result = itemLine.length > 1 ? parseInformationVector(itemLine[1].trim().split(VECTOR_DIVIDER), itemFeatures.size())
               : new SparseDoubleVector(itemFeatures.size());
         parseResult.setItemVector(id, result.asArray());
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_LINE_PARSING_FEATURES, line), e);
      } catch (InvalidInstanceException e) {
         throw new ParsingFailedException(e.getMessage(), e);
      }


   }


   @Override
   public void parseContextVectorLine(String line) throws ParsingFailedException {
      String[] contextLine = line.split(ID_DIVIDER);
      String idString = contextLine[0].substring(contextLine[0].indexOf(CONTEXT_MARKER) + 1);
      try {
         Integer id = Integer.parseInt(idString);
         IVector result = contextLine.length > 1
               ? parseInformationVector(contextLine[1].trim().split(VECTOR_DIVIDER), contextFeatures.size())
               : new SparseDoubleVector(contextFeatures.size());
         parseResult.setContextVector(id, result.asArray());
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_LINE_PARSING_FEATURES, line), e);
      } catch (InvalidInstanceException e) {
         throw new ParsingFailedException(e.getMessage(), e);
      }
   }


}
