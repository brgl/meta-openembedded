require libgpiod.inc

inherit systemd

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later & CC-BY-SA-4.0"
LIC_FILES_CHKSUM = " \
    file://LICENSES/GPL-2.0-or-later.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LICENSES/LGPL-2.1-or-later.txt;md5=4b54a1fd55a448865a0b32d41598759d \
    file://LICENSES/CC-BY-SA-4.0.txt;md5=fba3b94d88bfb9b81369b869a1e9a20f \
"

SRC_URI[sha256sum] = "f74cbf82038b3cb98ebeb25bce55ee2553be28194002d2a9889b9268cce2dd07"

S = "${WORKDIR}/libgpiod-2.0"

# Enable all project features for ptest
PACKAGECONFIG[tests] = "--enable-tests --enable-tools --enable-bindings-cxx --enable-bindings-glib --enable-gpioset-interactive --enable-dbus,--disable-tests,kmod util-linux glib-2.0 catch2 libedit glib-2.0-native libgudev"
PACKAGECONFIG[gpioset-interactive] = "--enable-gpioset-interactive,--disable-gpioset-interactive,libedit"
PACKAGECONFIG[glib] = "--enable-bindings-glib,--disable-bindings-glib,glib-2.0 glib-2.0-native"
PACKAGECONFIG[dbus] = "--enable-bindings-glib --enable-dbus,--disable-bindings-glib --disable-dbus,glib-2.0 glib-2.0-native libgudev"

PACKAGES =+ "${PN}-glib ${PN}-manager ${PN}-cli"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-systemd', '--disable-systemd', d)}"

FILES:${PN}-glib += "${libdir}/libgpiod-glib.so.*"
FILES:${PN}-manager += "${bindir}/gpio-manager ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_system_unitdir}/gpio-manager.service', '', d)}"
FILES:${PN}-cli += "${bindir}/gpiocli"
FILES:${PN}-tools += "${bindir}/gpionotify"
FILES:${PN}-ptest += "${libdir}/libgpiosim.so.*"

SYSTEMD_PACKAGES = "${PN}-manager"
SYSTEMD_SERVICE:${PN}-manager = "gpio-manager.service"

RRECOMMENDS:${PN}-ptest += "kernel-module-gpio-sim"

do_install_ptest:append() {
    install -m 0755 ${B}/bindings/glib/tests/.libs/gpiod-glib-test ${D}${PTEST_PATH}/tests/
    install -m 0755 ${B}/dbus/tests/.libs/gpiodbus-test ${D}${PTEST_PATH}/tests/
    install -m 0755 ${B}/dbus/.libs/gpio-manager ${D}${PTEST_PATH}/tests
}
