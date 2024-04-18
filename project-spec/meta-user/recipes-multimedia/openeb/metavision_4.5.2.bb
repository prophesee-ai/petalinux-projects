SUMMARY = "Prophesee Metavision"
DESCRIPTION = "Metavision SDK is an all-in-one package that provides you with a range of \
essential tools, from a visualization application to a comprehensive API."
HOMEPAGE = "https://www.prophesee.ai/metavision-intelligence"

SECTION = "multimedia"

# This recipe builds OpenEB, the part of Metavision that is distributed under Apache-2.0
# It is still called Metavision because OpenEB build produces extra packages such as
# metavision-hal-samples, and it was simpler that way to avoid installed-vs-shipped QA issues
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://licensing/LICENSE_OPEN;md5=c37080f4c2fe1ff35aee0ddacb4466b3"

S = "${WORKDIR}/git"

# Dependencies
DEPENDS = "boost libusb opencv glew glfw libeigen protobuf protobuf-native"

inherit cmake

EXTRA_OECMAKE = "-DCOMPILE_METAVISION_STUDIO=OFF \
                 -DCOMPILE_PLAYER=OFF \
                 -DUSE_OPENGL_ES2=ON \
                 -DUSE_PROTOBUF=ON \
                 -DCOMPILE_PYTHON3_BINDINGS=OFF \
                 -DBUILD_TESTING=OFF"

SRC_URI = "git://git@github.com/prophesee-ai/openeb.git;protocol=https;branch=main"
# Version 4.5.2 + fix 112 ("Support Protobuf CMake Config Mode")
SRCREV = "6934dd5200b816c99d3ec50efe01383dd9693350"

# This patch serie modifies Metavision HAL DataTransfer to use a BufferAllocator
# with virtual methods, that a plugin can override.
# This allows to run directly on V4L2 native buffers, without copy before processing
# however, it seems that the allocator is re-used by pybind, calling it way more
# often than the initial buffer allocation, decreasing the performance of python
# code samples.
# Here we don't even compile the bindings, so V4L2 transfer performances are more
# important
FILESEXTRAPATHS:append := ":${THISDIR}/v4l2"

SRC_URI += "file://0001-MV-1764-Debug-0-init-v4l2_dbg_register-structs.patch"
SRC_URI += "file://0002-MV-1764-Clean-up-V4L2-board-command-headers.patch"
SRC_URI += "file://0003-MV-1764-Create-a-BufferAllocator-that-may-have-a-sta.patch"
SRC_URI += "file://0004-MV-1764-Use-the-BufferAllocator-in-DataTransfer.patch"
SRC_URI += "file://0005-MV-1764-Make-V4L2-DataTransfer-working-in-place.patch"
SRC_URI += "file://0006-MV-1764-Remove-obsolete-user_ptr-implementation.patch"
SRC_URI += "file://0007-MV-1764-hotfix-streaming-format-when-available.patch"
SRC_URI += "file://0008-MV-1764-Add-a-DMABUF-Heap-Allocator.patch"
SRC_URI += "file://0009-MV-1764-Implement-explicit-cache-operations-when-nee.patch"
SRC_URI += "file://0010-MV-1764-Use-a-DMABUF-allocator-when-V4L2_HEAP-is-set.patch"
SRC_URI += "file://0011-MV-1764-Add-non-regression-test-on-DataTransfer.patch"
SRC_URI += "file://0012-MV-1764-Split-up-Allocator-interface-to-ease-re-usag.patch"
SRC_URI += "file://0013-Hack-to-sensor-sensor-path-via-V4L2_SENSOR_PATH-envi.patch"
