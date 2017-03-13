package jul.funtory.graphsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jul.funtory.graphview.BarGraphView;
import jul.funtory.graphview.model.BarGraphModel;


/**
 * Created by JuL on 2017. 3. 3..
 */

public class BarGraphActivity extends AppCompatActivity {

    private BarGraphView barGraphView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar_graph);

        barGraphView = (BarGraphView) findViewById(R.id.bar_view);
        barGraphView.setBgColor(0xffefefef);
        barGraphView.setHighlightColor(0xffff0000);

        final List<BarGraphModel> datas = new ArrayList<>();
        datas.add(new BarGraphModel(1, "20"));
        datas.add(new BarGraphModel(1, "30"));
        datas.add(new BarGraphModel(1, "40"));
        datas.add(new BarGraphModel(0.6f, "50"));

        barGraphView.setData(datas);

//        barGraphView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                barGraphView.setData(datas);
//            }
//        }, 1000);
    }


}
