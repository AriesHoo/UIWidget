# UIWidget
--------------------------
[![](https://img.shields.io/badge/download-demo-blue.svg)](https://raw.githubusercontent.com/AriesHoo/UIWidget/master/apk/sample.apk)
[![](https://jitpack.io/v/AriesHoo/UIWidget.svg)](https://jitpack.io/#AriesHoo/UIWidget)
[![](https://img.shields.io/github/release/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/releases)
[![API](https://img.shields.io/badge/API-11%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![GitHub license](https://img.shields.io/github/license/AriesHoo/UIWidget.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/简书-AriesHoo-blue.svg)](http://www.jianshu.com/u/a229eee96115)
<!-- [![GitHub stars](https://img.shields.io/github/stars/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/stargazers) -->
<!-- [![GitHub forks](https://img.shields.io/github/forks/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/network) -->
[![](https://img.shields.io/badge/特别声明-3.2.7为最后一个support包版本,3.2.7-androi为第一个androidx包版本注意不要冲突-red.svg)](http://www.jianshu.com/u/a229eee96115)


## 简介：

一个集成[UIAlertDialog](https://github.com/AriesHoo/UIWidget/blob/master/widget-alert/src/main/java/com/aries/ui/widget/alert/UIAlertDialog.java)、[UIActionSheetDialog](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/widget/action/sheet/UIActionSheetDialog.java)、[UIProgressDialog](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/widget/progress/UIProgressDialog.java)、[RadiusView](https://github.com/AriesHoo/RadiusView)、[TitleBarView](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/view/title/TitleBarView.java)、
[CollapsingTitleBarLayout](https://github.com/AriesHoo/UIWidget/blob/master/widget-collapsing/src/main/java/com/aries/ui/view/title/CollapsingTitleBarLayout.java)、[StatusViewHelper](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/helper/status/StatusViewHelper.java)、[NavigationViewHelper](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/helper/navigation/NavigationViewHelper.java)
等项目常用UI库

[[Download-蒲公英]](https://www.pgyer.com/CE6Z)

![Download-蒲公英](/apk/qr_pgyer.png)

[[Download-github]](https://raw.githubusercontent.com/AriesHoo/UIWidget/master/apk/sample.apk)

![Download-github](/apk/qr.png)

## 重大更新日志

**特别声明**

**1、3.2.7 是最后一个support包版本**

**2、3.2.7-androidx 为第一个androidx版本 以后版本均为androidx版本注意不要support与androidx冲突**

**3、多看源码注释及Demo演示,学会自己解决问题**

* 3.2.11

    * TitleBarView增加addLeftAction、addCenterAction、addRightAction 重写Api并重新优化撤销沉浸逻辑
    * StatusViewHelper修订Api并增加撤销效果逻辑
    * NavigationBarUtil 增加导航栏深色图标模式Api
    * NavigationViewHelper增加Dialog支持配置KeyboardHelper也增加Dialog支持,并优化增加假导航栏逻辑
    * UIActionSheetDialog增加导航控制监听方法
    
* 3.2.7-androidx

    * 3.2.7 更换androidx支持
    
* 3.2.7 更新
      
    * 3.2.6 beta 的最后一个稳定support版本
    
* 3.2.3 更新
      
    * 优化:优化UIActionSheetDialog 导航栏控制逻辑 
    * 优化:新增NavigationViewHelper 设置假View DrawableTop属性
    * 优化:调整RadiusView Drawable及TextColor、BackgroundColor、StrokeColor 设置逻辑

* 3.2.1 更新
        
    * 修复:RadiusView下新增TextView及CheckBox等代理类用于解决泛型错误BUG
    * 优化:使用AS 3.1.2 + Gradle 4.4进行优化
    * 优化:增加AlphaViewHelper 按下及不可操作 alpha值通过Style 设置--参考demo Style样式
    * 优化:去掉默认控制是否可点击控制,调整水波纹效果开启逻辑
	
* 3.1.0 更新
        
	* 发布正式版本
	
* 3.0.0-alpha5 更新
        
	* 修改TitleBarView 方法setOutPadding 逻辑以增加左右TextView 点击范围
	* 修改UIActionSheetDialog 控制Title、Item、Cancel已经CancelMarginTop逻辑方法控制各种形式下背景Drawable
	
* 3.0.0-alpha4 更新
        
	* 新增BasisDialog控制虚拟导航栏效果的方法-初试
	* 新增UIActionSheetDialog 控制View是否手指拖拽(通过V4包ViewDragHelper实现)
	
* 3.0.0-alpha3 更新
        
	* 新增UIActionSheetDialog 设置文本与图片间隔
	* 删除UIAlertView、UIActionSheetView、UIProgressView及相应的资源文件
	
* 3.0.0-alpha 更新
	* 完整重构整个UI库-拆分库为widget-core(TitleBarView、RadiusView、AlphaView、Helper、UIActionSheetDialog、UIProgressDialog)、widget-alert、widget-collapsing
	* widget-core新增状态栏控制帮助类[StatusViewHelper](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/helper/status/StatusViewHelper.java)及虚拟导航栏控制帮助类[NavigationViewHelper](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/helper/navigation/NavigationViewHelper.java)
	* widget-core新增控制View按下透明度变化帮助类[AlphaViewHelper](https://github.com/AriesHoo/UIWidget/blob/master/widget-core/src/main/java/com/aries/ui/helper/alpha/AlphaViewHelper.java) 并增加对应常用View控件AlphaTextView、AlphaCheckBox、AlphaRadioButton、AlphaRelativeLayout等基础控件
	* TitleBarView属性规范并删除部分冗余属性;增加与widget-collapsing库中CollapsingTitleBarLayout配合达到CollapsingToolbarLayout+Toolbar效果参考[TitleWithCollapsingLayoutActivity](https://github.com/AriesHoo/UIWidget/blob/master/app/src/main/java/com/aries/ui/widget/demo/module/title/TitleWithCollapsingLayoutActivity.java)
	* RadiusView 增加RadiusSwitch并将属性规范并拆分不同的delegate代理类管理
	* 新增UIActionSheetDialog 添加不同Builder用于替换原有UIActionSheetView控件UIActionSheetView标记为废弃正式版本移除
	* 新增UIProgressDialog 添加不同Builder用于替换原有UIProgressView控件UIProgressView标记为废弃正式版本移除
	* 新增UIAlertDialog 添加不同Builder用于替换原有UIAlertView控件UIAlertView标记为废弃正式版本移除
	
* 2.0.5 更新
    * TitleBarView父容器为ConstraintLayout高度测量不准BUG-参考[TitleWithConstraintActivity](https://github.com/AriesHoo/UIWidget/blob/master/app/src/main/java/com/aries/ui/widget/demo/module/title/TitleWithConstraintActivity.java)

## 录屏预览

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/widget.gif)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/StatusViewHelper.gif)

[StatusViewHelper](https://github.com/AriesHoo/UIWidget/blob/dev/widget-core/src/main/java/com/aries/ui/helper/status/StatusViewHelper.java)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/title.gif)

[TitleBarView](https://github.com/AriesHoo/TitleBarView)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/titleAction.gif)

[TitleBarView-添加自定义Action](https://github.com/AriesHoo/UIWidget/blob/dev/app/src/main/java/com/aries/ui/widget/demo/module/title/TitleActionActivity.java)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/alert.gif)

[UIAlertDialog](https://github.com/AriesHoo/UIWidget/blob/dev/widget-alert/src/main/java/com/aries/ui/widget/alert/UIAlertDialog.java)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/action.gif)

[UIActionSheetDialog](https://github.com/AriesHoo/UIWidget/blob/dev/widget-core/src/main/java/com/aries/ui/widget/action/sheet/UIActionSheetDialog.java)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/loading.gif)

[UIProgressDialog](https://github.com/AriesHoo/UIWidget/blob/dev/widget-core/src/main/java/com/aries/ui/widget/progress/UIProgressDialog.java)

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/radius.gif)

[RadiusView](https://github.com/AriesHoo/UIWidget/blob/dev/app/src/main/java/com/aries/ui/widget/demo/module/radius/RadiusActivity.java)

模拟器软键盘控制

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/keyboard01.gif)

华为可隐藏软键盘控制

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/keyboard02.gif)

华为全面屏手势控制

![](https://github.com/AriesHoo/UIWidget/blob/dev/screenshot/keyboard03.gif)

[KeyboardHelper](https://github.com/AriesHoo/UIWidget/blob/dev/widget-core/src/main/java/com/aries/ui/helper/navigation/KeyboardHelper.java)
  
**Gradle集成**

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

[![](https://jitpack.io/v/AriesHoo/UIWidget.svg)](https://jitpack.io/#AriesHoo/UIWidget)
[![](https://img.shields.io/github/release/AriesHoo/UIWidget.svg)](https://github.com/AriesHoo/UIWidget/releases)

##### 3.0.0及以后版本-core为核心库、alert及collapsing默认compile了core库

```
dependencies {
     //implementation 'com.github.AriesHoo.UIWidget:widget-core:3.2.10'
     //implementation 'com.github.AriesHoo.UIWidget:widget-alert:3.2.10'
     //implementation 'com.github.AriesHoo.UIWidget:widget-collapsing:3.2.10'
     implementation 'com.github.AriesHoo.UIWidget:widget-core:${LATEST_VERSION}'
     implementation 'com.github.AriesHoo.UIWidget:widget-alert:${LATEST_VERSION}'
     implementation 'com.github.AriesHoo.UIWidget:widget-collapsing:${LATEST_VERSION}'
}
```

##### 3.0.0以前版本

```
dependencies {
     //implementation 'com.github.AriesHoo:UIWidget:2.0.9'
     implementation 'com.github.AriesHoo:UIWidget:${LATEST_VERSION}'
}
```


**Maven集成**

```
<repositories>
     <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
     </repository>
</repositories>
```

##### 3.0.0以后版本

```
<dependency>
    <groupId>com.github.AriesHoo.UIWidget</groupId>
    <artifactId>widget-core</artifactId>
    <artifactId>widget-collapsing</artifactId>
    <artifactId>widget-alert</artifactId>
    <version>3.2.10</version>
</dependency>
```

##### 3.0.0以前版本

```
<dependency>
    <groupId>com.github.AriesHoo</groupId>
    <artifactId>UIWidget</artifactId>
    <version>2.0.9</version>
</dependency>
```


**UIAlertDialog属性控制**

```
UIAlertDialog alertDialog = new UIAlertDialog.DividerQQBuilder(this)
                        //设置背景--包括根布局及Button
                        .setBackgroundColor(Color.WHITE)
//                        .setBackground(drawable)
//                        .setBackgroundResource(resId)
                        //设置按下背景--Button
                        .setBackgroundPressedColor(Color.argb(255, 240, 240, 240))
//                        .setBackgroundPressed(drawable)
//                        .setBackgroundPressedResource(resId)
                        //背景圆角(当背景及按下背景为ColorDrawable有效)-根布局及Button
                        .setBackgroundRadius(6f)
//                        .setBackgroundRadiusResource(resId)
                        //设置统一padding
                        .setPadding(SizeUtil.dp2px(20))
                        //设置根布局最小高度
                        .setMinHeight(SizeUtil.dp2px(160))
                        .setElevation(12f)

                        //设置Title上边分割线颜色--推荐
                        .setTitleDividerColor(Color.RED)
//                        .setTitleDividerResource(resId)
//                        .setTitleDivider(drawable)
                        //设置Title分割线高度
                        .setTitleDividerHeight(SizeUtil.dp2px(4))
//                        .setTitleDividerHeightResource(resId)
                        //设置TextView对应的尺寸单位
                        .setTextSizeUnit(TypedValue.COMPLEX_UNIT_DIP)
                        .setLineSpacingExtra(0f)
                        .setLineSpacingMultiplier(1.0f)
                        //设置Title文本
                        .setTitle("UIAlertDialog示例头部")
//                        .setTitle(resId)
                        //设置Title文本颜色
                        .setTitleTextColor(Color.BLACK)
//                        .setTitleTextColor(ColorStateList)
//                        .setTitleTextColorResource(resId)
                        //设置Title文本尺寸
                        .setTitleTextSize(20f)
                        //设置Title文本对齐方式
                        .setTitleTextGravity(Gravity.CENTER)
                        //设置Title文本是否加粗
                        .setTitleTextFakeBoldEnable(false)

                        //设置Message文本
                        .setMessage(Html.fromHtml(String.format(mFormatName, "你将退出 ", "四川移动爱分享抢流量(XXXXXXXX)", "退群通知仅群管理员可见。")))
//                        .setMessage(resId)
                        //设置Message文本颜色
                        .setMessageTextColor(Color.BLACK)
//                        .setMessageTextColor(ColorStateList)
//                        .setMessageTextColorResource(resId)
                        //设置Message文本尺寸
                        .setMessageTextSize(16f)
                        //设置Message文本对齐方式
                        .setMessageTextGravity(Gravity.CENTER)
                        //设置Title文本是否加粗
                        .setMessageTextFakeBoldEnable(false)

                        //设置View --始终在Message下边
//                        .setView(View)
//                        .setView(layoutId)

                        //设置是否去掉Button按下阴影-5.0以后的新特性
                        .setBorderLessButtonEnable(true)
                        //文本及点击事件
                        .setNegativeButton("取消", onAlertClick)
//                        .setNegativeButton(resId,click)
                        //文本颜色
                        .setNegativeButtonTextColor(Color.BLACK)
//                        .setNegativeButtonTextColor(ColorStateList)
//                        .setNegativeButtonTextColorResource(resId)
                        //文本尺寸
                        .setNegativeButtonTextSize(18f)
                        //是否粗体
                        .setNegativeButtonFakeBoldEnable(false)

                        //文本及点击事件
                        .setNeutralButton("考虑", onAlertClick)
//                        .setNeutralButton(resId,click)
                        //文本颜色
                        .setNeutralButtonTextColor(Color.BLACK)
//                        .setNeutralButtonTextColor(ColorStateList)
//                        .setNeutralButtonTextColorResource(resId)
                        //文本尺寸
                        .setNeutralButtonTextSize(18f)
                        //是否粗体
                        .setNeutralButtonFakeBoldEnable(false)

                        //文本及点击事件
                        .setPositiveButton("退出", onAlertClick)
//                        .setPositiveButton(resId,click)
                        //文本颜色
                        .setPositiveButtonTextColor(Color.BLACK)
//                        .setPositiveButtonTextColor(ColorStateList)
//                        .setPositiveButtonTextColorResource(resId)
                        //文本尺寸
                        .setPositiveButtonTextSize(18f)
                        //是否粗体
                        .setPositiveButtonFakeBoldEnable(false)

                        //设置点击返回键是否可关闭Window
                        .setCancelable(true)
                        //设置点击非布局是否关闭Window
                        .setCanceledOnTouchOutside(true)
                        //设置Window dismiss()监听
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        //设置Window cancel()监听
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        })
                        //设置 window show()监听
                        .setOnShowListenerer(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {

                            }
                        })
                        //设置Window 键盘事件监听
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                return false;
                            }
                        })
                        .setOnTextViewLineListener(new BasisDialog.OnTextViewLineListener() {
                            @Override
                            public void onTextViewLineListener(TextView textView, int lineCount) {
                                switch (textView.getId()) {
                                    case R.id.tv_titleAlertDialog:
                                        break;
                                    case R.id.tv_messageAlertDialog:
                                        break;
                                    case R.id.btn_negativeAlertDialog:
                                        break;
                                    case R.id.btn_neutralAlertDialog:
                                        break;
                                    case R.id.btn_positiveAlertDialog:
                                        break;
                                }
                            }
                        })

                        //创建Dialog
                        .create()
                        //设置Window宽度
                        .setWidth(WindowManager.LayoutParams.WRAP_CONTENT)
                        //设置Window高度
                        .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                        //设置Window 阴影程度
                        .setDimAmount(0.6f)
                        //设置window其它属性
//                        .setAttributes(WindowManager.LayoutParams)
                        //设置window动画
//                        .setWindowAnimations(resId)
                        //设置Window 位置
                        .setGravity(Gravity.CENTER);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                alertDialog.show();
```

## License

```
Copyright 2016-2019 Aries Hoo

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


