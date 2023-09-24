use std::{error::Error, fmt::Display};

#[derive(Debug)]
pub struct InvalidGLFormatError {
    pub format: i32,
}

impl Display for InvalidGLFormatError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "Unknown/Unsupported OpenGL format: {}", self.format)
    }
}

impl Error for InvalidGLFormatError {
    fn source(&self) -> Option<&(dyn Error + 'static)> {
        Some(self)
    }

    fn cause(&self) -> Option<&dyn Error> {
        self.source()
    }
}
