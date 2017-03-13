package jul.funtory.graphsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jul.funtory.graphview.LineGraphView;
import jul.funtory.graphview.model.LineGraphModel;


/**
 * Created by JuL on 2017. 3. 7..
 */

public class LineGraphActivity extends AppCompatActivity {

    private LineGraphView lineGraphView;

    private Random random;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_line_graph);

        random = new Random(System.currentTimeMillis());

        lineGraphView = (LineGraphView) findViewById(R.id.line_view);

        List<LineGraphModel> datas = new ArrayList<>();
        for(int i = 0 ; i < 24 ; i++){
            datas.add(new LineGraphModel((i+1)*random.nextFloat(), (i+1)*random.nextFloat(), ""+i));
        }

        lineGraphView.setData(datas);
    }
}
