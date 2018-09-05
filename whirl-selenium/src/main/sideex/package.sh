#!/bin/bash

# jar and xpi files name - must be lowercase with no spaces
APP_NAME='sideex'
CHROME_PROVIDERS='content'
# which chrome providers we have (space-separated list)
ROOT_DIRS='defaults'         # ...and these directories       (space separated list)

ROOT_DIR=`pwd`
TMP_DIR=build

rm -f $APP_NAME.jar $APP_NAME.xpi
rm -rf $TMP_DIR

mkdir -p $TMP_DIR/chrome

cp -v -R chrome/content $TMP_DIR/chrome
cp -v -R chrome/locale $TMP_DIR/chrome
cp -v -R chrome/skin $TMP_DIR/chrome
cp -v -R $ROOT_DIRS $TMP_DIR
cp -v install.rdf $TMP_DIR
cp -v chrome.manifest $TMP_DIR/chrome.manifest

cd $TMP_DIR
echo Package the $APP_NAME.xpi
zip -r ../$APP_NAME.xpi *

cd "$ROOT_DIR"
