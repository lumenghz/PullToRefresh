## PullLaunchRocket

[![Twitter](https://img.shields.io/badge/Twitter-@LuMengHZ-blue.svg?style=flat-square)](https://twitter.com/LuMengHZ)
[![License](https://img.shields.io/github/license/lubeast/PullLaunchRocket.svg?style=flat-square)](https://github.com/lubeast/PullLaunchRocket/blob/master/LICENSE)
[![Travis](https://img.shields.io/travis/lubeast/PullToRefresh/master.svg?style=flat-square)](https://travis-ci.org/lubeast/PullToRefresh)

感谢[Yalantis](https://github.com/Yalantis)为下拉刷新创建了一个炒鸡棒的逻辑案例, 也是`PullLaunchRocket`的基础.

欢迎各位点个`Star`给我.

- 样式一: 发射火箭

![rocket](https://raw.github.com/lubeast/PullLaunchRocket/master/screenshots/rocket.gif)

- 样式二: 太阳升起(仿58同城刷新效果)

![rocket](https://raw.github.com/lubeast/PullLaunchRocket/master/screenshots/sunraise.gif)

### 使用
*为了更好使用也可参照示例工程`sample`*

- 在project层级`build.gradle`中加入
```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

- 在module层级`build.gradle`中加入
```groovy
dependencies {
    compile 'com.github.lubeast:PullLaunchRocket:1.0.1'
}
```

- 在布局文件中使用`PullToRefreshView`
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
- 在`onCreate`方法中初始化此View并添加刷新监听
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
- 你可以调用`mPullToRefreshView.setRefreshing(boolean isRefreshing);`修改刷新状态.
