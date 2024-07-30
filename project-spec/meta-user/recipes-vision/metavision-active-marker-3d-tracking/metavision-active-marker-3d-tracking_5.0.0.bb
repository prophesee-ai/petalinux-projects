SUMMARY = "Prophesee Metavision Active Marker 3D"
DESCRIPTION = "This sample shows how to implement a pipeline for detecting and \
tracking an active marker using Prophesee Metavision"
HOMEPAGE = "https://docs.prophesee.ai/stable/samples/modules/cv3d/active_marker_3d_tracking_cpp.html"

SECTION = "vision"
LICENSE = "CLOSED"

# The zip archive is to be copied in the file directory next to this recipe
# The archive itself is not public and shall be retrieved separately using customer access
#SRC_URI = "https://github.com/prophesee-ai/metavision_active_marker_3d_tracking/archive/refs/tags/openeb_v5.0.0.tar.gz"
SRC_URI = "file://metavision_active_marker_3d_tracking-openeb_v${PV}.tar.gz"
SRC_URI[sha256sum] = "3c62335e41da6fa5836bfb4cff3a83538bbf3ffe8554a352e406b0fa8378afae"

S = "${WORKDIR}/metavision_active_marker_3d_tracking-openeb_v${PV}"

# Dependencies
DEPENDS = "boost opencv metavision"

inherit pkgconfig cmake

# Options to pass to cmake: Metavision Studio is not included to avoid the need for nodejs
EXTRA_OECMAKE = "-DEMBEDDED_ONLY=ON -DCMAKE_BUILD_TYPE=Release"

FILES:${PN} += "/opt/metavision/embedded_active_marker_3d_tracking/*"
