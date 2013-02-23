#!/usr/bin/env groovy
/**
* HostFile.groovy
* 
* Class for representing a hosts file
*/

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