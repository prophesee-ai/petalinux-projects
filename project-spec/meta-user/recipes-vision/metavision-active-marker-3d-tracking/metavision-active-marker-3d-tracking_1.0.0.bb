SUMMARY = "Prophesee Metavision Active Marker 3D"
DESCRIPTION = "This sample shows how to implement a pipeline for detecting and \
tracking an active marker using Prophesee Metavision"
HOMEPAGE = "https://docs.prophesee.ai/stable/samples/modules/cv3d/active_marker_3d_tracking_cpp.html"

SECTION = "vision"
LICENSE = "CLOSED"

# The zip archive is to be copied in the file directory next to this recipe
# The archive itself is not public and shall be retrieved separately using customer access
# The hash is not checked by yocto fetcher for local sources, it is purely informational
SRC_URI = "file://metavision_active_marker_3d_tracking-main.zip;sha256sum=8166dfe22ae5efe4fb19a6290ed721df215948fd818b5d9e2c551276d29ff721"

S = "${WORKDIR}/metavision_active_marker_3d_tracking-main"

# Dependencies
DEPENDS = "boost opencv metavision"

inherit pkgconfig cmake

# Options to pass to cmake: Metavision Studio is not included to avoid the need for nodejs
EXTRA_OECMAKE = "-DEMBEDDED_ONLY=ON -DCMAKE_BUILD_TYPE=Release"

FILES:${PN} += "/opt/metavision/embedded_active_marker_3d_tracking/*"
