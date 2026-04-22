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
PACKAGE_ARCH = "${MACHINE_ARCH}"

KERNEL_IMAGEDEST = "boot"
DTB_OUTPUT = "${@d.getVar('DEPLOY_DIR_IMAGE') + '/' + os.path.basename(d.getVar('KERNEL_DEVICETREE'))}"

# uniLoader is taking the place of the kernel here
KERNEL_IMAGE = "${B}/${UNILOADER_IMAGETYPE}"

addtask do_integrate_blobs before do_configure
do_integrate_blobs[depends] = "initramfs-android-image:do_image_complete virtual/kernel:do_deploy"

do_integrate_blobs() {
    echo "KERNEL_OUTPUT_DIR: ${KERNEL_OUTPUT_DIR}"
    echo "RECIPE_SYSROOT: ${RECIPE_SYSROOT}"
    echo "DEPLOY_DIR_IMAGE: ${DEPLOY_DIR_IMAGE}"
    echo "DTB_OUTPUT: ${DTB_OUTPUT}"
    cp -v ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ${S}/blob/Image
    cp -v ${DTB_OUTPUT} ${S}/blob/dtb
    cp -v ${DEPLOY_DIR_IMAGE}/initramfs-android-image-${MACHINE}.cpio.gz ${S}/blob/ramdisk
}

do_configure() {
    oe_runmake ${PARALLEL_MAKE} ${MACHINE}_defconfig
}

do_compile() {
    # The Makefile doesn't seem to be able to find libgcc on its own in this environment,
    # so we tell it where it is with LIBGCC.
    # We also clear LDFLAGS due to them containing GCC args but being passed to LD.
    oe_runmake ${PARALLEL_MAKE} ARCH="${TARGET_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}"
    LDFLAGS="" LIBGCC="/asteroid/build/tmp/work/aarch64-oe-linux/uniloader/+git/recipe-sysroot/usr/lib/aarch64-oe-linux/14.3.0/libgcc.a"
}

inherit mkbootimg