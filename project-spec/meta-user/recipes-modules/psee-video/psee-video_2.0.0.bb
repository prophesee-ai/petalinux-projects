SUMMARY = "Recipe to build an external psee-video Linux kernel module"
SECTION = "PETALINUX/modules"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

SRC_URI = "git://github.com/prophesee-ai/zynq-video-drivers;protocol=https;branch=kernel-5.15"
SRCREV = "22c8103d047cc7937960fd655d0c6869f745d76b"

SRC_URI += "file://avoid-descriptor-link-corruption.patch"

S = "${WORKDIR}/git"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
inherit module
