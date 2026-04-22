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

KERNEL_OUTPUT_DIR = "${STAGING_KERNEL_DIR}/arch/${TARGET_ARCH}/boot"
KERNEL_IMAGEDEST = "boot"
DTB_OUTPUT = "${KERNEL_OUTPUT_DIR}/dts/${KERNEL_DEVICETREE}"

# uniLoader is taking the place of the kernel here
KERNEL_IMAGE = "${B}/${UNILOADER_IMAGETYPE}"

addtask integrate_blobs after do_unpack

do_integrate_blobs() {
    cp ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${S}/blobs/Image
    cp ${KERNEL_OUTPUT_DIR}/dts/${KERNEL_DEVICETREE} ${S}/blobs/dtb
    cp ${DEPLOY_DIR_IMAGE}/initramfs-android-image-${MACHINE}.cpio.gz ${S}/blobs/ramdisk
}

do_configure() {
    # uniLoader uses "aarch64" as the 64-bit ARM identifier, we use "arm64"
    # Switch it around if we're on 64-bit ARM
    if [ "${TARGET_ARCH}" = "arm64" ]; then
        UNILOADER_ARCH="aarch64"
    else
        UNILOADER_ARCH="${TARGET_ARCH}"
    fi

    oe_runmake ${PARALLEL_MAKE} ARCH="${UNILOADER_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}" ${MACHINE}_defconfig
}

do_compile() {
    if [ "${TARGET_ARCH}" = "arm64" ]; then
        UNILOADER_ARCH="aarch64"
    else
        UNILOADER_ARCH="${TARGET_ARCH}"
    fi

    oe_runmake ${PARALLEL_MAKE} ARCH="${UNILOADER_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}"
}

inherit mkbootimg