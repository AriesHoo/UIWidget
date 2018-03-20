# UIWidget
--------------------------
[![fir.im](https://img.shields.io/badge/download-fir.im-blue.svg)](http://fir.im/r84v)
[![](https://jitpack.io/v/AriesHoo/UIWidget.svg)](https://jitpack.io/#AriesHoo/UIWidget)
[![](https://img.shields.io/github/release/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/releases)
[![API](https://img.shields.io/badge/API-11%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![GitHub license](https://img.shields.io/github/license/AriesHoo/UIWidget.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/简书-AriesHoo-blue.svg)](http://www.jianshu.com/u/a229eee96115)
<!-- [![GitHub stars](https://img.shields.io/github/stars/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/stargazers) -->
<!-- [![GitHub forks](https://img.shields.io/github/forks/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/network) -->

## 简介：

一个集成[UIAlertView](https://github.com/AriesHoo/UIAlertView)、[UIActionSheetDialog](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/widget/action/sheet/UIActionSheetDialog.java)、[UIProgressDialog](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/widget/progress/UIProgressDialog.java)、[RadiusView](https://github.com/AriesHoo/RadiusView)、[TitleBarView](https://github.com/AriesHoo/TitleBarView)
等项目常用UI库

[[Download]](https://raw.githubusercontent.com/AriesHoo/UIWidget/master/apk/sample.apk)

![](https://github.com/AriesHoo/UIWidget/blob/master/apk/qr.png)

**特别说明:1、RadiusView设置海拔(elevation)在7.1以下版本需在xml属性设置rv_strokeColor方可有效--据悉因为设置海拔必须是和设置背景同步的**

**特别更新说明:
  2.0.5:TitlteBarView父容器为ConstraintLayout高度测量不准BUG**
  
**Gradle集成**

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

```
dependencies {
     //compile 'com.github.AriesHoo:UIWidget:2.0.9'
     compile 'com.github.AriesHoo:UIWidget:${LATEST_VERSION}'
}
```
## 录屏预览

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/widget.gif)

[UIAlertView](https://github.com/AriesHoo/UIAlertView)

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/alert.gif)

[UIActionSheetView](https://github.com/AriesHoo/UIActionSheetView)

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/action.gif)

[UIProgressView](https://github.com/AriesHoo/UIProgressView)

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/loading.gif)

[RadiusView](https://github.com/AriesHoo/RadiusView)

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/radius.gif)

[TitleBarView](https://github.com/AriesHoo/TitleBarView)

![](https://github.com/AriesHoo/UIWidget/blob/master/screenshot/title.gif)


## License

```
Copyright 2016-2018 Aries Hoo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


