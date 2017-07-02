package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * 
 * This class implements the Plackett-Luce model for the rank aggregation learning problem. It is
 * linked to the {@link Enum} value, {@link ELearningAlgorithm#PLACKETT_LUCE}, whereby the
 * Minorization and Maximization algorithm us used to find the Maximum Likelihood Estimation
 * parameters.
 * 
 * The implementation of this class is based on the code from
 * <a href="https://github.com/mizzao/libmao">Copyright (c) 2014 Andrew Mao</a> (libmao). The code
 * from Andrew Mao for the Plackett-Luce model is based on the code from (Hunter, D.R., 2004)).
 * 
 * @see <a href="http://sites.stat.psu.edu/~dhunter/code/btmatlab/plackmm.m">MM algorithms for
 *      generalized Bradley-Terry models. (Hunter, D.R., 2004)</a>
 * @see <a href="https://github.com/mizzao/libmao">https://github.com/mizzao/libmao</a>
 * @author Andreas Kornelsen
 *
 */
public class PlackettLuceLearningAlgorithm extends ALearningAlgorithm<PlackettLuceConfiguration> {

   private static final String ERROR_OPERATOR = "There is an unsupported ranking operator in the training dataset.";


   /**
    * Creates a new Plackett-Luce learning algorithm with the enum identifier.
    */
   public PlackettLuceLearningAlgorithm() {
      super(ELearningAlgorithm.PLACKETT_LUCE.getIdentifier());
   }


   @Override
   public ADatasetParser getDatasetParser() {
      return new RankAggregationDatasetParser();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      RankAggregationDataset rankAggregationDataset = (RankAggregationDataset) dataset;

      PlackettLuceConfiguration plackettLuceConfiguration = getAlgorithmConfiguration();

      IVector gamma = getGamma(rankAggregationDataset, plackettLuceConfiguration);

      double sumGamma = 0;
      for (double gammaValue : gamma.asArray()) {
         sumGamma += gammaValue;
      }
      double[] mleParameters = gamma.divideByConstantToCopy(sumGamma).asArray();

      return new PlackettLuceLearningModel(mleParameters, rankAggregationDataset.getLabels());
   }


   @Override
   public PlackettLuceLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (PlackettLuceLearningModel) super.train(dataset);
   }


   /**
    * Returns the gamma for the Maximum Likelihood Estimation parameters.
    *
    * @param rankAggregationDataset the rank aggregation dataset
    * @param plackettLuceConfiguration the plackett luce configuration
    * @return the gamma for the Maximum Likelihood Estimation parameters
    * @throws TrainModelsFailedException if the validation of the rankings failed
    */
   private IVector getGamma(RankAggregationDataset rankAggregationDataset, PlackettLuceConfiguration plackettLuceConfiguration)
         throws TrainModelsFailedException {
      int numberOfLabels = rankAggregationDataset.getLabels().size();
      int numberOfInstances = rankAggregationDataset.getNumberOfInstances();


      IVector w = getAmountCandidatePlacedHigher(rankAggregationDataset);
      IVector gamma = new DenseDoubleVector(numberOfLabels, 1);

      int iter = 0;
      double lastLogLikelihood = Double.NEGATIVE_INFINITY;
      double absImpr;
      double relImpr;
      double euclideanNormBetweenNewAndOldGamma;

      do {
         double[][] g = new double[numberOfInstances][numberOfLabels];
         for (int j = 0; j < numberOfInstances; j++) {
            int[] ranking = rankAggregationDataset.getRankingOfInstance(j).getObjectList();


            double gsum = 0;
            for (int i = numberOfLabels - 1; i >= 0; i--) {
               gsum += gamma.getValue(ranking[i]);
               if (i == numberOfLabels - 1)
                  continue;
               g[j][i] = 1 / gsum;
            }
         }

         double logLikelihood = getLL(w, gamma);
         for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[i].length; j++)
               if (g[i][j] > 0)
                  logLikelihood += Math.log(g[i][j]);

         absImpr = logLikelihood - lastLogLikelihood;
         relImpr = -absImpr / lastLogLikelihood;
         lastLogLikelihood = logLikelihood;

         for (int j = 0; j < numberOfInstances; j++) {
            double cumsum = 0;
            for (int i = 0; i < numberOfLabels; i++) {
               cumsum += g[j][i];
               g[j][i] = cumsum;
            }
         }

         double[] denoms = new double[numberOfLabels];
         for (int j = 0; j < numberOfInstances; j++) {
            int[] ranking = rankAggregationDataset.getRankingOfInstance(j).getObjectList();
            for (int i = 0; i < numberOfLabels; i++) {
               denoms[ranking[i]] += g[j][i];
            }
         }

         DenseDoubleVector newGamma = (DenseDoubleVector) w.duplicate();
         w.divideByVectorPairwise(new DenseDoubleVector(denoms));

         IVector diff = newGamma.duplicate();
         diff.subtractVector(gamma);
         euclideanNormBetweenNewAndOldGamma = diff.euclideanNorm();

         gamma = newGamma;
         iter++;
      } while ((euclideanNormBetweenNewAndOldGamma > plackettLuceConfiguration.getNormTolerance())
            || (iter < plackettLuceConfiguration.getMaxIterations()
                  && (Double.isNaN(relImpr) || relImpr > plackettLuceConfiguration.getLogLikelihoodTolerance())));

      return gamma;
   }


   /**
    * Returns the times each candidate placed higher than last in rankings easy way: subtract 1 for
    * each time someone placed last.
    *
    * @param rankAggregationDataset the rank aggregation dataset
    * @return the amount candidate placed higher
    * @throws TrainModelsFailedException the train models failed exception
    */
   private DenseDoubleVector getAmountCandidatePlacedHigher(RankAggregationDataset rankAggregationDataset)
         throws TrainModelsFailedException {
      int numberOfLabels = rankAggregationDataset.getLabels().size();
      int numberOfInstances = rankAggregationDataset.getNumberOfInstances();


      DenseDoubleVector w = new DenseDoubleVector(numberOfLabels, numberOfInstances);

      for (int index = 0; index < numberOfInstances; index++) {
         Ranking ranking = rankAggregationDataset.getRankingOfInstance(index);

         int[] objectList = ranking.getObjectList();
         w.incrementValueAt(objectList[numberOfLabels - 1], -1.0);

         validateComparativeOperators(ranking.getCompareOperators());
      }
      return w;
   }


   /**
    * Returns the Log Likelihood.
    *
    * @param w the weight vector
    * @param gamma the gamma vector
    * @return the log-likelihood result
    */
   private double getLL(IVector w, IVector gamma) {

      DenseDoubleVector mapToLog = (DenseDoubleVector) gamma.duplicate();
      for (int i = 0; i < mapToLog.length(); i++) {
         mapToLog.setValue(i, Math.log(mapToLog.getValue(i)));
      }

      return w.dotProduct(mapToLog);
   }


   /**
    * Validates whether the operators are set correctly or not. The BordaCount Algorithm doesn't
    * work for all operators. It works for ordered relation.
    *
    * @param comparativeOperators the comparative operators
    * @throws TrainModelsFailedException if the order is not total
    */
   private void validateComparativeOperators(int[] comparativeOperators) throws TrainModelsFailedException {
      for (Integer comparator : comparativeOperators) {
         if (Ranking.COMPARABLE_ENCODING != comparator) {
            throw new TrainModelsFailedException(ERROR_OPERATOR);
         }
      }
   }


   @Override
   public void init() {
      // There is no configuration required during the init phase.
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new PlackettLuceConfiguration();
   }
}
