#!/bin/bash

PRG=$0

while [ -h "$PRG" ]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '^.*-> \(.*\)$' 2>/dev/null`
    if expr "$link" : '^/' 2> /dev/null >/dev/null; then
        PRG="$link"
    else
        PRG="`dirname "$PRG"`/$link"
    fi
done

progdir=`dirname "$PRG"`

if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
elif [ -x /usr/libexec/java_home ]; then
  JAVACMD="`/usr/libexec/java_home`/bin/java"
else
  JAVACMD="/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java"
fi

exec "$JAVACMD" -classpath "$progdir/../Resources/Java/*" \
       -Dapple.laf.useScreenMenuBar=true \
       TuioDriver
