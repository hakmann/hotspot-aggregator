# This script determines the OS of the linux system, and runs the appropriate create
# user command

# Non platform specific bash String contains method
stringContain() { [ -z "${2##*$1*}" ]; }

if [ -f /etc/os-release ]; then
    # freedesktop.org and systemd
    . /etc/os-release
    OS=$NAME
    VER=$VERSION_ID
elif type lsb_release >/dev/null 2>&1; then
    # linuxbase.org
    OS=$(lsb_release -si)
    VER=$(lsb_release -sr)
elif [ -f /etc/lsb-release ]; then
    # For some versions of Debian/Ubuntu without lsb_release command
    . /etc/lsb-release
    OS=$DISTRIB_ID
    VER=$DISTRIB_RELEASE
elif [ -f /etc/debian_version ]; then
    # Older Debian/Ubuntu/etc.
    OS=Debian
    VER=$(cat /etc/debian_version)
else
    # Fall back to uname, e.g. "Linux <version> ", also works for BSD, etc.
    OS=$(uname -s)
    VER=$(uname -r)
fi

if stringContain 'Alpine' "$OS" ; then
adduser -u 1000 -s sh -D java
else
useradd -m -u 1000 -s /bin/bash java
fi