# Introduction #

Because each Dalvik application runs as a separate non-root user on the system, cgroupmgr does not have permissions to write to the files under the cgroup procfs file system.  This page describes the kernel changes made so that the files were writeable by non-root users.


# Details #

In Android 2.1, cgroups is implemented in the following files located in the /kernel directory of the source tree:

  * cgroup.c
  * cgroup\_debug.c
  * cgroup\_freezer.c

The function _cgroup\_add\_file()_ in cgroup.c (located at around line 1692) is responsible for the creation of all files located under the /dev/cpuctl file structure.  By default, the file permissions for all files are -rw-r--r-- (0644).  This value is passed into the function _cgroup\_create\_file()_.  By changing this value to 0666 (-rw-rw-rw-), the files become writeable by all users on the system upon creation.

By making this change, any process running inside of Dalvik can manipulate the cgroups' configuration by simple reads and writes to the files.

## The tasks File ##

The file _tasks_ in the cgroup folder contains a list of process IDs (PIDs) that are controlled by the cgroup.  Values written to this file must be unsigned 64-bit integers (u64).  This can easily be done using an echo command in the root shell, but has proven difficult to do in code.  The reason is that Java does not support unsigned integer types.

Current attempts at workarounds include the following:
  * Using a system() command to run "echo PID > tasks".  This doesn't work because the "echo" command in Android is not implemented as a separate program.  Instead it is a buit-in command of the shell.  Therefore, you cannot specify a fully-qualified path to it.
  * Writing to the tasks file as a String or as an int64\_t type.  Both of these attempts have resulted in a different PID than specified being added to the tasks file for unknown reasons.

The next technique that will be attempted is to add and ioctl() command to cgroup.c that allows direct access to the cgroup data structures.  This will be used from the application's JNI library.