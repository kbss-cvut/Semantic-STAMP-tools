INBAS Mobile
=============

Requirements
------------

  * Apache Cordova 4.1
	* see http://cordova.apache.org/docs/en/4.0.0//guide_cli_index.md.html#The%20Command-Line%20Interface

How to build APK for Android
----------------------------

  1. `cordova build android`

Simulator (FirefoxOS)
---------------------

Some development testing can be done using FirefoxOS simulator.

  1. Get simulator:
     https://developer.mozilla.org/en-US/docs/Tools/Firefox_OS_Simulator
  2. `cordova emulate firefoxos`
  3. Open app in emulator

Debugging
---------

Recent Chrome can connect to Android device and inspect any web view.

  1. Connect the device, check connection using adb.
  2. Open chrome://inspect/#devices in Chrome and check "Discover USB devices".
     List of web views should appear.
  3. Click "Inspect"
  4. Have fun!

