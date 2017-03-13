package jul.funtory.graphview.model;

/**
 * Created by JuL on 2017. 3. 10..
 */

public class LineGraphModel {

    private String xText;
    private float value;
    private float avgValue;


    public LineGraphModel(float value, float avgValue, String xText){
        this.value = value;
        this.avgValue = avgValue;
        this.xText = xText;
    }

    public String getXText() {
        return xText;
    }

    public float getValue() {
        return value;
    }

    public float getAvgValue() {
        return avgValue;
    }

}
