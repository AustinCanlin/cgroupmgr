# Introduction #

Because this is a research project and not a polished end-user application, getting it up and running requires several steps.  This page provides instructions on how to get started.


# Details #

## Building the App ##

This application contains both Java code and a JNI shared object written in C.  The following development tools are required for building the project:

  1. Eclipse Ganymede - a free Java IDE that includes Java 6.
  1. The Android SDK
  1. The [ADT Plugin](http://developer.android.com/sdk/installing.html#InstallingADT) for Ecllipse
  1. The [Android Native Development Kit (NDK)](http://developer.android.com/sdk/ndk/index.html)
  1. The GNU compiler collection, which comes with most Linux distributions
  1. Subversion, a source control program that comes with most Linux distributions

The instructions for installing and configuring these items are easily available via man pages and from the Android web site.

The source code for the cgroupmgr project can be downloaded from this site using the instructions provided [here.](http://code.google.com/p/cgroupmgr/source/checkout)

In order to build the JNI shared object, simple execute the command _`<ndk_path>`/ndk-build_ from the `<cgroupmgr_src>`/jni directory.  This will compile the shared object and put it into the proper location for inclusion into the install package.

Run Eclipse and then execute the following steps to build the rest of the application and install package.

  1. Go to **File->New->Other...**
  1. Select **Android Project** and click Next
  1. Type "cgroupmgr" in the **Project Name** edit box.
  1. Select the **Create project from existing source** radio button and specify the root of the cgroupmgr source directory as the path.
  1. Click on **Finish** at the bottom of the dialog box

## Running the App ##
Once the project has finished auto-building, run the Android Emulator.  Once an instance of the emulator has booted you can run the **cgroupmgr** application by selecting **Run->Run As->Android Application** from the Eclipse menu