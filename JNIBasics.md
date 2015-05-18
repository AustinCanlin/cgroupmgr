# Introduction #

While most Android applications are developed entirely in Java, developers often need to develop code in C or C++ in order to access system services that aren't exposed in the Google API or to create code that is more efficient that what can be done in the Dalvik JVM.  This page describes the basics of JNI programming using the Android Native Development Kit (NDK) [r4](https://code.google.com/p/cgroupmgr/source/detail?r=4).

# Details #

## NDK Setup ##

The Android NDK can be downloaded from this site: http://developer.android.com/sdk/ndk/index.html.  For this project, [revision 4](https://code.google.com/p/cgroupmgr/source/detail?r=4) of the Linux version was used.

The NDK is delivered as a ZIP file.  Once it is unzipped, the folder `<install_dir>`/android-ndk-[r4](https://code.google.com/p/cgroupmgr/source/detail?r=4)/tools should be added to the _path_ environment variable for convenient use of the tools.

## Building a Project ##

The script `<install_dir>`/android-ndk-[r4](https://code.google.com/p/cgroupmgr/source/detail?r=4)/tools/ndk-build is used to compile C or C++ code into a JNI shared object.  The file _Android.mk_ in the source directory is used as the makefile.  Several examples of basic JNI applications are located in the `<install_dir>`/android-ndk-[r4](https://code.google.com/p/cgroupmgr/source/detail?r=4)/samples directory.

## Integration with an Eclipse Project ##

If your Java code is being developed in Eclipse, it is relatively easy to get your JNI shared object integrated into your project.  Place the .c or .cpp files into a directory called jni under the Eclipse project's directory.  Refresh the Eclipse project by right-clicking on it in the tree view and selcting "Refresh".  The jni directory will be added to the project.

Once the jni code is added to the project and compiled using _ndk-build_, the shared object file can be added to the project's .apk file by selecting Project->Clean from Eclipse's menu bar.  The project can then be run on the Android emulator.

## Headers ##

The build script, _ndk-build_, automatically sets up the include and lib paths for your project.  The linux headers are located in the folder `<install_dir>`/android-ndk-[r4](https://code.google.com/p/cgroupmgr/source/detail?r=4)/platforms/armeabi/android-8/include.

A large portion of the linux system headers are included in the NDK.  The most critical header file for JNI objects is the file "jni.h".  This header contains all of the data structures and functions required to implement the Java/C bridge.