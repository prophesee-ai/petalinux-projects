Integrate this design a project
===============================

This project demonstrates a system image build with streaming capabilities and
a display of streamed events. This section describes the parts integrated in the
image to help integrate part or all of it in an other project.

The base of the project is using `Petalinux 2022.2
<https://www.xilinx.com/support/download/index.html/content/xilinx/en/downloadNav/embedded-design-tools/archive.html>`_,
based on AMD BSP ``xilinx-kv260-starterkit-v2022.2-10141622.bsp``, and this
section only documents differences with the base BSP configuration.

Requirements to load the FPGA design
------------------------------------

FPGA manager
''''''''''''

The Prophesee video pipeline is built as a Kria firmware, following
`Kria SOM flow <https://xilinx.github.io/kria-apps-docs/creating_applications/2022.1/build/html/docs/bitstream_management.html>`_.
Loading it requires to enable the FPGA Manager (see
`UG1144 <https://docs.amd.com/r/2022.2-English/ug1144-petalinux-tools-reference-guide/FPGA-Manager-Configuration-and-Usage-for-Zynq-7000-Devices-and-Zynq-UltraScale-MPSoC>`_,
chapter 10).

Requirements to stream sensor data
----------------------------------

FPGA firmware
'''''''''''''

This projects uses the ``fpgamanager_dtg`` class to load the FPGA, as shown in
``project-spec/meta-user/recipes-firmware/prophesee-kv260-imx636/``.

The hardware design file (XSA file) is not saved in the petalinux project
recipes, and must be manually added in the ``files/`` folder.

Hardware description
''''''''''''''''''''

On top of the hardware design file (XSA file), it is necessary to describe the
hardware for Linux. In the recipe this is done by ``files/pl-imx636.dtsi``.

This `devicetree overlay <https://docs.kernel.org/devicetree/overlay-notes.html>`_
add the following information in the hardware description:

- The links between Prophesee FPGA IPs, that are not exported in the XSA with
  the current version of the IPs.

- The pca9546 I2C mux used on the KV260 board

- The CCAM5 camera module with an IMX646


Prophesee linux-sensor-drivers
''''''''''''''''''''''''''''''

This package contains the Prophesee sensor drivers for Linux, meant to be useful on several
platforms.

