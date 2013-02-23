#!/usr/bin/env groovy
/**
* malhosts.groovy
* 
* Main app
* 
* 
*/

import HostFile
import java.text.*

def __version__ = "0.1"

def parseArgs(args) {
  def cli = new CliBuilder(usage: 'malhosts.groovy --help/-h --legacy/-l --dry-run/-d')

  cli.with {
      h longOpt: 'help', 'Show usage information'
      l longOpt: 'legacy', 'Use legacy format (127.0.0.1 instead of faster 0.0.0.0)'
      d longOpt: 'dry-run', 'Don\'t do anything, only display what will happen'
  }

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

// to implement proxy support later on, look at this:
//System.properties.putAll( ["http.proxyHost":"proxy-host", "http.proxyPort":"proxy-port","http.proxyUserName":"user-name", "http.proxyPassword":"proxy-passwd"] )

def host = new Host(['ip': '0.0.0.0', 'hostname': 'lanboll.com', 'global': true])

assert host.ip == '0.0.0.0'
assert host.hostname == 'lanboll.com'
assert host.global

def options = parseArgs(args)
println "malhosts ${__version__}\nlegacy: ${options.l}, dry-run: ${options.d}"

def url = getUrl(options.l)

List malhosts_BEGIN = ["############ MALHOSTS BEGIN MARK, DO NOT REMOVE ############"]
List malhosts_END = ["############ MALHOSTS END MARK, DO NOT REMOVE ############"]
List hosts_list = getStringFromUrl(url).split('\n')

host_list_with_marks = malhosts_BEGIN += hosts_list
host_list_with_marks = host_list_with_marks += malhosts_END

new_hostfile = new HostFile(["someonewhocares_part": host_list_with_marks])

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
  // let's use the part outside of old malhosts entry
  new_hostfile.personal_part = hosts_file_on_disk[0..start_idx-1] + hosts_file_on_disk[stop_idx+1..-1]
} else {
  // it has not been run before, lets just add current hosts file
  new_hostfile.personal_part = hosts_file_on_disk
}


new_hostfile.personal_part.each { println it }
new_hostfile.someonewhocares_part.each { println it }

