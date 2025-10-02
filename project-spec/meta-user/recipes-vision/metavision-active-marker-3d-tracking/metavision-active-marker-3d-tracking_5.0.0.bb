SUMMARY = "Prophesee Metavision Active Marker 3D"
DESCRIPTION = "This sample shows how to implement a pipeline for detecting and \
tracking an active marker using Prophesee Metavision"
HOMEPAGE = "https://docs.prophesee.ai/stable/samples/modules/cv3d/active_marker_3d_tracking_cpp.html"

SECTION = "vision"
LICENSE = "CLOSED"

# The zip archive is to be copied in the file directory next to this recipe
# The archive itself is not public and shall be retrieved separately using customer access
#SRC_URI = "https://github.com/prophesee-ai/metavision_active_marker_3d_tracking/archive/refs/tags/openeb_v5.0.0+244228f.tar.gz"

SRC_NAME = "metavision_active_marker_3d_tracking-openeb_v${PV}-244228f"

SRC_URI = "file://${SRC_NAME}.tar.gz"
SRC_URI[sha256sum] = "20fa76ae09894d1737f603188e62277322ec3463d22dbd31b9da1b33d9e98791"
PR = "r1"

S = "${WORKDIR}/${SRC_NAME}"

# Dependencies
DEPENDS = "boost opencv metavision"

inherit pkgconfig cmake

# Options to pass to cmake: Metavision Studio is not included to avoid the need for nodejs
EXTRA_OECMAKE = "-DEMBEDDED_ONLY=ON -DCMAKE_BUILD_TYPE=Release"

FILES:${PN} += "/opt/metavision/embedded_active_marker_3d_tracking/*"
