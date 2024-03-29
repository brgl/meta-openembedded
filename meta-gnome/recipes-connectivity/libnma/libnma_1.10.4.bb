SUMMARY = "NetworkManager GUI library"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 gtk+3 gtk4 networkmanager"

GNOMEBASEBUILDCLASS = "meson"
inherit gnomebase gobject-introspection gtk-doc gettext vala features_check

REQUIRED_DISTRO_FEATURES = "${@bb.utils.contains('PACKAGECONFIG','gcr','x11','',d)} opengl"
ANY_OF_DISTRO_FEATURES = "${GTK3DISTROFEATURES}"

SRC_URI[archive.sha256sum] = "79e730dda19f9b34886f4064aa170f1a2326b08329d655d80b67e906c7f78b7c"

PACKAGECONFIG ?= "gcr iso_codes mobile_broadband_provider_info"
PACKAGECONFIG[gcr] = "-Dgcr=true,-Dgcr=false,gcr"
PACKAGECONFIG[iso_codes] = "-Diso_codes=true,-Diso_codes=false,iso-codes,iso-codes"
PACKAGECONFIG[mobile_broadband_provider_info] = "-Dmobile_broadband_provider_info=true,-Dmobile_broadband_provider_info=false,mobile-broadband-provider-info,mobile-broadband-provider-info"

# for gnome-control-center >= 42
EXTRA_OEMESON = "-Dlibnma_gtk4=true"

# go introspection is not supported for mipsn32/riscv32, but vapi needs it
#
EXTRA_OEMESON:append:mipsarchn32 = " -Dvapi=false"
EXTRA_OEMESON:append:riscv32 = " -Dvapi=false"
EXTRA_OEMESON:append:powerpc64le = " -Dvapi=false"

GTKDOC_MESON_OPTION = "gtk_doc"
