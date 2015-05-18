# Introduction #

Adding processes to a cgroup should be straightforward and simple, especially since I modified the groups kernel module to allow writing to the file by anyone.  However, it has proven very difficult for reasons I cannot explain.  This section outlines the techniques I have attempted while trying to add processes to a cgroup.


# Details #

Adding process IDs to the _tasks_ file via the command line is simple.  The command "echo PID > tasks" does the job.  However, I have not been able to replicate this success via my Java application using direct Java APIs or my JNI shared object.  Below are brief descriptions of my attempts to write to this file.

  * Using a system() command to run "echo PID > tasks".  This doesn't work because the "echo" command in Android is not implemented as a separate program.  Instead it is a buit-in command of the shell.  Therefore, you cannot specify a fully-qualified path to it.

  * Wrote my own echo program called _myecho_ and placed it in the /data folder.  This command works from the command-line exactly like the built-in echo command.  I can call it from my JNI shared object and have it echo text to files (such as my log file) with no issues.  However, the command "/data/myecho PID > /dev/cpuctl/_cgroup_/tasks" fails.  I get no errors, but no process is added to the cgroup.

  * Writing to the tasks file as a String or as an int64\_t type via the JNI shared object.  Both of these attempts have resulted in a different PID than specified being added to the tasks file for unknown reasons.  Normally, the value somehow gets converted to 0, which adds the calling process to the group.

  * Writing to the tasks file as a String or Long directly from Java.  This yields identical results as trying to do this from the JNI shared object

  * Modifying the cgroup kernel code to accept signed int64 values.  The new code was never called.

  * Modified the cgroup kernel code by adding a function to the _file\_operations_ structure that accepts string values and converts them to a u64 value.  I could not get this code to execute.  There is some pre-processing of values being done by the **procfs** module that is out of the control of the cgroup code.

  * Attempting to write to the file using the various techniques above while, at the same time, trying various signed-to-usigned, memcpy(), and casting strategies.  None succeeded.

This doesn't seem to be a pathing issue because I can get myecho to run from the JNI object and write to a text file under the /data directory.  It doesn't seem to be a permissions issue because I can write a value to _tasks_.  I just don't get the value I want to be written there.  There is some mechanism at work that I do not understand at this point.

I will revisit this feature if time permits.