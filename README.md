# Android Graph View
Android GraphView Library & Sample App


# Usage

For a working implementation of this project see the `app/` folder.

* CircleGraph
```java
 List<CircleGraphModel> datas = new ArrayList<>();
 datas.add(new CircleGraphModel(49, 0xffff8888, "여성"));
 datas.add(new CircleGraphModel(51, 0xff8888ff, "남성"));
 circleGraphView.setData(datas);
```

* BarGraph
```java
 List<BarGraphModel> datas = new ArrayList<>();
 datas.add(new BarGraphModel(1, "20"));
 datas.add(new BarGraphModel(1, "30"));
 datas.add(new BarGraphModel(1, "40"));
 datas.add(new BarGraphModel(0.6f, "50"));
 barGraphView.setData(datas);
```

* LineGraph
```java
 List<LineGraphModel> datas = new ArrayList<>();
 for(int i = 0 ; i < 24 ; i++){
     datas.add(new LineGraphModel((i+1)*random.nextFloat(), (i+1)*random.nextFloat(), ""+i));
 }
 lineGraphView.setData(datas);
```


# License

    Copyright 2017 JuL

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
