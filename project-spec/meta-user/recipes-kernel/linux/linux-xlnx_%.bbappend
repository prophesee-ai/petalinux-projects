FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://bsp.cfg"
KERNEL_FEATURES:append = " bsp.cfg"

SRC_URI += "file://advanced-video-debug.cfg"
SRC_URI += "file://declare-Prophesee-V4L2-media-bus-types.patch"
SRC_URI += "file://don-t-use-IRQThreshold-on-Xilinx-AXIDMA.patch"
