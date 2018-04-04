cmake_minimum_required(VERSION 3.4.1)
file(GLOB c_srcs
                 src/*.c
                 src/audio/*.c
                 src/audio/android/*.c
                 src/audio/dummy/*.c
                 src/atomic/SDL_atomic.c
                 src/atomic/SDL_spinlock.c.arm
                 src/core/android/*.c
                 src/cpuinfo/*.c
                 src/dynapi/*.c
                 src/events/*.c
                 src/file/*.c
                 src/haptic/*.c
                 src/haptic/dummy/*.c
                 src/joystick/*.c
                 src/joystick/android/*.c
                 src/loadso/dlopen/*.c
                 src/power/*.c
                 src/power/android/*.c
                 src/filesystem/android/*.c
                 src/render/*.c
                 src/render/*/*.c
                 src/stdlib/*.c
                 src/thread/*.c
                 src/thread/pthread/*.c
                 src/timer/*.c
                 src/timer/unix/*.c
                 src/video/*.c
                 src/video/android/*.c
                 src/test/*.c)
set(sdl2_srcs ${c_srcs})
add_library( # Sets the name of the library.
             sdl2

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${sdl2_srcs})
target_include_directories( sdl2
                            PRIVATE
                            ${distribution_DIR}/sdl2/include)

#find_path(GLES_INCLUDE_DIR GLES/gl.h HINTS ${ANDROID_NDK_HOME}/sysroot/usr/include)
#find_library( GLESv1_CM-lib llibGLESv1_CM.so HINTS ${ANDROID_NDK_HOME}/platforms/android-21/arch-arm/usr/lib)
#find_path(GLES2_INCLUDE_DIR GLES2/gl2.h HINTS ${ANDROID_NDK_HOME}/sysroot/usr/include)
#find_library( GLES2-lib libGLESv2.so HINTS  ${ANDROID_NDK_HOME}/platforms/android-21/arch-arm/usr/lib)
#include_directories(${GLES_INCLUDE_DIR} ${GLES2_INCLUDE_DIR})

target_link_libraries( # Specifies the target library.
                       sdl2

                       # Links the target library to the log library
                       # included in the NDK.
                       log
                       GLESv1_CM
                       GLESv2)