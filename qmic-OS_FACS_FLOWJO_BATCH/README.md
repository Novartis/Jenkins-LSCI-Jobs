# README #
OS_FACS_FLOWJO_BATCH is a Jenkins job  to assist in the creation of batch commands to execute FlowJo templates on a large number of data sets. FACS_FLOWJO_BATCH will not invoke FlowJo by itself, but it will prepare the batch file that can be executed on a workstation where FlowJo is installed and licensed to run.

FlowJo templates contain an  entire flow cytometric analysis, minus the FCS files. They are helpful when you analyze the same type of data over and over. Command line FlowJo is invoked by referencing an installed version of FlowJo. 

OS_FACS_FLOWJO_BATCH facilitates the creation of correctly formatted FlowJo batch commands for any number of input FCS files. The user selects appropriate FlowJo template and output preferences and the job build will **create one command for each input FCS file**. The generated batch file can then be downloaded to the workstation where FlowJo is installed and executed from the appropriate operating system console.

### What is this repository for? ###

The repository provides an archive of the key artifacts required to setup (or update) the job on a Jenkins server. Artifacts include:

* Job configuration, and job-specific properties and scripts
* Shared Groovy Scriptlets

## Prerequisites
The deployment of the OS_FACS_FLOWJO_BATCH Jenkins job assumes that a standard Jenkins or Jenkins-LSCI continous integration server is already installed and configured.
Additional [installation and configuration details](https://github.com/Novartis/Jenkins-LSCI/blob/master/userContent/docs/installation_and_use.md) are provided as part of the Novartis [Jenkins-LSCI](https://github.com/Novartis/Jenkins-LSCI) open source project

### Deployment Instructions ###

* Clone the repository ```git clone https://github.com/Novartis/Jenkins-LSCI-jobs.git```
* Deploy artifacts with [gradle](https://gradle.org/)
	* Open console in the nibr-OS_FACS_FLOWJO_BATCH repository folder and execute command ```gradle deploy```
	* Deployment creates a **backup of all original files** (if they exist) in **nibr-OS_FACS_FLOWJO_BATCH/backup** folder
	* Project configuration, scripts and properties are deployed to **$JENKINS_HOME/jobs/OS_FACS_FLOWJO_BATCH** folder
	* Scriptlets are deployed to **$JENKINS_HOME/scriptlet/scripts** folder	
* Review project plugins (shown below with latest version tested) and install as needed
	* uno-choice@2.1
	* groovy@2.0
	* groovy-postbuild@2.3.1
	* build-name-setter@1.6.5

### How do I build this job? ###

1. Define the INPUT data folder (containing subfolders with FCS files)
2. Define the OUTPUT_FOLDER where the results of the FlowJo analysis will be saved
3. Define the path to the FlowJo executable (as defined on the workstation where will FlowJo will be executed)
4. Define the path to the FlowJo analysis template to be used for the analysis
5. Define the path to the preferences file to be used for the analysis
6. Select FlowJo command options as needed (by default save, allGraphs are selected)
7. Annotate the build with a label and a description so it can be easily identified

### Attributions
The initial Jenkins-LSCI code was committed to accompany the article by Ioannis Moutsatsos K., et al. “Jenkins-CI, an Open-Source Continuous Integration System, as a Scientific Data and Image-Processing Platform.” Journal of Biomolecular Screening (2016): [1087057116679993](http://journals.sagepub.com/doi/abs/10.1177/1087057116679993). Please, reference this article when using code from the Jenkins-LSCI repository.

The Jenkins-LSCI project is not associated with the [Jenkins](https://jenkins.io/)(https://jenkins.io/) project.

The name "Jenkins" is a registered trademark in the USA (#4664929), held by  [Software in the Public Interest](http://www.spi-inc.org/) (SPI). The [Jenkins-LSCI logo](./userContent/images/Jenkins_LifeSci.png) is derivative work from the original [Jenkins logo] (https://wiki.jenkins-ci.org/display/JENKINS/Logo) licensed under the [Creative Commons Attribution-ShareAlike 3.0 Unported License](https://creativecommons.org/licenses/by-sa/3.0/).

The name "FlowJo" and FLOWJO® are registered trademarks owned by FlowJo,LLC

## License
The Jenkins-LSCI and the OS_FACS_FLOWJO_BATCH code are provided under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) license


