# RxJava-Retrofit-MVP
用Rxjava+Retrofit+MVP写的小Demo
# 介绍
API来自干货集中营，利用MVP重构了之前写的一个Demo  [MeiZhi](https://github.com/miaoquanwei/MeiZhi)
- 主界面的瀑布流式的图片加载，点击图片进入图片显示界面，这个界面用ViewPager实现左右滑动
- 该Demo学习将一些Acticity和Fragment的通用代码封装出来，例如BaseActivity, BaseFragment,还有Activity的管理类ActivityManager
- 用到了EasyRecycler开源库，它将RecyclerView的Adapter与ViewHolder解耦
