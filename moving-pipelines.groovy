import com.cloudbees.hudson.plugins.folder.*
import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

String[] FOLDER_NAMES = ['Backend Guild/BBB/', 'Cardiff/BPAY/', 'CTRL/MB/', 'CX/CXP-MAN/', 'CX/CXP-PORTAL/', 'CX/CXP-DM/', 'CX/CXP-PS/', 'F5/APPR/', 'Flow/flow/', 'Flow/FORMS/', 'Flow/FLOW/', 'Frontend Guild/BSFG/', 'QA Guild/CT/', 'Shared/WA3REF/', 'Tribe X/BDLC/', 'Tribe X/CXP-AUTO/', 'Tribe X/ENL/', 'Tribe X/HELM/', 'Tribe X/PLAT/', 'Tribe X/REF/', 'Tribe X/START/', 'Tribe X/ENL/', 'Tribe X/SDLC/', 'Tribe X/SDLCW/', 'Tribe X/TOOLS/', 'Tribe X/CXP-AUTO/', 'Tribe X/SDLC/', 'UA/ACT/', 'UA/AS/', 'UA/NOT/', 'UA/ACT/', 'UA/AS/', 'UA/NOT/']
String[] OLD_FOLDERS = ['Backend Guild/BBB/Freestyle/',  'Cardiff/Build/BPAY/', 'CTRL/Deploy/MB/', 'CX/Build/CXP-MAN/', 'CX/Build/CXP-PORTAL/', 'CX/Deploy/CXP-DM/', 'CX/Deploy/CXP-PS/', 'F5/Build/APPR/', 'Flow/Build/flow/', 'Flow/Build/FORMS/', 'Flow/Deploy/FLOW/', 'Frontend Guild/Build/BSFG/', 'QA Guild/Build/CT/', 'Shared/Build/WA3REF/', 'Tribe X/Build/BDLC/', 'Tribe X/Build/CXP-AUTO/', 'Tribe X/Build/ENL/', 'Tribe X/Build/HELM/', 'Tribe X/Build/PLAT/', 'Tribe X/Build/REF/', 'Tribe X/Build/START/', 'Tribe X/Deploy/ENL/', 'Tribe X/Deploy/SDLC/', 'Tribe X/Deploy/SDLCW/', 'Tribe X/Deploy/TOOLS/', 'Tribe X/Freestyle/CXP-AUTO/', 'Tribe X/Freestyle/SDLC/', 'UA/Build/ACT/', 'UA/Build/AS/', 'UA/Build/NOT/', 'UA/Deploy/ACT/', 'UA/Deploy/AS/', 'UA/Deploy/NOT/']

jenkins = Jenkins.instance

for(i=0; i <= FOLDER_NAMES.length; i++) {
  def arrFolder = FOLDER_NAMES[i].tokenize('/')
  def mainFolder = jenkins.getItem(arrFolder[0])
  
  if (mainFolder == null) {
    // First create the new folder if it doesn't exist or if no existing job has the same name
    println "Creating " + arrFolder[0]
    mainFolder = jenkins.createProject(Folder.class, arrFolder[0])
  } 
  def subFolder = mainFolder.getItem(arrFolder[1])
  if (subFolder == null) {
    println "Creating " + FOLDER_NAMES[i]
    subFolder = mainFolder.createProject(Folder.class, arrFolder[1])
  }
  def folder = jenkins.getItemByFullName(FOLDER_NAMES[i])
  if (folder == null) {
    println "ERROR: Folder '$FOLDER_NAMES[i]' not found"
    return
  }

  // Find jobs in main folder
  def oldFoldersArr = OLD_FOLDERS[i].tokenize('/')
  def tribe =  jenkins.getItem(oldFoldersArr[0])
  def jobType = tribe.getItem(oldFoldersArr[1])
  def proj = jobType.getItem(oldFoldersArr[2])

  def found = proj.getAllItems()

  // Move them
  found.each { job ->
    println "Moving '$job.name' to '$folder.name'"
    Items.move(job, subFolder)
    proj.delete()
  }
}
