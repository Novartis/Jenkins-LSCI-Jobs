/**
 * An adaptor script for executing the advanceDataMergeGroovy script as a Jenkins Job
 * The script gets parameterized from the environment variables
 *
 * Below are the CLI options for the main script
     h longOpt: 'help', 'script merges data from multiple csv files to a new single file'
     s longOpt: 'source', args:1, argName: 'csv folder', 'folder with separate csv files', required:true
     f longOpt: 'filter', args:1, argName: 'file filter', 'string for filtering files, default: csv'
     p longOpt: 'parentFilter', args:1, argName: 'parent filter', 'string for filtering parent folder, default: all'
     r longOpt: 'headerRows', args:1, argName: 'header rows', 'number of header rows, default: 1'
     m longOpt: 'fileMeta', args:0, argName: 'file metadata', 'includes file and folder names as extra columns'
     d longOpt: 'destination', args:1, argName: 'destination file', 'file where joined data will be written', required:true
     c longOpt: 'delimiter', args:1, argName: 'value delimiter', 'delimiter for data values, default: ,(comma)'
    @JIRA: DMPQM-562, DMPQM-198
    @Since: JUL-10-2014
    @LastUpdate: NOV-22-2016
    @Author: Ioannis K. Moutsatsos
 */

def env = System.getenv()
def options= new HashMap()
def mergedFileName='JData.csv'

options.s=env['SOURCE_FOLDER'].trim().replace('\\','/')
options.f=(env['FILE_FILTER']!=null)?env['FILE_FILTER'].trim():''
options.p=(env['FOLDER_FILTER']!=null)?env['FOLDER_FILTER'].trim():''
options.m=env['ADD_FILEMETA'].toBoolean()
options.d="${env['WORKSPACE'].replace('\\','/')}/$mergedFileName"

if (env['ADD_WELL_ID'].tokenize(',')[0]=='true'){
    options.w=true
    options.b=env['ADD_WELL_ID'].tokenize(',')[1]
    options.a=env['ADD_WELL_ID'].tokenize(',')[2]
}

def thisAdvancedDirMerge= new advanceDirMerge()
println getAdvanceDirMergeCLI(options)
//now we call the main script with the args array
thisAdvancedDirMerge.main(getAdvanceDirMergeCLI(options))

/*
    write some additional build properties
 */
//Create the properties object, and load it from the file system:
//Properties props = new Properties()
//File propsFile = new File("${env['WORKSPACE']}/merged.properties")
//assert propsFile.exists()
//props.load(propsFile.newDataInputStream())
//props.setProperty('download.merged.data',"${env['BUILD_URL']}artifact/$mergedFileName")
//props.store(propsFile.newWriter(), "PROPERTY=VALUE")

/*
method to build the cli args String [] required for the advanceDataMerge script
Note the casting of the list to String[] required by the script main(String[]) method call
 */
def getAdvanceDirMergeCLI(options){
    def args=[] //args is defined as a list but will be returned as a String[]
//    The following are required options-Exit if user did not provide folder
    (options.s=='')?System.exit(1):args.add("-s${options.s}")
     args.add("-d${options.d}")

    //now we use a set of ternary conditionals to review/set the CLI options from the UI
    (options.f!='') ?args.add("-f${options.f}"):''
    (options.p!='') ?args.add("-p${options.p}"):''
    (options.r>-1) ?args.add("-r${options.r}"):''
    (options.m) ?args.add('-m'):''
    (options.w) ?args.add('-w'):''
    (options.b!='') ?args.add("-b${options.b}"):''
    (options.a!='') ?args.add("-a${options.a}"):''
     args.add('-l') //creating merged properties log
    return args as String[]
}
