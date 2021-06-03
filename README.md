# 👻 Cloaks+ Unofficial Client

![NodeJs](https://img.shields.io/badge/Written%20In-Kotlin-c71de5?style=for-the-badge) ![LINES OF CODE](https://img.shields.io/tokei/lines/github/jacobtread/CloaksPlus?style=for-the-badge) ![LICENSE](https://img.shields.io/github/license/jacobtread/CloaksPlus?style=for-the-badge)

This is an unofficial client for the service [Cloaks+](https://cloaks.plus/) the official client is available
at [https://cloaks.plus](https://cloaks.plus/)  this client aims to provide a cleaner UI and more operating system
support out of the box it supports windows

(Only tested on devices with a Java 8 Runtime)

## 📦 Compiling

To compile you can run the command

```shell
./gradlew launch4j
```

This will build the jar and create a windows executable You also must provide a jfxrt jar from your Java 8 Runtime this
can be found at JDK_8_PATH/jre/lib/ext/jfxrt.jar you need to place it at /libs/jfxrt.jar