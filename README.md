# Prophesee KV260 Petalinux project

This branch of the repository holds a Petalinux (2022.2) project to run the Prophesee RTL design on
a Kria KV260 board.

The goal of this project is to provide the minimum environment to run a viewer displaying the
output of a Prophesee sensor plugged to a Kria KV260 board.

## Quickstart

### Prerequisite

To build this project, you need [Petalinux 2022.2](https://docs.amd.com/r/2022.2-English/ug1144-petalinux-tools-reference-guide/Overview).

### Build the microSD image

Clone the current repository and build the project:

	git clone git@github.com:prophesee-ai/petalinux-projects.git -b kv260-2022.2
	cd petalinux-projects

Then run Petalinux tools to build the system and generate a microSD card image.

	source <path-to-installed-PetaLinux>/settings.sh
	petalinux-build
	petalinux-package --wic --bootfiles "ramdisk.cpio.gz.u-boot boot.scr Image system.dtb"

### Write the microSD card

Write the image the microSD card (the name of the device representing your SD card may vary):

	sudo dd bs=4M if=images/linux/petalinux-sdimage.wic of=/dev/mmcblk0 status=progress && sync

Programming the microSD card may take up to 10mn, depending on the microSD card and your reader.

### Run the viewer

Plug the microSD card and power-up the Kria board. Make sure you have a micro-USB cable to connect
the Kria to your computer and possibly a screen connected to the hdmi output.

The linux serial console is on the second serial-over-USB advertised by the board.
Serial baudrate is 115200, 8 bits data, no parity, 1 stop bit, without hardware flow control.

	minicom -D /dev/ttyUSB1

On the Kria, login is root and password is root, prepare the FPGA design as follows:

	# load and set the pipeline with the default script
	load-prophesee-kv260-imx636.sh

Then you can start a viewer either on a screen plugged to the board, or on your computer,
streaming the display over ethernet. In both case, Metavision requires some information regarding
the board, that is exported as environment variables.

#### Viewer on HDMI

To run the viewer on a HDMI screen plugged to the Kria board, start a X server on the board and run
Metavision using it as display.

	Xorg&
	export V4L2_HEAP=reserved
	export V4L2_SENSOR_PATH=/dev/v4l-subdev3
	# Force the sensor to be always on, to allow register accesses from Metavision
	echo on > /sys/class/video4linux/v4l-subdev3/device/power/control
	DISPLAY=:0.0 metavision_viewer

#### Viewer over SSH

To use your own computer as X server, over SSH, identify the IP address of the Kria over ethernet:

	root@xilinx-kv260-starterkit-20222:~# ip addr
	1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
	    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
	    inet 127.0.0.1/8 scope host lo
	       valid_lft forever preferred_lft forever
	    inet6 ::1/128 scope host 
	       valid_lft forever preferred_lft forever
	2: sit0@NONE: <NOARP> mtu 1480 qdisc noop state DOWN group default qlen 1000
	    link/sit 0.0.0.0 brd 0.0.0.0
	3: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
	    link/ether 00:0a:35:15:82:e2 brd ff:ff:ff:ff:ff:ff
	    inet 10.14.75.154/16 metric 10 brd 10.14.255.255 scope global dynamic eth0
	       valid_lft 78278sec preferred_lft 78278sec
	    inet6 fe80::20a:35ff:fe15:82e2/64 scope link 
	       valid_lft forever preferred_lft forever

here it is 10.14.75.154. Just SSH on the board from your computer and run Metavision:

	ssh -X root@10.14.75.154
	# Log as root and then run the following commands on the board
	export V4L2_HEAP=reserved
	export V4L2_SENSOR_PATH=/dev/v4l-subdev3
	# Force the sensor to be always on, to allow register accesses from Metavision
	echo on > /sys/class/video4linux/v4l-subdev3/device/power/control
	metavision_viewer

