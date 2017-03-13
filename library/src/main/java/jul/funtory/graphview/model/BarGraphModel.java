package jul.funtory.graphview.model;

/**
 * Created by JuL on 2017. 3. 6..
 */

public class BarGraphModel {

    private String xText;
    private float value;


    public BarGraphModel(float value, String xText){
        this.value = value;
        this.xText = xText;
    }

    public String getXText() {
        return xText;
    }

    public float getValue() {
        return value;
    }
}
