DESCRIPTION = "C++ implementation of Lie Groups using Eigen."
SECTION = "devel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=400f4cadec03da3053fcbc6abcdf6730"

DEPENDS = "libeigen"

SRC_URI = "git://github.com/strasdat/Sophus.git;protocol=https;nobranch=1"
SRCREV = "d0b7315a0d90fc6143defa54596a3a95d9fa10ec"

S = "${WORKDIR}/git"

inherit cmake

# CXXFLAGS are needed to ignore issues in eigen
CXXFLAGS += "-Wno-error=maybe-uninitialized"
