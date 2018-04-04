# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(distribution_DIR ${CMAKE_SOURCE_DIR})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

ADD_SUBDIRECTORY(sdl2)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             ffplay

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${distribution_DIR}/src/native-lib.cpp
             ${distribution_DIR}/src/cmdutils.c
             ${distribution_DIR}/src/ffplay.c)
target_include_directories( ffplay
                            PRIVATE
                            ${distribution_DIR}/ffmpeg/include
                            ${distribution_DIR}/sdl2/include)

add_library(ffmpeg STATIC IMPORTED)
set_target_properties(  ffmpeg
                        PROPERTIES
                        IMPORTED_LOCATION
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libavcodec.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libavdevice.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libavfilter.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libavformat.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libavutil.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libswresample.a
                        ${distribution_DIR}/ffmpeg/lib/${ANDROID_ABI}/libswscale.a)
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       ffplay
                       sdl2
                       ffmpeg
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )