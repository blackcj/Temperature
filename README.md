Temperature
===========
Temperature is a native Android app to view the temperature and humidity of my apartment. The current temperature and humidity are monitored by a Spark Core and recorded to a MySQL database via a cron job. Data requests from the app are made with Retrofit. The graph is displayed using AChartEngine (a charting library for Android).

![Animated Gif](demo.gif)

Third Party Libraries
===========
+ Retrofit (used for API requests)
+ Butterknife (used for view injection)
+ AChartEngine (used to display the report graph)
+ Cupboard (will be used to cache data locally)
