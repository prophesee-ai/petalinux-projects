FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://null-pointer-dereference.patch"
