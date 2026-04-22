DESCRIPTION = "A simple second-stage bootloader which provides a clean environment for booting Linux"
HOMEPAGE = "https://github.com/ivoszbg/uniLoader.git"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ba925be40dd5b952d53f0414e95a491a"

SRC_URI = "git://github.com/ivoszbg/uniLoader.git;branch=master;protocol=https"
SRCREV = "1bd1f6c06ac8d54b4ed40e3927381e83d86bce98"
PR = "r0"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"
DEPENDS = "initramfs-android-image virtual/kernel"
PACKAGE_ARCH = "${TARGET_ARCH}"

# TARGET_ARCH on 64-bit ARM is 'aarch64', but the kernel source directory is 'arm64'
KERNEL_OUTPUT_DIR = "${@d.getVar('STAGING_KERNEL_DIR') + '/arch/' + 'arm64' if d.getVar('TARGET_ARCH') == 'aarch64' else d.getVar('TARGET_ARCH') + '/boot'}"
KERNEL_IMAGEDEST = "boot"
DTB_OUTPUT = "${KERNEL_OUTPUT_DIR}/dts/${KERNEL_DEVICETREE}"

# uniLoader is taking the place of the kernel here
KERNEL_IMAGE = "${B}/${UNILOADER_IMAGETYPE}"

addtask integrate_blobs after do_unpack

do_integrate_blobs() {
    cp ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${S}/blob/Image
    cp ${KERNEL_OUTPUT_DIR}/dts/${KERNEL_DEVICETREE} ${S}/blob/dtb
    cp ${DEPLOY_DIR_IMAGE}/initramfs-android-image-${MACHINE}.cpio.gz ${S}/blob/ramdisk
}

do_configure() {
    oe_runmake ${PARALLEL_MAKE} ARCH="${TARGET_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}" ${MACHINE}_defconfig
}

do_compile() {
    oe_runmake ${PARALLEL_MAKE} ARCH="${TARGET_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}"
}

inherit mkbootimg