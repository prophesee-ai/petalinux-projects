SUMMARY = "Recipe for  build an external imx636-driver Linux kernel module"
SECTION = "PETALINUX/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a75371ba4d16749254a51215d13f97"

inherit module

SRC_URI = "git://git@github.com/prophesee-ai/linux-sensor-drivers.git;protocol=ssh;branch=kernel-5.15"
SRCREV = "2868b1b2a38d24559aa1cb9fce77c1e1d50406f8"

S = "${WORKDIR}/git"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
