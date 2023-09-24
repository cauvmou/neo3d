use std::ffi::c_ulong;

use glfw::ffi::{glfwGetWindowSize, glfwGetX11Display, glfwGetX11Window, GLFWwindow};
use raw_window_handle::{
    HasRawDisplayHandle, HasRawWindowHandle, RawDisplayHandle, RawWindowHandle, XlibDisplayHandle,
    XlibWindowHandle,
};

pub struct NeoWindow {
    ptr: *mut GLFWwindow,
}

impl NeoWindow {
    #[inline]
    pub fn new(glfw_handle: u64) -> Self {
        let ptr: *const u64 = &glfw_handle;
        let ptr: *mut GLFWwindow = ptr as *mut GLFWwindow;
        Self { ptr }
    }

    pub fn size(&self) -> (u32, u32) {
        let mut width: i32 = 0;
        let mut height: i32 = 0;
        unsafe { glfwGetWindowSize(self.ptr, &mut width as *mut i32, &mut height as *mut i32) };
        (width as u32, height as u32)
    }
}

#[cfg(all(
    any(target_os = "linux", target_os = "freebsd"),
    not(feature = "wayland")
))]
unsafe impl HasRawWindowHandle for NeoWindow {
    fn raw_window_handle(&self) -> RawWindowHandle {
        let x11_window = unsafe { glfwGetX11Window(self.ptr) as c_ulong };
        let mut x = XlibWindowHandle::empty();
        x.window = x11_window;
        RawWindowHandle::Xlib(x)
    }
}

#[cfg(all(
    any(target_os = "linux", target_os = "freebsd"),
    not(feature = "wayland")
))]
unsafe impl HasRawDisplayHandle for NeoWindow {
    fn raw_display_handle(&self) -> RawDisplayHandle {
        let x11_display = unsafe { glfwGetX11Display() };
        let mut x = XlibDisplayHandle::empty();
        x.display = x11_display;
        RawDisplayHandle::Xlib(x)
    }
}
