# qmic-OS_Advanced_Dir_Merge

**OS_Advanced_Dir_Merge** is a Jenkins utility job that enables users to **merge key metadata from files nested in a complex hierarchy**. The merged file metadata is saved to a tabular format (comma separated value table), that can be used for other purposes. 

Scientific instruments such as the iQue, acquire data from assay plates in a complex nested folder structure where each assay plate is represented by a folder and each well of the assay plate is represented by a file. User/experimentally defined hierarchy levels are also possible as shown in the example below
```
Experiment_01
	|
	|__DataOutput_01
		|
		|__BARCODE-ASSAY_PLATE_01
		|	|
		|	|__FlowJo_WebFiles
		|		|
		|		|Layout.Well_A01.fcs.png
		|		|Layout.Well_A02.fcs.png
		|		|Layout.Well_A03.fcs.png
		|		|.....
		|__BARCODE-ASSAY_PLATE_02
			|
			|__FlowJo_WebFiles
				|
				|Layout.Well_A01.fcs.png
				|Layout.Well_A02.fcs.png
				|Layout.Well_A03.fcs.png
				|.....
```
Well identifiers are also of interest to the users and are frequently requested file metadata. Typical naming conventions for well-level files include the well identifier as part of the file name (for example Layout.Well_**A01**.fcs.png). The OS_Advanced_Dir_Merge utility, makes it easy to extract file and folder metadata, as well as well identifiers from files of interest. In addition, OS_Advanced_Dir_Merge allows **user-defined filters** for files and folders of interest when it's necessary to select only a subset of the scanned files/folders.The merged metadata table can be used to annotate well-level data analysis with the additional information provided by these files. For example, in Spotfire we can link well-level data with the OS_Advanced_Dir_Merge output to display images of the FACS scatterplots and gate settings using the fileURL table column.

Note that the OS_Advanced_Dir_Merge **maximum scan depth for nested folders is 2 levels**. 

A **sample output** from OS_Advanced_Dir_Merge is shown below: 

folderL1 | folderL2 | fileName | wellID | rowNumber | columnNumber | fileUrl
------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- 
BARCODE-ASSAY_PLATE_01 | FlowJo_Web_files | Layout.Well_H08.fcs.png | H08 | 8 | 08 | file:////example.com/lab/AW2017/Experiment_01/DataOutput_01/BARCODE-ASSAY_PLATE_01/FlowJo_Web_files/Layout.Well_H08.fcs.png
BARCODE-ASSAY_PLATE_01 | FlowJo_Web_files | Layout.Well_L10.fcs.png | L10 | 12 | 10 | file:////example.com/lab/AW2017/Experiment_01/DataOutput_01/BARCODE-ASSAY_PLATE_01/FlowJo_Web_files/Layout.Well_L10.fcs.png
BARCODE-ASSAY_PLATE_01 | FlowJo_Web_files | Layout.Well_J06.fcs.png | J06 | 10 | 06 | file:////example.com/lab/AW2017/Experiment_01/DataOutput_01/BARCODE-ASSAY_PLATE_01/FlowJo_Web_files/Layout.Well_J06.fcs.png
BARCODE-ASSAY_PLATE_01 | FlowJo_Web_files | Layout.Well_K19.fcs.png | K19 | 11 | 19 | file:////example.com/lab/AW2017/Experiment_01/DataOutput_01/BARCODE-ASSAY_PLATE_01/FlowJo_Web_files/Layout.Well_K19.fcs.png
BARCODE-ASSAY_PLATE_01 | FlowJo_Web_files | Layout.Well_I16.fcs.png | I16 | 9 | 16 | file:////example.com/lab/AW2017/Experiment_01/DataOutput_01/BARCODE-ASSAY_PLATE_01/FlowJo_Web_files/Layout.Well_I16.fcs.png



### What is this repository for? ###

The repository provides an archive of the key artifacts required to setup (or update) the job on a Jenkins server. Artifacts include:

* Job configuration, and job-specific properties and scripts
* Shared Groovy Scriptlets
* Shared External scripts

### Deployment Instructions ###

* Clone the repository ```git clone https://github.com/Novartis/Jenkins-LSCI-jobs.git```

* Deploy artifacts with [gradle](https://gradle.org/)
	* Open a console in the qmic-OS_Advanced_Dir_Merge repository folder and execute command ```gradle deploy```
	* Deployment creates a **backup of all original files** (if they exist) in **qmic-OS_Advanced_Dir_Merge/backup** folder
	* Project configuration, scripts and properties are copied to **$JENKINS_HOME/jobs/OS_Advanced_Dir_Merge** folder
	* Scriptlets are copied to **$JENKINS_HOME/scriptlet/scripts** folder
	* Files in  **OS_Advanced_Dir_Merge/externalScripts** are copied to paths dictated in the config.xml file                                                                      
* Review project plugins (shown below with latest version tested) and install as needed
	* uno-choice@2.1
	* groovy@2.0
	* summary_report@1.15
	* build-name-setter@1.6.5

### How do I build this job? ###
This is a freestyle Jenkins job that requires parameters
1. Select the parent folder where the search for files starts
2. Define file and folder filters (this will select files and folders for metadata extraction)
3. Decide whether to add the Level 1 and Level 2 folders as 2 extra columns
4. Decide whether to parse the Well ID from file names (requires you to provide an example)
5. Annotate the build output with a name and a description

### Attributions
The initial Jenkins-LSCI code was committed to accompany the article by Ioannis Moutsatsos K., et al. ?Jenkins-CI, an Open-Source Continuous Integration System, as a Scientific Data and Image-Processing Platform.? Journal of Biomolecular Screening (2016): [1087057116679993](http://journals.sagepub.com/doi/abs/10.1177/1087057116679993). Please, reference this article when using code from the Jenkins-LSCI repository.

The Jenkins-LSCI project is not associated with the [Jenkins](https://jenkins.io/)(https://jenkins.io/) project.

The name "Jenkins" is a registered trademark in the USA (#4664929), held by  [Software in the Public Interest](http://www.spi-inc.org/) (SPI). The [Jenkins-LSCI logo](./userContent/images/Jenkins_LifeSci.png) is derivative work from the original [Jenkins logo] (https://wiki.jenkins-ci.org/display/JENKINS/Logo) licensed under the [Creative Commons Attribution-ShareAlike 3.0 Unported License](https://creativecommons.org/licenses/by-sa/3.0/).

## License
The Jenkins-LSCI and the OS_Advanced_Dir_Merge code are provided under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) license

