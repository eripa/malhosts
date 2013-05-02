# malhosts

## What is this?

malhosts is a simple script written in Groovy that can be used to install, update and remove _hosts file_ entries from http://someonewhocares.org more easily. And since it's effectively Java, it's quickly installed and activated on computers that you want to fiddle with as little as possible such as parents, siblings, grandparents, friends, friends of friends and your friends friends dog etc.

## How to use?

It comes with pre-written wrappers that works on Linux (ubuntu 12.10 tested), Mac OS X (10.8.2 tested) and Windows (XP, 7, 8 tested).

Simply run the wrapper scripts to fetch the hosts file and apply it. Your present entries **will be kept**.

Please note that you should **not** edit the entries that are added between the MALHOSTS marked in the hosts file. Neither should you edit the marker itself.

The markers looks like this:

    ############ MALHOSTS BEGIN MARK, DO NOT REMOVE ############
    <entries>
    ############ MALHOSTS END MARK, DO NOT REMOVE ############

### Usage details

    usage: malhosts.groovy [--help|-h] [--legacy|-l] [--dry-run|-d]
                           [--quiet|-q]
     -c,--clean     Remove all entries that were added by malhosts
     -d,--dry-run   Don't do anything, only display what will happen
     -h,--help      Show usage information
     -l,--legacy    Use legacy format (127.0.0.1 instead of faster 0.0.0.0)
     -q,--quiet     Don't print anything, useful for automatic runs

## Credits

The hosts file is brought to you by Dan Pollock and can be found at [someonewhocares.org](http://someonewhocares.org/hosts/) You are free to copy and distribute this file, as long the original URL is included. See below for acknowledgements. Please forward any additions, corrections or comments by email tohosts@someonewhocares.org

Use this file to prevent your computer from connecting to selected internet hosts. This is an easy and effective way to protect you from many types of spyware, reduces bandwidth use, blocks certain pop-up traps, prevents user tracking by way of "web bugs" embedded in spam, provides partial protection to IE from certain web-based exploits and blocks most advertising you would otherwise be subjected to on the internet. 

There is a version of this file that uses 0.0.0.0 instead of 127.0.0.1 available at [http://someonewhocares.org/hosts/zero/](http://someonewhocares.org/hosts/zero/). On some machines this may run minutely faster, however the zero version may not be compatible with all systems.

## Making changes

There is a compiled Jar in the repository but feel free to make changes.. if you do and you need to recompile the jar you will need to have [Groovy](http://groovy.codehaus.org) installed. The use the supplied GroovyWrapper script ([source](http://groovy.codehaus.org/WrappingGroovyScript)) to compile the package. For some reason that I haven't tried to find the reason to you will also have to manually update the jar with the HostFile class:

    groovy GroovyWrapper -c -m  malhosts
    zip -u malhosts.jar HostFile.class
    # clean class files (these have been added to the jar file already)
    rm -f *.class

## License

This script is delivered "as is" and is [unlicensed](http://unlicense.org).

    This is free and unencumbered software released into the public domain.

    Anyone is free to copy, modify, publish, use, compile, sell, or
    distribute this software, either in source code form or as a compiled
    binary, for any purpose, commercial or non-commercial, and by any
    means.

    In jurisdictions that recognize copyright laws, the author or authors
    of this software dedicate any and all copyright interest in the
    software to the public domain. We make this dedication for the benefit
    of the public at large and to the detriment of our heirs and
    successors. We intend this dedication to be an overt act of
    relinquishment in perpetuity of all present and future rights to this
    software under copyright law.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
    OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
    ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.

    For more information, please refer to <http://unlicense.org/>