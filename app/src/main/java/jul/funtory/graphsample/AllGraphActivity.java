package jul.funtory.graphsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jul.funtory.graphview.BarGraphView;
import jul.funtory.graphview.CircleGraphView;
import jul.funtory.graphview.LineGraphView;
import jul.funtory.graphview.model.BarGraphModel;
import jul.funtory.graphview.model.CircleGraphModel;
import jul.funtory.graphview.model.LineGraphModel;


/**
 * Created by JuL on 2017. 3. 7..
 */

public class AllGraphActivity extends AppCompatActivity {
    private BarGraphView barGraphView;
    private CircleGraphView circleGraphView;
    private LineGraphView lineGraphView;

    private Random random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_graph);


        barGraphView = (BarGraphView) findViewById(R.id.bar_view);

        barGraphView.setBgColor(0xffefefef);
        barGraphView.setHighlightColor(0xffff0000);

        List<BarGraphModel> datas = new ArrayList<>();
        datas.add(new BarGraphModel(1.5f, "20"));
        datas.add(new BarGraphModel(1.3f, "30"));
        datas.add(new BarGraphModel(2.7f, "40"));
        datas.add(new BarGraphModel(0.6f, "50"));

        barGraphView.setData(datas);


        circleGraphView = (CircleGraphView) findViewById(R.id.circle_view);

        final List<CircleGraphModel> datas2 = new ArrayList<>();
        datas2.add(new CircleGraphModel(49, 0xffff8888, "여성"));
        datas2.add(new CircleGraphModel(51, 0xff8888ff, "남성"));
        circleGraphView.setData(datas2);



        random = new Random(System.currentTimeMillis());

        lineGraphView = (LineGraphView) findViewById(R.id.line_view);
        List<LineGraphModel> datas3 = new ArrayList<>();
        for(int i = 0 ; i < 24 ; i++){
            datas3.add(new LineGraphModel(i*random.nextFloat(), i*random.nextFloat(), ""+i));
        }

        lineGraphView.setData(datas3);
    }
}
