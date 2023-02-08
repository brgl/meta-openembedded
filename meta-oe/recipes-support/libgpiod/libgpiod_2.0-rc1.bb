require libgpiod.inc

LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later & CC-BY-SA-4.0"
LIC_FILES_CHKSUM = " \
    file://LICENSES/GPL-2.0-or-later.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LICENSES/LGPL-2.1-or-later.txt;md5=4b54a1fd55a448865a0b32d41598759d \
    file://LICENSES/CC-BY-SA-4.0.txt;md5=fba3b94d88bfb9b81369b869a1e9a20f \
"

SRC_URI[sha256sum] = "64a718e8f8e022afc1880bae0b6ddc874fca1ce1bf8a2ac88bab4ccca6b66a1e"

S = "${WORKDIR}/libgpiod-2.0"

# We must enable gpioset-interactive for all gpio-tools tests to pass
PACKAGECONFIG[tests] = "--enable-tests --enable-gpioset-interactive,--disable-tests,kmod util-linux glib-2.0 catch2 libedit"
PACKAGECONFIG[gpioset-interactive] = "--enable-gpioset-interactive,--disable-gpioset-interactive,libedit"

FILES:${PN}-tools += "${bindir}/gpionotify"
FILES:${PN}-ptest += "${libdir}/libgpiosim.so.*"

RRECOMMENDS:${PN}-ptest += "kernel-module-gpio-sim"