Prophesee zynq-video-drivers
''''''''''''''''''''''''''''

This package contains the Prophesee drivers for the FPGA IPs, for use on ZynqMP/Kria platforms.


packagegroup-petalinux-v4lutils
'''''''''''''''''''''''''''''''

The video pipeline is composed of several
`V4L2 subdevices <https://docs.kernel.org/userspace-api/media/v4l/v4l2.html>`_
communicating with a Media Controller.
In this framework, the configuration of the pipeline is the responsability of
the userland (it shall be managed by a program running outside the kernel).
Unless your program manages this itself, the standard tool to do it is
``media-ctl``, included in `packagegroup-petalinux-v4lutils`, and used in
the `load-prophesee-kv260-imx636.sh` script from the
`prophesee-kv260-imx636 <FPGA firmware>`_ firmware app.

As event-based sensors use data formats unknown to upstream tools, this
petalinux project patch them to add formats identifiers. In ``media-ctl``, a
utility from ``v4lutils``, there is a copy of the kernel headers, which must be
patched, as done by the patch located in
``project-spec/meta-user/recipes-multimedia/v4l2apps/files/0001-Add-prophesee-media-formats.patch``.

Linux kernel patch for the Xilinx AXIDMA IP driver
''''''''''''''''''''''''''''''''''''''''''''''''''

The Prophesee design uses the `Xilinx AXIDMA <https://docs.amd.com/r/en-US/pg021_axi_dma>`_,
which can move AXI4-Stream data to memory, and indicates the amount of data
received and stored in each buffer.

The mainline driver uses an other feature of this DMA, the Interrupt Threshold
Status, which waits several transfers before triggering a CPU interrupt. This
usually reduce the CPU load, but in Prophesee use case, it delays the data
processing on software side, and causes a gap as all buffers are passed at the
same time and the first buffer must be processed before resuming DMA operations,
which can take more time than the video pipeline internal memory can store.

To avoid this issue, the driver is patched directly in the kernel, with
``project-spec/meta-user/recipes-kernel/linux/linux-xlnx/don-t-use-IRQThreshold-on-Xilinx-AXIDMA.patch``,
which fixes the Interrupt Threshold to 1. This modification affects any
application using this kernel and a Xilinx AXIDMA driven by Linux.

Linux kernel patch to add Prophesee data formats (optional)
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

V4L2 pixel formats and media bus codes are added for Prophesee data formats.
This patch is not strictly necessary since other tools redefine those code if
not defined by the kernel, but this avoid warning messages when passing
otherwise unknown formats through the kernel APIs.

In the sample petalinux project, the patch is appended to the kernel recipe, and
resides in
``project-spec/meta-user/recipes-kernel/linux/linux-xlnx/declare-Prophesee-V4L2-media-bus-types.patch``.

Display sensor data using Metavision SDK
----------------------------------------

`Metavision SDK <https://docs.prophesee.ai/stable/index.html>`_
is a SDK to create applications leveraging event-based vision hardware
equipment. It includes some apps to display some rendering of the events.

packagegroup-petalinux-x11
''''''''''''''''''''''''''

This setup uses Xorg as renderer for Metavision, provided by
``packagegroup-petalinux-x11``. This is a default choice, other renderers may
work as well.

OpenEB
''''''

This project uses the open-source (Apache v2) subset of Metavision called
`OpenEB <https://www.prophesee.ai/event-based-vision-open-source/>`_.
It has no support for ``media-ctl``, but this petalinux project patches it to
allow to use the viewer.

The complete build recipe is located in
``project-spec/meta-user/recipes-multimedia/openeb/metavision_4.5.2.bb``.
The recipe uses OpenEB sources, but is called metavision, because the build of
OpenEB generates packages called metavision.

V4L2 DataTransfer
~~~~~~~~~~~~~~~~~

The major change in the OpenEB source is to allow HAL plugins (dynamically
linked libraries used by Metavision HAL to open a camera and access all of its
functionalities) to modify the memory allocator for the buffers used in
streaming.

This is then used to stream directly with the buffers using
`DMABUF <https://docs.kernel.org/driver-api/dma-buf.html>`_,
a Linux kernel way to describe physical memory buffers from applications.

With these patches, when a Metavision application is run with the ``V4L2_HEAP``
environment variable set to the name of a DMA heap, it uses this heap to
allocate the buffer memory. In this project exemples, the heap called
``reserved`` is used, it is a heap created by petalinux default configuration,
by the ``cma=900M`` in the kernel command line, where cma stands for
`contiguous memory allocator <https://lwn.net/Articles/396707/>`_.

If ``V4L2_HEAP`` is not set, the default V4L2 allocator will be used. This
results in poor decoding performances, and it is assumed that, by default, V4L2
allocates non-cachable memory, as this avoids cache maintenance considarations,
and a CPU is inefficient to handle the usual (frame-based) data of V4L2 devices.

Enable ``VIDEO_ADV_DEBUG`` in the kernel
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Prophesee plugin for Metavision currently relies on direct access to the
sensor registers to enable the sensor features, following the design of the USB
EVKs, but providing direct access to the hardware is not nominal in Linux
drivers. To allow this, the ``VIDEO_ADV_DEBUG`` config must be enabled in the
kernel, as shown in
``project-spec/meta-user/recipes-kernel/linux/linux-xlnx_%.bbappend``.

Even once enabled, the access to registers, as done using ``v4l2-dbg``, is
limited to ``root`` user, and Metavision is to be used as ``root`` with this
implementation.

Metavision also does register accesses before the streaming, while the driver
does not necessarily keep the sensor powered-up. This can be addressed at
runtime by explicitely requesting the sensor to be powered before running
Metavision. This can be done from the console using:

.. code:: none

	echo on > /sys/class/video4linux/v4l-subdev3/device/power/control

The sensor may be brought back to its normal behaviour using:

.. code:: none

	echo auto > /sys/class/video4linux/v4l-subdev3/device/power/control

It is assumed in both these commands that the sensor has beed probed as
``v4l-subdev3``.

``V4L2_SENSOR_PATH``
~~~~~~~~~~~~~~~~~~~~

The V4L2 code present in Metaivision expects the sensor to be
``/dev/v4l-subdev1``, and there is no media controller implementation to find
the actual sensor device. With the acquisition pipeline used in this project,
the sensor is usually probed as ``/dev/v4l-subdev3``, and an other patch allows
to make Metavision try to use the device designed by the ``V4L2_SENSOR_PATH``
environment variable.
