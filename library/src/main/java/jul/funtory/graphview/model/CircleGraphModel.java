package jul.funtory.graphview.model;

/**
 * Created by JuL on 2017. 3. 6..
 */

public class CircleGraphModel {
    private int value;
    private int color;
    private String text;


    public CircleGraphModel(int value, int color, String text){
        this.value = value;
        this.color = color;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }
}
