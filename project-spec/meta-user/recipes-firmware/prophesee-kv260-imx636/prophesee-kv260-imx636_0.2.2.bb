SUMMARY = "Dynamic load of kria base design for use with imx636"
SECTION = "PETALINUX/apps"
LICENSE = "CLOSED"

inherit fpgamanager_dtg


SRC_URI = "file://pl-imx636.dtsi \
           file://load.sh \
           file://shell.json"

XSA_FILE = "kv260_v0_2_2.xsa"
# Get the XSA from the github artifacts
SRC_URI += "https://github.com/prophesee-ai/fpga-projects/releases/download/v0.2.2/${XSA_FILE}"
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
