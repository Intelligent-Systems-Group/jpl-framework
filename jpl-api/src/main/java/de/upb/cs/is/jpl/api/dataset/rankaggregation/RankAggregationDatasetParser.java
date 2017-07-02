package de.upb.cs.is.jpl.api.dataset.rankaggregation;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * This class parses an GPRF dataset of the correct form for the rank aggregation problem and
 * transforms it into the {@link RankAggregationDataset}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class RankAggregationDatasetParser extends ADatasetParser {

   private static final String ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_COUNT_RANKINGS = "The size of the rankings is not equal to the provided size of rating numbers; rankingsSize: %d; countRankingsSize: %d";
   private static final String ERROR_NO_ID_DIVIDER_IN_STRING = "There is no id divider found in the line; line: %s.";
   private static final String ERROR_LINE_CONTAINS_NO_CONTENT = "Line contains no content, line: %s.";
   private static final String ERROR_INTEGER_PARSE = "Line couldn't be parsed to Integer, lineContent: %s.";
   private List<Integer> labels;
   private List<Integer> countRankings;
   private List<Ranking> rankings;

   private RankAggregationDataset rankAggregationDataset;


   @Override
   public IDataset<?, ?, ?> getDataset() {
      if (rankAggregationDataset == null) {
         return new RankAggregationDataset();
      }
      return rankAggregationDataset;
   }


   @Override
   protected void initParse() throws ParsingFailedException {
      this.labels = new ArrayList<>();
      this.countRankings = new ArrayList<>();
      this.rankings = new ArrayList<>();
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      int countRankingsSize = countRankings.size();
      int rankingsSize = rankings.size();

      if (countRankingsSize != rankingsSize) {
         throw new ParsingFailedException(
               String.format(ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_COUNT_RANKINGS, rankingsSize, countRankingsSize));
      }

      rankAggregationDataset = new RankAggregationDataset(labels, countRankings, rankings);
   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      int label = getFirstFeatureValueOfLine(itemInformationLine);
      labels.add(label);
   }


   @Override
   protected void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      int countRating = getFirstFeatureValueOfLine(contextInformationLine);
      countRankings.add(countRating);
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      String lineContent = getLineContent(ratingInformationLine);
      Ranking ranking = parseRelativeRanking(lineContent);
      rankings.add(ranking);
   }


   /**
    * Returns the line content without the line index.
    *
    * @param indexedLine a line in the dataset with an index in the beginning
    * @return the line without the index in the beginning
    * @throws ParsingFailedException if the line doesn't contain an id divider
    */
   private String getLineContent(String indexedLine) throws ParsingFailedException {
      int indexOfIdDivider = indexedLine.indexOf(ID_DIVIDER);
      if (indexOfIdDivider == -1) {
         throw new ParsingFailedException(String.format(ERROR_NO_ID_DIVIDER_IN_STRING, indexedLine));
      }

      return indexedLine.substring(indexOfIdDivider + 1).trim();
   }


   /**
    * Returns the first feature value of the line without the index.
    *
    * @param datasetIndexedLineWithOneFeature a line in the dataset with an index in front and one
    *           feature value
    * @return the first feature value of the line
    * @throws ParsingFailedException if the line doesn't contain an id divider
    */
   private int getFirstFeatureValueOfLine(String datasetIndexedLineWithOneFeature) throws ParsingFailedException {
      String lineContent = getLineContent(datasetIndexedLineWithOneFeature);

      if (lineContent.isEmpty()) {
         throw new ParsingFailedException(String.format(ERROR_LINE_CONTAINS_NO_CONTENT, datasetIndexedLineWithOneFeature));
      }
      int firstFeatureValueOfLien = -1;
      try {
         firstFeatureValueOfLien = Integer.parseInt(lineContent);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_INTEGER_PARSE, lineContent));
      }

      if (firstFeatureValueOfLien == -1) {
         throw new ParsingFailedException(String.format(ERROR_INTEGER_PARSE, lineContent));
      }

      return firstFeatureValueOfLien;
   }
}
