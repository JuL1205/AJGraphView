package jul.funtory.graphsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jul.funtory.graphview.CircleGraphView;
import jul.funtory.graphview.model.CircleGraphModel;


/**
 * Created by JuL on 2017. 3. 3..
 */

public class CircleGraphActivity extends AppCompatActivity {
    private CircleGraphView circleGraphView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circle_graph);


        final List<CircleGraphModel> datas = new ArrayList<>();
        datas.add(new CircleGraphModel(49, 0xffff8888, "여성"));
        datas.add(new CircleGraphModel(51, 0xff8888ff, "남성"));
        circleGraphView = (CircleGraphView) findViewById(R.id.circle_view);
        circleGraphView.setData(datas);

//        circleGraphView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                circleGraphView.setData(datas);
//            }
//        }, 1000);
    }
}
