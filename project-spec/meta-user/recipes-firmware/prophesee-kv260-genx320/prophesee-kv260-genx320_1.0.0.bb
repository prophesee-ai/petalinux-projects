SUMMARY = "Dynamic load of kria base design for use with genx320"
SECTION = "PETALINUX/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit fpgamanager_dtg


SRC_URI = "file://pl-genx320.dtsi \
           file://load.sh \
           file://shell.json"

XSA_FILE = "kv260_v1_0_0.xsa"
# Get the XSA from the github artifacts
SRC_URI += "https://github.com/prophesee-ai/fpga-projects/releases/download/v1.0.0/${XSA_FILE}"
SRC_URI[sha256sum] = "3374ea5208ae366037d5dc289778b57dcb067affb1163db75f74ea615ef8cad2"
# Get the XSA from the "files" folder next to this recipe
#SRC_URI += "file://${XSA_FILE}"

RDEPENDS:${PN} += "bash"

# override the fpgamanager_dtg variable that expects local XSA URI
python (){
    d.setVar("XSCTH_HDF_PATH", d.getVar('XSA_FILE'))
}

do_install:append () {
    install -d ${D}/${bindir}
    install -m 0700 ${WORKDIR}/load.sh ${D}/${bindir}/load-${PN}.sh
}
FILES:${PN} += "${bindir}/load-${PN}.sh"
