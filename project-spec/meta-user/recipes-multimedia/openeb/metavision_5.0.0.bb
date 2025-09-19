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
DEPENDS = "boost libusb opencv glew glfw libeigen protobuf protobuf-native sophus ceres-solver"

# For non-investigated reasons, depending on opencv install only some of the libs built by opencv
# However, the other libs (such as libopencv_calib3d) are a dependency of the opencv package, and
# adding it also as a runtime dependency forces the libopencv_stuff to go in the image
RDEPENDS:${PN}= "opencv"

inherit pkgconfig cmake

EXTRA_OECMAKE = "-DCOMPILE_METAVISION_STUDIO=OFF \
                 -DCOMPILE_PLAYER=OFF \
                 -DUSE_OPENGL_ES2=ON \
                 -DUSE_PROTOBUF=ON \
                 -DUSE_SOPHUS=ON \
                 -DCOMPILE_PYTHON3_BINDINGS=OFF \
                 -DHDF5_DISABLED=ON \
                 -DBUILD_TESTING=OFF"

SRC_URI = "git://git@github.com/prophesee-ai/openeb.git;protocol=https;nobranch=1 \
           file://0001-V4L2-Enumerate-available-sensor-controls.patch \
           file://0002-V4L2-Enumerate-available-media-entities.patch \
           file://0003-V4L2-Introducing-ERC-BIAS-and-ROI-V4L2-facilities.patch \
           file://0004-V4L2-Update-the-V4L2-device-builder-to-instanciate-v.patch \
           file://0005-V4L2-Add-crop-support.patch \
           file://0006-V4L2-Introducing-V4L2Crop-facility.patch \
           file://0007-V4L2-add-v4l2-device-geometry-helpers.patch \
           file://0008-V4L2-ROI-interface-should-not-be-genx320-specific.patch \
           file://0009-V4L2-Avoid-breaking-imx636-relative-bias-behaviour.patch \
           file://0010-V4L2-Updates-V4L2-Biases-facility.patch \
           file://0011-V4L2-Fix-controls-retrieving-out-of-sync-values.patch \
           file://0001-Disable-compilation-of-OpenEB-untested-apps.patch \
           "

# Add ongoing work
FILESEXTRAPATHS:append := ":${THISDIR}/v4l2"


SRCREV = "0391df5ab3bea6d0aa2595a73ccee66a1d9c1093"
PR = "r1"
