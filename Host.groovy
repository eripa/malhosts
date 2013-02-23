#!/usr/bin/env groovy
/**
* Host.groovy
* 
* Class for representing each host in a hosts file
*/

import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
class Host {
  String ip
  String hostname
  boolean global = false
}