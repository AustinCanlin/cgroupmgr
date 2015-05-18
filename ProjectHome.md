CGroup support is built into the Android version of the linux kernel.  Currently the only way to access or manage cgroups is through a remote root shell on an unlocked phone.

This project aims to create a Android app with JNI library that allows users to view and manage cgroups on the phone.

NOTE:  In order to add groups using the CGroupMgr Android application, you must first change some permissions of system directories.  From a root shell (adb shell) issue the following commands:

  1. cd /
  1. chmod 777 dev
  1. cd dev
  1. chmod 777 cpuctl

Once this is done, the Android app will have the proper permissions to add children to existing cgroups.