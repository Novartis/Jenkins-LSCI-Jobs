/*
An advanced utility for creating annotated lists of image/data files
    Starting with a parent folder (-s) the script will iterate over all sub-folders
    Sub-folders matching a user-provided pattern matcher (-p) are reviewed for files matching a second file pattern (-f)
    Matched files are added to a CSV formatted list (-d)
    The grandparent/parent folders can be used as additional metadata columns (-m)
Assumptions
    For Well-ID extraction Data/Image file names must adhere to a naming convention
      (prefix_WellID.png).
    @Since: DEC-12-2017
    @LastUpdate: DEC-13-2017
    @Author: Ioannis K. Moutsatsos
*/

import groovy.io.FileType

def cli = new CliBuilder(usage: 'advanceDirMerge.groovy -sd[fpml]')
cli.with {
    h longOpt: 'help', 'script merges data from multiple csv files to a new single file'
    s longOpt: 'source', args: 1, argName: 'csv folder', 'folder with separate csv files', required: true
    f longOpt: 'filter', args: 1, argName: 'file filter', 'string for filtering files, default: csv'
    p longOpt: 'parentFilter', args: 1, argName: 'parent filter', 'string for filtering parent folder, default: all'
    m longOpt: 'fileMeta', args: 0, argName: 'file metadata', 'includes file and folder names as extra columns'
    w longOpt: 'wellMeta', args: 0, argName: 'well metadata', 'includes well id metadata as extra columns'
    b longOpt: 'prefix', args: 1, argName: 'wellId prefix', 'wellid prefix in file name'
    a longOpt: 'postfix', args: 1, argName: 'wellId postfix', 'wellid postfix in file name'
    d longOpt: 'destination', args: 1, argName: 'destination file', 'file where joined data will be written', required: true
    l longOpt: 'writeLog', args: 0, argName: 'write log', 'writes a log file in the output folder'
}
def options = cli.parse(args)
parsedFolderList = []
parseParentOfFolderList = [] //grandparent of files
mergedFileList = []
mergedFileMeta = [] //list of merged file metadata

if (!options) {
    println options
    cli.usage()
    return
}
if (options.h) {
    cli.usage()
    return
}
def parentCsvFolder = new File(options.s)
assert parentCsvFolder.exists()
def combinedCSV = new File(options.d)

def fileMetaFlag = false
def wellIdFlag=false

def totalLineCount = 0

if (options.m) {
    fileMetaFlag = true
    println 'Adding 2 new columns with L1,L2 Folder Names'
}

if (options.w) {
    wellIdFlag = true
    println 'Adding 2 new columns with Well Id metadata'
}

def wellIdPrefix=''
def wellIdPostfix=''
if (options.b) {
    wellIdPrefix = options.b
    println 'Using WellID prefix: '+ wellIdPrefix
}
if (options.a) {
    wellIdPostfix = options.a
    println 'Using WellID postfix: '+ wellIdPostfix
}
/* default filter for selecting csvfiles to join */
def filePattern = ~/csv/
def parentPattern = ~/.*/

/*The user can alter the default filter with the optional -f CL argument
The filter can be a combination of file name and extension if a dot is included
For example: 
-f51.CSV will find files whose name contains '51' anywhere and their extension is .CSV (case insensitive)
-f51.txt will find files whose name contains '51' anywhere and their extension is .txt (case insensitive)
*/
if (options.f) {
    pattern = options.f.replace('.', '.*\\.')
    filePattern = ~/.*(?i)${pattern}/
//println 'RegExp:'+filePattern
}
if (options.p) {
    parentPattern = ~/.*(?i)${options.p}.*/
//println 'RegExp:'+filePattern
}

println "\nProcessing files from subfolders of:\t${parentCsvFolder}"
def l = 0 //start at line 0
parentCsvFolder.traverse(
        type: FileType.DIRECTORIES,
        nameFilter: parentPattern,
        maxDepth: 3
) { csvDir ->

    println "Folder matched by parent filter:\t${csvDir.parent}\\${csvDir.name}"
    parsedFolderList.add(csvDir.name)

    csvDir.traverse(
            type: FileType.FILES,
            nameFilter: filePattern,
            maxDepth: 3
    ) { f ->
        mergedFileList.add(f)
        l = 0 //start at line 0
        folderName = f.getParentFile().name// parent
        parentOfFolderName = (f.getParentFile()).getParentFile().name
        parseParentOfFolderList.add(parentOfFolderName)
        fName = f.name.lastIndexOf('.').with { it != -1 ? f.name[0..<it] : f.name }
//        if (f.isFile() && filePattern.matcher(f.name).find()) {
//
//        }//end if f.isFile
    }


}//end for each CSVdir

getMergedMeta(mergedFileList, combinedCSV, wellIdPrefix, wellIdPostfix, fileMetaFlag,wellIdFlag )

if (options.l) {
    println 'Writing Merge Properties Log'
    //Create the properties object, and load it from the file system:
    Properties props = new Properties()
    File propsFile = new File("${combinedCSV.parent}/merged.properties")
    props.setProperty('merged.folders.L1', parseParentOfFolderList.unique().join(','))
    props.setProperty('merged.folders.L2', parsedFolderList.unique().join(','))
    props.setProperty('merged.numberOf.files', mergedFileList.size() as String)
    props.setProperty('merged.numberOf.folders.L2', parseParentOfFolderList.unique().size() as String)
/* and save merge properties file */
    props.store(propsFile.newWriter(), "PROPERTY=VALUE")
    println "Merged properties saved in: ${propsFile}"
}

/*
Returns the merged file metadata
Assigns alphanumeric well-IDs to row/column numbers
 */

def getMergedMeta(fileList, output, wellIdPrefix, wellIdPostfix, boolean fileMetaFlag=false, boolean wellIDFlag = false) {

    fileMeta = [:]
    /*
    if WellID extraction was requested we prepare a map of WellIDs to row/column numbers
     */
    if (wellIDFlag) {
        plateWellID = [:]
        rowIds = 'A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,AE,AF'.tokenize(',')
        columnIds = (1..48)
        plate = GroovyCollections.combinations(rowIds, columnIds.collect { String.format('%02d', it) })
        assert plate.size() == 1536
        plate.each {
            thisRowNum = (rowIds.findIndexValues { r -> r == it[0] }[0] + 1)
            plateWellID.put(it.join(''), [thisRowNum, it[1]])
        }
    }
    fileList.eachWithIndex { it, ind ->
        if (fileMetaFlag){
            fileMeta['folderL1'] = (new File(it.parent)).getParentFile().name
            fileMeta['folderL2'] = it.getParentFile().name
        }

        fileMeta['fileName'] = it.name

        if (wellIDFlag) {
            fileMeta['wellID'] = (it.name - wellIdPrefix -wellIdPostfix).tokenize('.')[0]
            fileMeta['rowNumber'] = plateWellID[fileMeta['wellID']][0]
            fileMeta['columnNumber'] = plateWellID[fileMeta['wellID']][1]
        }
        fileMeta['fileUrl'] = "file://${it.canonicalPath.replace('\\', '/')}"
        if (ind == 0) {
//           println fileMeta.keySet().join(',')
            output << fileMeta.keySet().join(',') + '\n'
        } else {
//            println fileMeta.values().join(',')
            output << fileMeta.values().join(',') + '\n'
        }
    }
}

