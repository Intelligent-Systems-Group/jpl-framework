package de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * A parser for dataset files with absolute ratings.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultAbsoluteDatasetParser extends DefaultDatasetParser<IVector> {

   private static final String CANNOT_PARSE_RELATIVE_DATASETS = "Cannot parse relative datasets.";


   /**
    * Creates a new {@link DefaultAbsoluteDatasetParser}.
    */
   public DefaultAbsoluteDatasetParser() {
      parseResult = new DefaultAbsoluteDataset();
   }


   @Override
   public void initParse() throws ParsingFailedException {
      parseResult = new DefaultAbsoluteDataset();
      parseResult.setRatingAllowedValues(allowedRatingValues);
      if (isRelative)
         throw new ParsingFailedException(CANNOT_PARSE_RELATIVE_DATASETS);
      sendFeatureDeclarationsToDataset();
   }


   @Override
   public void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      try {
         String[] contextRatingSplit = ratingInformationLine.split(ID_DIVIDER);
         int contextId = Integer.parseInt(contextRatingSplit[0].substring(contextRatingSplit[0].indexOf(CONTEXT_MARKER) + 1));
         parseResult.addInstance(contextId, parseAbsoluteRatingVector(contextRatingSplit[1]));
      } catch (IndexOutOfBoundsException e) {
         throw new ParsingFailedException(e.getMessage(), e);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(ERROR_IN_LINE_PARSING_FEATURES, e);
      }
   }
}
