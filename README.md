![](https://github.com/Intelligent-Systems-Group/jpl-framework/blob/master/logo.png)

The jPL framework was originally created by a student project group under the supervision of the [Intelligent Systems Group](https://www-old.cs.uni-paderborn.de/fachgebiete/intelligente-systeme.html) at the [University of Paderborn](https://www.uni-paderborn.de/en/university/). The aim of this framework is to provide a generic framework to evaluate different algorithms in the context of preference learning. Preference learning is a subfield of machine learning, which deals with learning prediction models using preference information.
Learning algorithms solving the following preference learning problems are implemented so far:

* [Collaborative Filtering](http://jpl-framework.cs.upb.de:8090/display/BAC/Collaborative+Filtering)
* [Instance Ranking](http://jpl-framework.cs.upb.de:8090/display/BAC/Instance+Ranking)
* [Label Ranking](http://jpl-framework.cs.upb.de:8090/display/BAC/Label+Ranking)
* [Multilabel Classification](http://jpl-framework.cs.upb.de:8090/display/BAC/Multilabel+Classification)
* [Object Ranking](http://jpl-framework.cs.upb.de:8090/display/BAC/Object+Ranking)
* [Ordinal Classification](http://jpl-framework.cs.upb.de:8090/display/BAC/Ordinal+Classification)
* [Rank Aggregation](http://jpl-framework.cs.upb.de:8090/display/BAC/Rank+Aggregation)

On the architecture level our framework can be divided into two different parts. The [*jPL API*](http://jpl-framework.cs.upb.de:8090/display/BAC/API) provides necessary classes to integrate the different learning algorithms into your own projects whereas the [*jPL CLI*](http://jpl-framework.cs.upb.de:8090/display/BAC/Command+Line+Interface) is a complete command line interface.
The jPL framework can be used with datasets in the [Generic Preference Representation Format (GPRF)](http://jpl-framework.cs.upb.de:8090/display/BAC/GPRF+in+Detail). This is a format which is created by us for the context of preference learning. In addition, we have created the [GPRF dataset transformer](http://jpl-framework.cs.upb.de:8090/display/BAC/GPRF+Dataset+Transformer) to ensure that the user can easily convert other dataset formats into the GPRF.

## Getting Started 
For a comprehensive guide on how to get started, we refer to the [Getting Started](http://jpl-framework.cs.upb.de:8090/display/BAC/Getting+Started) page in our wiki.

TL;DR:
The following code gives a quick introduction on how to initialize two multilabel classification learning algorithms and run an evaluation of the two of them based on a given dataset in the GPRF format using the API. 

    //Create the dataset file
    DatasetFile datasetFile = new DatasetFile(new File("datasets/emotions-arff.gprf"));
    
    //Create the learning algorithms
    ClassifierChainsLearningAlgorithm classifierChains = new ClassifierChainsLearningAlgorithm();
    BinaryRelevanceLearningAlgorithm binaryRelevanceLearning = new BinaryRelevanceLearningAlgorithm();
    
    //Create an in sample evaluation
    MultilabelClassificationInSampleEvaluation evaluation = new MultilabelClassificationInSampleEvaluation();
    
    //Setup the evaluation with the dataset and the algorithms
    evaluation.setupEvaluation(Arrays.asList(datasetFile), Arrays.asList(classifierChains, binaryRelevanceLearning));
    evaluation.evaluate();
    
    //Print evaluation result
    System.out.println(evaluation.interpretEvaluationResult());

## Project Structure
The project itself consists of three [Maven](https://maven.apache.org/) projects, where we distinguish between the `jpl-api` and the `jpl-cli` project (the latter having a dependency to the first) and finally the `jpl-parent`, which acts as a Maven aggregator project for the other two. 

## Quick Building
The easiest way to build the parts of the jPL framework is to use [Maven](https://maven.apache.org/).

### API
In order to build the API on the command line, simply navigate to the according subdirectory and type `mvn clean install`. Once this is done, you installed the jPL API to your local Maven reository and can now use it freely inside your projects as a Maven dependency.

### CLI
As the CLI depends on the API, you firstly need to install the API using `mvn clean install` in the according API directory. Once this is done, you can go to the CLI directory and use the `mvn package` command to build a jar file. 

If you do not want to manually install the API on your system, you can also go to the `jpl-parent` directory and use the `mvn package` command, which will ensure that the CLI dependency to the API will be resolved automatically. In case you will only use the CLI, this is method is the suggested method.

## Version
The current version of the jPL framework is: 0.0.9

## Contribution guidelines 
*Coming soon!*

## More Information
For more detailed information, please visit our [Confluence handbook](http://jpl-framework.cs.upb.de:8090/display/BAC/Introduction).

## License
Currently the project is licensed under **GPL v3.0**. 
