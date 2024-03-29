DESCRIPTION = "Hardware identification and configuration data"
HOMEPAGE = "https://github.com/vcrhonek/hwdata"
SECTION = "System/Base"

LICENSE = "GPL-2.0-or-later | XFree86-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1556547711e8246992b999edd9445a57"

SRCREV = "3ac83cecf56f299e7390e5afeab2808632719243"
SRC_URI = " \
    git://github.com/vcrhonek/${BPN}.git;branch=master;protocol=https \
    file://0001-hwdata.pc.in-use-sysroot-prefix-for-pkgdatadir-varia.patch \
"

S = "${WORKDIR}/git"

inherit allarch

do_configure() {
    ${S}/configure --datadir=${datadir} --libdir=${libdir}
}

do_compile[noexec] = "1"

do_install() {
    oe_runmake install DESTDIR=${D}
}

FILES:${PN} = "${libdir}/* \
               ${datadir}/* "
