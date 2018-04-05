LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := mp3lame
LOCAL_SRC_FILES := ../libmp3lame/lib/$(TARGET_ARCH_ABI)/libmp3lame.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:= avcodec
LOCAL_SRC_FILES:= ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:= avdevice
LOCAL_SRC_FILES:= ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libavdevice.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:= avfilter
LOCAL_SRC_FILES:= ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:= avformat
LOCAL_SRC_FILES:= ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE :=  avutil
LOCAL_SRC_FILES := ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := ../ffmpeg/lib/$(TARGET_ARCH_ABI)/libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := ffplay

SDL_PATH := ../sdl2

#LOCAL_ALLOW_UNDEFINED_SYMBOLS := true

LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(SDL_PATH)/include
LOCAL_C_INCLUDES += -L${SYSROOT}/usr/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../ffmpeg/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../libmp3lame/include

# Add your application source files here...
LOCAL_SRC_FILES := $(SDL_PATH)/src/main/android/SDL_android_main.c \
	cmdutils.c \
	ffplay.c \
	native-lib.cpp

LOCAL_SHARED_LIBRARIES := SDL2

LOCAL_LDLIBS := -lGLESv1_CM -lGLESv2 -llog -lm -lz

LOCAL_STATIC_LIBRARIES := avdevice \
                          avformat \
                          avfilter \
                          avcodec \
                          avutil \
                          swresample \
                          swscale \
                          mp3lame

include $(BUILD_SHARED_LIBRARY)