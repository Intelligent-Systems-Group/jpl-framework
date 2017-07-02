package de.upb.cs.is.jpl.api.dataset.collaborativefiltering;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringParser extends ADatasetParser {

   @SuppressWarnings("unused")
   private static final Logger parser_logger = LoggerFactory.getLogger(CollaborativeFilteringParser.class);

   protected CollaborativeFilteringDataset parseResult;


   @Override
   public CollaborativeFilteringDataset getDataset() {
      makeMatrixNotNullAndCorrectSize();
      return parseResult;
   }


   @Override
   public void parseItemVectorLine(String line) throws ParsingFailedException {
      String[] itemLine = line.split(ID_DIVIDER);
      Integer id = Integer.parseInt(itemLine[0].substring(itemLine[0].indexOf(ITEM_MARKER) + 1));
      IVector result = itemLine.length > 1 ? parseInformationVector(itemLine[1].trim().split(VECTOR_DIVIDER), itemFeatures.size())
            : new SparseDoubleVector(itemFeatures.size());
      parseResult.setItemVector(id, result);
   }


   @Override
   public void parseContextVectorLine(String line) throws ParsingFailedException {
      String[] contextLine = line.split(ID_DIVIDER);
      Integer id = Integer.parseInt(contextLine[0].substring(contextLine[0].indexOf(CONTEXT_MARKER) + 1));
      IVector result = contextLine.length > 1 ? parseInformationVector(contextLine[1].trim().split(VECTOR_DIVIDER), contextFeatures.size())
            : new SparseDoubleVector(contextFeatures.size());
      parseResult.setContextVector(id, result);
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      makeMatrixNotNullAndCorrectSize();
      String[] contextRatingSplit = ratingInformationLine.split(ID_DIVIDER);
      int contextId = Integer.parseInt(contextRatingSplit[0].substring(contextRatingSplit[0].indexOf(CONTEXT_MARKER) + 1).trim());

      IVector vector = parseAbsoluteRatingVector(contextRatingSplit.length > 1 ? contextRatingSplit[1] : StringUtils.EMPTY_STRING);
      for (int i = 0; i < vector.length(); i++) {
         if (Double.compare(vector.getValue(i), 0.0) != 0)
            parseResult.setRating(contextId, i, vector.getValue(i));
      }


   }


   private void makeMatrixNotNullAndCorrectSize() {
      if (parseResult == null) {
         parseResult = new CollaborativeFilteringDataset(contextsDeclared, itemsDeclared);
      } else if (parseResult.matrix == null || parseResult.matrix.getNumberOfColumns() == 0 || parseResult.matrix.getNumberOfRows() == 0) {
         parseResult.matrix = new SparseDoubleMatrix(contextsDeclared, itemsDeclared);
      }

   }


   @Override
   protected void initParse() {
      parseResult = new CollaborativeFilteringDataset();
   }


   @Override
   protected void finishParse() {
      // Nothing to do
   }

}
