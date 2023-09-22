FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://bsp.cfg"
KERNEL_FEATURES:append = " bsp.cfg"

SRC_URI += "file://declare-Prophesee-V4L2-media-bus-types.patch"
