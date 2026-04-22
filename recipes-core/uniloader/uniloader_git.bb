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

# uniLoader is taking the place of the kernel here
KERNEL_IMAGE = "${B}/${UNILOADER_IMAGETYPE}"
KERNEL_IMAGEDEST = "boot"
DTB_OUTPUT = "${@d.getVar('DEPLOY_DIR_IMAGE') + '/' + os.path.basename(d.getVar('KERNEL_DEVICETREE'))}"

addtask do_integrate_blobs before do_configure
do_integrate_blobs[depends] = "initramfs-android-image:do_image_complete virtual/kernel:do_deploy"

do_integrate_blobs() {
    cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ${S}/blob/Image
    cp ${DTB_OUTPUT} ${S}/blob/dtb
    cp ${DEPLOY_DIR_IMAGE}/initramfs-android-image-${MACHINE}.cpio.gz ${S}/blob/ramdisk
}

do_configure() {
    oe_runmake ${PARALLEL_MAKE} ${MACHINE}_defconfig
}

do_compile() {
    # The Makefile doesn't seem to be able to find libgcc on its own in this environment,
    # so we tell it where it is with LIBGCC.
    # We also clear LDFLAGS due to them containing GCC args but being passed to LD.
    oe_runmake ${PARALLEL_MAKE} ARCH="${TARGET_ARCH}" CROSS_COMPILE="${TARGET_PREFIX}" LDFLAGS="" \
              LIBGCC="${RECIPE_SYSROOT}/usr/lib/${TARGET_SYS}/$(${TARGET_PREFIX}gcc -dumpversion)/libgcc.a"
}

inherit mkbootimg