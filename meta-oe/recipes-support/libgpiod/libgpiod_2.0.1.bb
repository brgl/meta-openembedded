require libgpiod.inc

inherit systemd

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later & CC-BY-SA-4.0"
LIC_FILES_CHKSUM = " \
    file://LICENSES/GPL-2.0-or-later.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LICENSES/LGPL-2.1-or-later.txt;md5=4b54a1fd55a448865a0b32d41598759d \
    file://LICENSES/CC-BY-SA-4.0.txt;md5=fba3b94d88bfb9b81369b869a1e9a20f \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-2.x:"

SRC_URI += "file://gpio-tools-test-bats-modify.patch"

SRC_URI[sha256sum] = "b5367d28d045b36007a4ffd42cceda4c358737ef4f2ce22b0c1d05ec57a38392"

# Enable all project features for ptest
PACKAGECONFIG[tests] = "--enable-tests --enable-tools --enable-bindings-cxx --enable-bindings-glib --enable-gpioset-interactive --enable-dbus,--disable-tests,kmod util-linux glib-2.0 catch2 libedit glib-2.0-native libgudev"
PACKAGECONFIG[gpioset-interactive] = "--enable-gpioset-interactive,--disable-gpioset-interactive,libedit"
PACKAGECONFIG[glib] = "--enable-bindings-glib,--disable-bindings-glib,glib-2.0 glib-2.0-native"
PACKAGECONFIG[dbus] = "--enable-bindings-glib --enable-dbus,--disable-bindings-glib --disable-dbus,glib-2.0 glib-2.0-native libgudev"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-systemd', '--disable-systemd', d)}"

PACKAGES =+ "${PN}-ptest-dev ${PN}-glib ${PN}-manager ${PN}-cli"
FILES:${PN}-tools += "${bindir}/gpionotify"
FILES:${PN}-ptest += "${libdir}/libgpiosim.so.*"
FILES:${PN}-ptest-dev += "${includedir}/gpiosim.h"
FILES:${PN}-glib += "${libdir}/libgpiod-glib.so.*"
FILES:${PN}-manager += " \
    ${bindir}/gpio-manager \
    ${sysconfdir}/dbus-1/system.d/io.gpiod1.conf \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_system_unitdir}/gpio-manager.service', '', d)} \
"
FILES:${PN}-cli += "${bindir}/gpiocli"

RDEPENDS:${PN}-manager += "dbus"
RDEPENDS:${PN}-ptest += " \
    bash \
    shunit2 \
    libgpiod-manager \
"

SYSTEMD_PACKAGES = "${PN}-manager"
SYSTEMD_SERVICE:${PN}-manager = "gpio-manager.service"

RRECOMMENDS:${PN}-ptest += "kernel-module-gpio-sim"

do_install_ptest:append() {
    install -m 0755 ${S}/tools/gpio-tools-test.bash ${D}${PTEST_PATH}/tests/
    install -m 0644 ${S}/tests/bash/gpiod-bash-test-helper.inc ${D}${PTEST_PATH}/tests/
    install -m 0644 ${S}/tests/gpiosim/gpiosim.h ${D}${includedir}/gpiosim.h
    install -m 0755 ${B}/bindings/glib/tests/.libs/gpiod-glib-test ${D}${PTEST_PATH}/tests/
    install -m 0755 ${B}/dbus/tests/.libs/gpiodbus-test ${D}${PTEST_PATH}/tests/
    install -m 0755 ${B}/dbus/.libs/gpio-manager ${D}${PTEST_PATH}/tests
}
