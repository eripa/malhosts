#!/usr/bin/env groovy
/**
* malhosts.groovy
* 
* @author: Eric Ripa - https://github.com/eripa
*
* This script was written to simplify the installation and updating
* of Dan Pollocks excellent hosts file that is used to to prevent your
* computer from connecting to selected internet hosts. Please read more
* on http://someonewhocares.org/hosts/
* 
*/

import java.text.*

def __version__ = "0.1"

class HostFile {
  List personal_part = []
  List someonewhocares_part = []

  String toString() {
    // universal line break
    def ln = System.getProperty('line.separator')
    // join both lists.
    def ret = this.personal_part+this.someonewhocares_part
    return ret.join("$ln")
  }
}

def parseArgs(args) {
  def cli = new CliBuilder(usage: 'malhosts.groovy [--help|-h] [--legacy|-l] [--dry-run|-d] [--quiet|-q]')

  cli.with {
      h longOpt: 'help', 'Show usage information'
      l longOpt: 'legacy', 'Use legacy format (127.0.0.1 instead of faster 0.0.0.0)'
      d longOpt: 'dry-run', 'Don\'t do anything, only display what will happen'
      q longOpt: 'quiet', 'Don\'t print anything, useful for automatic runs'
      c longOpt: 'clean', 'Remove all entries that were added by malhosts'
  }
  cli.footer = """
  ------------ Credits ------------ 
  Please give thanks to Dan Pollock of 'http://someonewhocares.org' for providing the source hosts file
  ---------------------------------
  The hosts file is brought to you by Dan Pollock and can be found at
  http://someonewhocares.org/hosts/
  You are free to copy and distribute this file, as long the original 
  URL is included. See below for acknowledgements.
  Please forward any additions, corrections or comments by email to
  hosts@someonewhocares.org

  Use this file to prevent your computer from connecting to selected
  internet hosts. This is an easy and effective way to protect you from 
  many types of spyware, reduces bandwidth use, blocks certain pop-up 
  traps, prevents user tracking by way of \"web bugs\" embedded in spam,
  provides partial protection to IE from certain web-based exploits and
  blocks most advertising you would otherwise be subjected to on the 
  internet. 

  There is a version of this file that uses 0.0.0.0 instead of 127.0.0.1 
  available at http://someonewhocares.org/hosts/zero/.
  On some machines this may run minutely faster, however the zero version
  may not be compatible with all systems."""

  def options = cli.parse(args)
  if (options.h) {
    cli.usage()
    System.exit(0)
  }
  return options
}

// Method for selecting the desired hosts file
def getUrl(legacy) {
  switch(legacy) {
    case true:
      // use legacy formatted file for older systems 127.0.0.1
      def url = "http://someonewhocares.org/hosts/hosts"
      break
    case false:
      // use faster syntax with 0.0.0.0
      def url = "http://someonewhocares.org/hosts/zero/hosts"
      break
  }
}

// Method for downloading a file to a string
def getStringFromUrl(url) {
  try {
    String hosts = url.toURL().getText()
  } catch (java.io.FileNotFoundException e) {
    println "File ${e.message} not found!"
    throw new RuntimeException()
  }
}

def getEtcPath() {
  switch(System.getProperty('os.name')) {
    case 'Mac OS X':
      '/etc'
      break
    case 'Linux':
      '/etc'
      break
    case 'Windows':
      System.getenv("WINDIR") + "\\system32\\drivers\\etc"
      break
  }
}

def writeToFile(List directory_list, String file_name, String content) {
  // get cross-platform line endings 
  def ln = System.getProperty('line.separator')

  directory_list.add(file_name)
  def absolute_path = directory_list.join(File.separator) 

  File file = new File(absolute_path)

  // make sure to overwrite the file so we don't get any duplicate entries
  file.newWriter()

  file << content
  file << ln
}

// to implement proxy support later on, look at this:
//System.properties.putAll( ["http.proxyHost":"proxy-host", "http.proxyPort":"proxy-port","http.proxyUserName":"user-name", "http.proxyPassword":"proxy-passwd"] )

def options = parseArgs(args)
if (!options.q) { println "malhosts ${__version__}\nlegacy: ${options.l}, dry-run: ${options.d}" }

def url = getUrl(options.l)

List malhosts_BEGIN = ["############ MALHOSTS BEGIN MARK, DO NOT REMOVE ############"]
List malhosts_END = ["############ MALHOSTS END MARK, DO NOT REMOVE ############"]
List hosts_list = getStringFromUrl(url).split('\n')

// Create a HostFile instance with the new hostfile and start-end-marks
new_hostfile = new HostFile(["someonewhocares_part": malhosts_BEGIN + hosts_list + malhosts_END])

// Get the current host
List hosts_file_on_disk = new File('/etc/hosts').text.split('\n')

// Find section of current malhost entries, so that they can be ignored
def start_idx = hosts_file_on_disk.findIndexOf {
  it == malhosts_BEGIN[0]
}
def stop_idx = hosts_file_on_disk.findIndexOf {
  it == malhosts_END[0]
}

if (start_idx != -1) {
  // Previous entries found, let's use the part outside of old malhosts entry
  def before = hosts_file_on_disk[0..start_idx-1]
  def after = hosts_file_on_disk[stop_idx..-1]
  // remove the END marker as we cannot reliably do it with list slicing above
  after.remove(0)
  new_hostfile.personal_part = before + after
} else {
  // it has not been run before, lets just add current hosts file
  new_hostfile.personal_part = hosts_file_on_disk
}

if (options.d) {
  // dry-run selected let's give the user some indication on what would happen
  println '--------------- Dry run report ---------------'
  println 'The following lines would be kept unchanged:'
  println '----------------------------------------------'
  println ''
  new_hostfile.personal_part.each { println it }
  println ''
  println '--------------- Dry run report ---------------'
  println 'A sample of the lines that will be added to the end:'
  println '----------------------------------------------'
  println ''
  println new_hostfile.someonewhocares_part[0]
  new_hostfile.someonewhocares_part[100..200].each { println it }
  println new_hostfile.someonewhocares_part[-1]
  System.exit(0)
}

if (options.c) {
  // clean out the file
  // get cross-platform line endings 
  def ln = System.getProperty('line.separator')
  writeToFile([getEtcPath()], 'hosts', new_hostfile.personal_part.join(ln))
  println "Entries cleaned out!"
  System.exit(0)
} else {
  // update file
  writeToFile([getEtcPath()], 'hosts', new_hostfile.toString())
  println "Entries added/updated!"
}


