SUMMARY = "Modern AOSP tools for working with Android boot images"
HOMEPAGE = "https://android.googlesource.com/platform/system/tools/mkbootimg"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://mkbootimg.py;beginline=3;endline=15;md5=81fd84b0a4fa565c3c651a81d026addc"

SRC_URI = "git://android.googlesource.com/platform/system/tools/mkbootimg.git;protocol=https;branch=main"
SRCREV = "d2bb0af5ba6d3198a3e99529c97eda1be0b5a093"
S = "${WORKDIR}/git"
PV = "git"

BBCLASSEXTEND = "native"

do_install() {
    install -d ${D}${bindir}/gki
    install -m 0755 ${S}/mkbootimg.py ${D}${bindir}
    install -m 0755 ${S}/gki/certify_bootimg.py ${S}/gki/generate_gki_certificate.py ${D}${bindir}/gki
}
