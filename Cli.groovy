import java.text.*

def cli = new CliBuilder(usage: 'showdate.groovy -[chflms] [date] [prefix]')

cli.with {
    h longOpt: 'help', 'Show usage information'
    f longOpt: 'foo', 'Do bar'
}

def options = cli.parse(['-f'])

if (! options) {
    println 'oops?'
}

if (options.h) {
    println 'got it!'
} else if  (options.f) {
    println 'bar!'
} else {
    println 'nothing to see here, move along!'
}

//cli.usage