package de.upb.cs.is.jpl.api.dataset.multilabelclassification;


import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This class is the implementation of a parser which is specifically tailored to multilabel
 * classification. It can read a GPRF dataset of the correct form and will transform it into a
 * {@link MultilabelClassificationDataset}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationDatasetParser extends ADatasetParser {

   private static final String ERROR_FOUND_INVALID_RATING_INFORMATION_LINE = "Found invalid rating information line: %s.";

   private static final String ERROR_GIVEN_DATASET_NOT_ABSOLUTE = "The given dataset is not an absolute one.";

   private static final String ERROR_CANNOT_WORK_WITH_SPARSE_DATASET = "Cannot work with a sparse dataset that has no feature declarations.";
   private static final String ERROR_UNEQUAL_AMOUNT_CONTEXTS_RATINGS = "The amount of contexts and ratings does not coincide.";
   private static final String ERROR_CONTEXT_WITHOUT_FEATURES = "Encountered a context without features; line: %s.";

   private MultilabelClassificationDataset multilabelClassificationDataset;


   @Override
   public MultilabelClassificationDataset getDataset() {
      if (multilabelClassificationDataset == null) {
         return new MultilabelClassificationDataset();
      }
      return multilabelClassificationDataset;
   }


   @Override
   protected void initParse() throws ParsingFailedException {
      if (isRelative) {
         throw new ParsingFailedException(ERROR_GIVEN_DATASET_NOT_ABSOLUTE);
      }
      multilabelClassificationDataset = new MultilabelClassificationDataset();
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      if (multilabelClassificationDataset.getCorrectResults().size() != multilabelClassificationDataset.getFeatureVectors().size()) {
         throw new ParsingFailedException(ERROR_UNEQUAL_AMOUNT_CONTEXTS_RATINGS);
      }
   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      // not needed as we ignore item declarations
   }


   @Override
   protected void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      String[] contextLine = contextInformationLine.split(ID_DIVIDER);

      if (contextLine.length <= 1) {
         throw new ParsingFailedException(String.format(ERROR_CONTEXT_WITHOUT_FEATURES, contextInformationLine));
      }

      String featureDeclaration = contextLine[1].trim();
      String[] splitFeatureDeclarationLine = featureDeclaration.split(VECTOR_DIVIDER);
      IVector parsedContextVector;
      if (contextFeatures.isEmpty()) {
         if (featureDeclaration.contains(FEATURE_MARKER)) {
            throw new ParsingFailedException(ERROR_CANNOT_WORK_WITH_SPARSE_DATASET);
         }
         parsedContextVector = parseInformationVector(splitFeatureDeclarationLine, splitFeatureDeclarationLine.length);
      } else {
         parsedContextVector = parseInformationVector(splitFeatureDeclarationLine, contextFeatures.size());
      }

      multilabelClassificationDataset.addFeatureVector(parsedContextVector.asArray());
   }


   /**
    * Returns the leading id as an integer in the given line. This method assumes that the given
    * line is either a valid GPRF context information, item information or rating information line.
    * 
    * @param line a valid GPRF context information, item information or rating information line
    * @return the leading id as an integer in the given line
    */
   @SuppressWarnings("unused")
   private int getLeadingID(String line) {
      String[] splittedLine = line.split(ID_DIVIDER);
      String idDeclaration = splittedLine[0].trim();
      String idAsString = idDeclaration.substring(idDeclaration.indexOf(CONTEXT_MARKER) + 1);
      return Integer.parseInt(idAsString);
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      String[] contextRatingSplit = ratingInformationLine.split(ID_DIVIDER);
      if (contextRatingSplit.length < 2) {
         throw new ParsingFailedException(String.format(ERROR_FOUND_INVALID_RATING_INFORMATION_LINE, ratingInformationLine));
      }
      IVector vector = parseAbsoluteRatingVector(contextRatingSplit[1]);
      multilabelClassificationDataset.addCorrectResult(vector.toSparseVector());

   }

}
