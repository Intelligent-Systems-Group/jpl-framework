/**
 * Provides the implementation for the Rank SVM algorithm which uses the configuration which
 * provides which Base learner is to be used for the classification and train the learning model,
 * which is then used to prediction rankings for provided dataset or instance. This package
 * implements:
 * <ul>
 * <li>RankSVM is a pairwise method for designing ranking models, we generate pair wise preference
 * and convert them to binary classification on instance pairs, and then to solve the problem using
 * Support Vector Machines.</li>
 * <li>Order SVM is method which learns a scoring function which maximally separates the
 * higher-ranked objects from the lower-ranked ones.</li>
 * </ul>
 * 
 * @author Pritha Gupta
 *
 */
package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;