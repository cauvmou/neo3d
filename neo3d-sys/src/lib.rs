use glfw::ffi::{glfwGetWindowSize, glfwGetX11Display, glfwGetX11Window, GLFWwindow};
use jni::{
    objects::JClass,
    sys::{jbyteArray, jint, jlong},
    JNIEnv,
};
use raw_window_handle::{
    HasRawDisplayHandle, HasRawWindowHandle, RawDisplayHandle, RawWindowHandle, XlibDisplayHandle,
    XlibWindowHandle,
};
use std::{
    ffi::c_ulong,
    sync::{Arc, Mutex},
};

mod error;
mod system;
mod texture;
mod window;
