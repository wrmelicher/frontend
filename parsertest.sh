#!/bin/sh

java -ea -cp dist/frontend.jar:ext_libs/commons-cli-1.2.jar frontend.CompilerTest $@
