package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * A parser for dataset file with relative rankings.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultRelativeDatasetParser extends DefaultDatasetParser<Ranking> {

   private static final String CANNOT_PARSE_AN_ABSOLUTE_DATASET = "Cannot parse an absolute dataset.";


   /**
    * Creates a new {@link DefaultRelativeDatasetParser}.
    */
   public DefaultRelativeDatasetParser() {
      parseResult = new DefaultRelativeDataset();
   }


   @Override
   public void initParse() throws ParsingFailedException {
      parseResult = new DefaultRelativeDataset();

      if (!isRelative)
         throw new ParsingFailedException(CANNOT_PARSE_AN_ABSOLUTE_DATASET);
      sendFeatureDeclarationsToDataset();
   }


   @Override
   public void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      try {
         String[] contextRatingSplit = ratingInformationLine.split(ID_DIVIDER);
         int contextId = Integer.parseInt(contextRatingSplit[0].substring(contextRatingSplit[0].indexOf(CONTEXT_MARKER) + 1));
         parseResult.addInstance(contextId, parseRelativeRanking(contextRatingSplit[1]));
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_LINE_PARSING_FEATURES, ratingInformationLine), e);
      }
   }
}
