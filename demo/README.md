![](https://github.com/Intelligent-Systems-Group/jpl-framework/blob/master/logo.png)

### CLI
As the CLI depends on the API, you firstly need to install the API using `mvn clean install` in the according API directory. Once this is done, you can go to the CLI directory and use the `mvn package` command to build a jar file. 

If you do not want to manually install the API on your system, you can also go to the `jpl-parent` directory and use the `mvn package` command, which will ensure that the CLI dependency to the API will be resolved automatically. In case you will only use the CLI, this is method is the suggested method.

You need to run the following commands after building and adding to the folder the jpl-cli.jar:
```console
user@laptop:~$ java -jar jpl-cli.jar run_toolchain -c="cross-validation/cross_validation_evaluation_system_configuration.json"
user@laptop:~$ java -jar jpl-cli.jar run_toolchain -c="supplied-testset/supplied_testset_evaluation_system_configuration.json"
user@laptop:~$ java -jar jpl-cli.jar run_toolchain -c="in-sample/in_sample_evaluation_system_configuration.json"
user@laptop:~$ java -jar jpl-cli.jar run_toolchain -c="percentage-split/percenatge_split_system_configuration.json"
```
## More Information
For more detailed information, please visit our [CLI-Getting Started](http://jpl-framework.cs.upb.de:8090/display/BAC/CLI+-+Getting+Started).

