package de.upb.cs.is.jpl.api.dataset.objectranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This parser is used to parse a dataset for object ranking.
 * 
 * @author Pritha Gupta
 */
public class ObjectRankingDatasetParser extends ADatasetParser {
   protected static final String PROBLEM_BY_PARSING_LINE = "Problem while parsing line \"%s\".";
   protected static final String ERROR_IN_LINE_PARSING_FEATURES = PROBLEM_BY_PARSING_LINE + " The values must only be double values.";
   protected static final String ERROR_IN_LINE_PARSING_RANKING = PROBLEM_BY_PARSING_LINE
         + " The ranking must have items which are present in the dataset.";

   private static final String ERROR_CONTEXT_WITHOUT_FEATURES = "Encountered a context without features; line: %s.";
   private static final String ERROR_ITEM_WITHOUT_FEATURES = "Encountered an item without features; line: %s.";
   private static final String ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_NUMBER_OF_CONTEXTS = "The size of the rankings is not equal to the provided size of contexts; rankingsSize: %d; countRankingsSize: %d";

   private List<Ranking> rankings;
   private List<double[]> itemVectors;
   private List<double[]> contextVectors;
   private ObjectRankingDataset objectRankingDataset;


   @Override
   public IDataset<?, ?, ?> getDataset() {
      if (objectRankingDataset == null) {
         return new ObjectRankingDataset();
      }
      return objectRankingDataset;
   }


   @Override
   protected void initParse() {
      this.rankings = new ArrayList<>();
      this.itemVectors = new ArrayList<>();
      this.contextVectors = new ArrayList<>();
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      int rankingsSize = rankings.size();
      int numberOfContexts = contextVectors.size();

      if (numberOfContexts != rankingsSize) {
         throw new ParsingFailedException(
               String.format(ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_NUMBER_OF_CONTEXTS, rankingsSize, numberOfContexts));
      }
      objectRankingDataset = new ObjectRankingDataset(contextVectors, itemVectors, rankings);

   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      String[] contextLine = itemInformationLine.split(ID_DIVIDER);
      if (contextLine.length <= 1) {
         throw new ParsingFailedException(String.format(ERROR_ITEM_WITHOUT_FEATURES, itemInformationLine));
      }
      String featureDeclaration = contextLine[1].trim();
      IVector parsedItemVector = parseInformationVector(featureDeclaration.split(VECTOR_DIVIDER), getNumberOfItemFeatures());
      itemVectors.add(parsedItemVector.asArray());
   }


   @Override
   protected void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      String[] contextLine = contextInformationLine.split(ID_DIVIDER);
      if (contextLine.length <= 1) {
         throw new ParsingFailedException(String.format(ERROR_CONTEXT_WITHOUT_FEATURES, contextInformationLine));
      }
      String featureDeclaration = contextLine[1].trim();
      IVector parsedContextVector = parseInformationVector(featureDeclaration.split(VECTOR_DIVIDER), getNumberOfContextFeatures());
      contextVectors.add(parsedContextVector.asArray());
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      try {
         String[] contextRatingSplit = ratingInformationLine.split(ID_DIVIDER);
         Ranking ranking = parseRelativeRanking(contextRatingSplit[1]);
         for (int obj : ranking.getObjectList()) {
            if (obj > itemVectors.size()) {
               throw new ParsingFailedException(String.format(ERROR_IN_LINE_PARSING_RANKING, ratingInformationLine));
            }
         }
         rankings.add(ranking);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_LINE_PARSING_FEATURES, ratingInformationLine), e);
      }
   }


   /**
    * Returns the number of context features in the parsed dataset.
    * 
    * @return the number of context features in the parsed dataset
    */
   private int getNumberOfContextFeatures() {
      return contextFeatures.size();
   }


   /**
    * Returns the number of item features in the parsed dataset.
    * 
    * @return the number of item features in the parsed dataset
    */
   private int getNumberOfItemFeatures() {
      return itemFeatures.size();
   }


}
