#!/usr/bin/env groovy
/**
* HostFile.groovy
* 
* Class for representing a hosts file
*/

import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
class HostFile {
  List personal_part = []
  List someonewhocares_part = []
}