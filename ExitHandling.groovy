
package ExitHandling
/*
* We can't just ignore a call to System.exit() since the script would continue to run.
* Instead we want to throw an exception with the desired status code. We throw a (custom)
* ProgramExitException when System.exit() is called in our tests
*/
class ProgramExitException extends RuntimeException {

    int statusCode

    public ProgramExitException(int statusCode) {
        super("Exited with " + statusCode)
        this.statusCode = statusCode
    }
}
/**
 * Make System.exit throw ProgramExitException to fake exiting the VM
 */
System.metaClass.static.invokeMethod = { String name, args ->
    if (name == 'exit')
        throw new ProgramExitException(args[0])
    def validMethod =  System.metaClass.getStaticMetaMethod(name, args)
    if (validMethod != null) {
        validMethod.invoke(delegate, args)
    }
    else {
        return  System.metaClass.invokeMissingMethod(delegate, name, args)
    }
}
