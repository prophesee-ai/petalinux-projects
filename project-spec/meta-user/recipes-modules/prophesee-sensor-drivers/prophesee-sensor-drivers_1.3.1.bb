SUMMARY = "Recipe to build Prophesee sensor drivers Linux kernel module"
SECTION = "PETALINUX/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a75371ba4d16749254a51215d13f97"

inherit module
SRC_URI = "git://git@github.com/prophesee-ai/linux-sensor-drivers;protocol=ssh;branch=kernel-5.15"
SRCREV = "7d61818531420b70d1b9d71d84801cd948c05d62"

S = "${WORKDIR}/git"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
