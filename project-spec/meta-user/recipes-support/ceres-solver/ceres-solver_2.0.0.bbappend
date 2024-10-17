# Appending nothing to the package configuration makes an issue disappear
# The reason is not investigated yet
PACKAGECONFIG += ""
# Description of the issue
# When generating a SDK (see Yocto Documentation,
# or Chapter 11, SDK Generation in Xilix UG1144)
# A CeresTargets.cmake file is generated, easing the compilation of software
# using CMake, such as Prophesee Metavision SDK and code samples.
# However, by default, the generated CeresTargets.cmake has the following code

#set_target_properties(Ceres::ceres PROPERTIES
#  INTERFACE_COMPILE_DEFINITIONS "CERES_EXPORT_INTERNAL_SYMBOLS"
#  INTERFACE_COMPILE_FEATURES "cxx_std_14"
#  INTERFACE_INCLUDE_DIRECTORIES "${_IMPORT_PREFIX}/include"
#  INTERFACE_LINK_LIBRARIES "Threads::Threads;glog::glog;gflags;[PETALINUX_BUILD_SYSROOT]/usr/lib/libspqr.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libcholmod.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libccolamd.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libcamd.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libcolamd.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libamd.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/liblapack.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libblas.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libblas.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libsuitesparseconfig.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/librt.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libmetis.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libcxsparse.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/liblapack.so;[PETALINUX_BUILD_SYSROOT]/usr/lib/libblas.so;Eigen3::Eigen"
#)

# The direct reference of the petalinux build path breaks the compilation of
# further software using the SDK
# With this appendix to the recipe the CeresTargets.cmake becomes

#set_target_properties(Ceres::ceres PROPERTIES
#  INTERFACE_COMPILE_DEFINITIONS "CERES_EXPORT_INTERNAL_SYMBOLS"
#  INTERFACE_COMPILE_FEATURES "cxx_std_14"
#  INTERFACE_INCLUDE_DIRECTORIES "${_IMPORT_PREFIX}/include"
#  INTERFACE_LINK_LIBRARIES "Threads::Threads;glog::glog;gflags;Eigen3::Eigen"
#)

# Which fixes the build issues using the SDK.
