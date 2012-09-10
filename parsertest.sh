#!/bin/sh

java -ea -Xmx2048M -cp dist/frontend.jar:ext_libs/commons-cli-1.2.jar frontend.CompilerTest $@
