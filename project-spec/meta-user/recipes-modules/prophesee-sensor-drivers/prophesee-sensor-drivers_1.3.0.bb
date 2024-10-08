SUMMARY = "Recipe to build Prophesee sensor drivers Linux kernel module"
SECTION = "PETALINUX/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a75371ba4d16749254a51215d13f97"

inherit module

SRC_URI = "git://github.com/prophesee-ai/linux-sensor-drivers;protocol=https;branch=kernel-5.15"
SRCREV = "7d38ced95346cf95df1ac649f05ab80ba43012c8"

S = "${WORKDIR}/git"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
