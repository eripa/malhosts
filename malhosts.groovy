#!/usr/bin/env groovy
/**
* malhosts.groovy
* 
* Main app
* 
* 
*/

import Host
import ProgramExitException

// to implement proxy support later on, look at this:
//System.properties.putAll( ["http.proxyHost":"proxy-host", "http.proxyPort":"proxy-port","http.proxyUserName":"user-name", "http.proxyPassword":"proxy-passwd"] )


def download(address)
{
    def file = new FileOutputStream(address.tokenize("/")[-1])
    def out = new BufferedOutputStream(file)
    out << new URL(address).openStream()
    out.close()
}

def host = new Host(['ip': '0.0.0.0', 'hostname': 'lanboll.com', 'global': true])

assert host.ip == '0.0.0.0'
assert host.hostname == 'lanboll.com'
assert host.global

// 0 file
// http://someonewhocares.org/hosts/zero/hosts

// 127 file
// http://someonewhocares.org/hosts/hosts


try {
  def hosts_file = download("http://someonewhocares.org/hosts/zero/hosts1")
} catch (java.io.FileNotFoundException e) {
   println "File ${e.message} not found!"
   throw new ProgramExitException(1)
}