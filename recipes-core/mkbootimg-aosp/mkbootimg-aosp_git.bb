SUMMARY = "Modern AOSP tools for working with Android boot images"
HOMEPAGE = "https://android.googlesource.com/platform/system/tools/mkbootimg"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://mkbootimg.py;beginline=3;endline=15;md5=81fd84b0a4fa565c3c651a81d026addc"

SRC_URI = "git://android.googlesource.com/platform/system/tools/mkbootimg.git;protocol=https;branch=main"
SRCREV = "c45163bf1cdb731e3bdd90d69d54c8e92004d673"
S = "${WORKDIR}/git"
PV = "git"

BBCLASSEXTEND = "native"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/mkbootimg.py ${D}${bindir}
}