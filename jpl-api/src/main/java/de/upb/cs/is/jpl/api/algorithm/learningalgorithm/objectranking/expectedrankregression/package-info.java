/**
 * Provides the implementation for the Expected Rank Regression algorithm which uses the Base
 * learner is to be used for the regression and train the learning model, which is then used to
 * predict rankings for provided dataset or instance. Each object in a ranking is converted to an
 * intance with features of the object concatenated with features of the context and the score is
 * directly proportional to the rank of the object in the given ranking. This algorithm is based on
 * the learning a regression model on the transformed dataset for scoring function. This scoring
 * function is a weight vector equal to the length of the features of the objects, then the objects
 * are raked according the increasing order of the scores.
 * 
 * @author Pritha Gupta
 *
 */
package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;