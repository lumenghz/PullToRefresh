## PullLaunchRocket

[中文文档](./README_CHN.md)

[![Twitter](https://img.shields.io/badge/Twitter-@LuMengHZ-blue.svg?style=flat-square)](https://twitter.com/LuMengHZ)
[![License](https://img.shields.io/github/license/lubeast/PullLaunchRocket.svg?style=flat-square)](https://github.com/lubeast/PullLaunchRocket/blob/master/LICENSE)
[![Travis](https://img.shields.io/travis/lubeast/PullToRefresh/master.svg?style=flat-square)](https://travis-ci.org/lubeast/PullToRefresh)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PullToRefresh-brightgreen.svg?style=flat-square)](https://android-arsenal.com/details/1/3943)

You guys can see [another project](https://github.com/lubeast/AlphaLayout) depends on this one.

Thanks to [Yalantis](https://github.com/Yalantis) for creating a great logic of `PullToRefresh`. And that's logic is the fundation of `PullLaunchRocket` also.

Give me a `Star` please :D  And welcome to contact me or make a PR if you have any good style, we make it better together.

- style one: launch rocket

![rocket](https://raw.github.com/lubeast/PullLaunchRocket/master/screenshots/rocket.gif)

- style two: sun raise

![rocket](https://raw.github.com/lubeast/PullLaunchRocket/master/screenshots/sunraise.gif)

### Usage
*You can have a look at Sample Project* `sample` for better use.
- Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

- Add the dependency in your module-level `build.gradle`
```groovy
dependencies {
    compile 'com.github.lubeast:PullToRefresh:1.0.1'
}
```

- `PullToRefreshView` widget in your layout.xml
```xml
<lumenghz.com.pullrefresh.PullToRefreshView
        android:id="@+id/pull_to_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:lrefresh="rocket"
        >

        <ListView
            android:id="@+id/list_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:divider="@null"
            android:dividerHeight="0dp"
            android:fadingEdge="none"
            />

</lumenghz.com.pullrefresh.PullToRefreshView>
```

- Initial the `PullToRefreshView` and setup `OnRefreshListener` in your `onCreate` method
```java
mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
    @Override
    public void onRefresh() {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.setRefreshing(false);
            }
        }, REFRESH_DELAY);
    }
 });
```

- You can change refresh state through call
```java
mPullToRefreshView.setRefreshing(boolean isRefreshing)
```
