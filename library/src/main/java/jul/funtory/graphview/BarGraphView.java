package jul.funtory.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

import jul.funtory.graphview.model.BarGraphModel;


/**
 * Created by JuL on 2017. 3. 6..
 */

public class BarGraphView extends AbsGraphView {
    private float ANIM_THRESHOLD;
    private final static int LINE_COUNT = 5;

    private List<BarGraphModel> dataList;

    private RectF[] barRect;
    private RectF[] animRect;

    private RectF targetRect;
    private RectF graphRect;

    private int highlightColor = 0xffaaaaff;
    private int bgColor = 0xffdadada;

    //dimen값
    private int graphMargin;
    private int axisTextMargin;
    private int barWidth;
    private int barLineWidth;

    public BarGraphView(Context context) {
        super(context);

        init();
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHighlightColor(int color){
        highlightColor = color;
    }

    public void setBgColor(int color){
        bgColor = color;
    }

    private void init() {
        graphMargin = getResources().getDimensionPixelOffset(R.dimen.graph_margin);
        axisTextMargin = getResources().getDimensionPixelOffset(R.dimen.axis_text_margin);
        barWidth = getResources().getDimensionPixelOffset(R.dimen.bar_graph_width);
        barLineWidth = getResources().getDimensionPixelOffset(R.dimen.bar_graph_line_width);
    }

    public void setData(List<BarGraphModel> datas) {
        dataList = datas;

        resetDependOnViewSize();

        startDraw();
    }

    @Override
    protected boolean onDrawGraph(Canvas canvas) {
        if(barRect ==  null || barRect.length == 0){
            return true;
        }

        drawText(canvas);

        drawLine(canvas);

        drawBar(canvas);

        return Arrays.equals(animRect, barRect);
    }

    @Override
    protected void resetDependOnViewSize() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if(viewWidth == 0 || viewHeight == 0){
            return;
        }

        targetRect = new RectF(graphMargin, graphMargin, viewWidth - graphMargin, viewHeight - graphMargin);

        if (dataList != null && dataList.size() > 0) {
            barRect = new RectF[dataList.size()];
            //텍스트 높이 계산
            Rect textRect = new Rect();
            getPaint(0).getTextBounds(dataList.get(0).getXText(), 0, dataList.get(0).getXText().length(), textRect);
            int textHeight = textRect.height();

            //텍스트영역을 제외한 그래프만의 영역 계산
            graphRect = new RectF(targetRect.left, targetRect.top, targetRect.right, targetRect.bottom - axisTextMargin - textHeight);

            //bar 간 margin 계산
            float dataCount = (float) dataList.size();
            float barMargin = (graphRect.width() - (barWidth * dataCount)) / (dataCount + 1);

            //각 bar 의 rect 계산
            float pixelPerValue = graphRect.height() / (getMaxValue() * 1.1f);  //값1당 y픽셀값 계산
            for (int i = 0; i < dataList.size(); i++) {
                int blankCount = i + 1;
                float left = (blankCount * barMargin) + (i * barWidth) + graphMargin;
                float top = graphRect.bottom - (dataList.get(i).getValue() * pixelPerValue);
                barRect[i] = new RectF(left, top, left + barWidth, graphRect.bottom);
            }

            //anim을 위한 값 세팅
            animRect = new RectF[barRect.length];
            for(int i = 0; i < barRect.length ; i++){
                animRect[i] = new RectF(barRect[i].left, barRect[i].bottom, barRect[i].right, barRect[i].bottom);
            }

            ANIM_THRESHOLD = graphRect.height() / 80.f;
        }
    }

    private float getMaxValue() {
        float max = 0;

        if (dataList != null && dataList.size() > 0) {
            for (BarGraphModel barGraphModel : dataList) {
                if (barGraphModel.getValue() > max) {
                    max = barGraphModel.getValue();
                }
            }
        }

        return max;
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true); //text anti alias
        paint.setColor(color);
        paint.setStrokeWidth(barLineWidth);
        paint.setTextSize(barWidth / 1.5f);
        return paint;
    }

    private void drawBar(Canvas canvas) {
        if (barRect != null) {
            float max = getMaxValue();

            for (int i = 0; i < barRect.length; i++) {
                BarGraphModel barGraphModel = dataList.get(i);
                RectF targetRect = animRect[i];
                targetRect.top -= ANIM_THRESHOLD;
                if(targetRect.top < barRect[i].top){
                    targetRect.top = barRect[i].top;
                }

                if(barGraphModel.getValue() == max){
                    canvas.drawRect(targetRect, getPaint(highlightColor));

                    Paint numberOnePaint = getPaint(0xffffffff);
                    numberOnePaint.setFakeBoldText(true);
                    numberOnePaint.setTextSize(barWidth/1.2f);
                    String text = "1";
                    Rect textRect = new Rect();
                    numberOnePaint.getTextBounds(text, 0, text.length(), textRect);
                    float x = barRect[i].left + (barRect[i].width() / 2) - (textRect.width() / 1.2f);
                    float y = barRect[i].top+textRect.height()+axisTextMargin;
                    canvas.drawText(text, x, y, numberOnePaint);
                } else{
                    canvas.drawRect(targetRect, getPaint(bgColor));
                }
            }
        }
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < dataList.size(); i++) {
            BarGraphModel barGraphModel = dataList.get(i);
            String text;
            if (i == 0) {
                text = "~" + barGraphModel.getXText();
            } else if (i == dataList.size() - 1) {
                text = barGraphModel.getXText() + "~";
            } else {
                text = barGraphModel.getXText();
            }

            Rect textRect = new Rect();
            getPaint(0).getTextBounds(text, 0, text.length(), textRect);

            float x = barRect[i].left + (barRect[i].width() / 2) - (textRect.width() / 2);
            float y = targetRect.bottom;
            canvas.drawText(text, x, y, getPaint(0xff6a6a6a));
        }
    }

    private void drawLine(Canvas canvas) {
        float lineMargin = graphRect.height() / (LINE_COUNT - 2 + 1);
        for(int i = 0 ; i < LINE_COUNT ; i++){
            if(i == 0){
                canvas.drawLine(targetRect.left, targetRect.top, targetRect.right, targetRect.top, getPaint(0xffdadada));
            } else if(i == LINE_COUNT - 1){
                canvas.drawLine(targetRect.left, graphRect.bottom, targetRect.right, graphRect.bottom, getPaint(0xff6a6a6a));
            } else{
                float y = targetRect.top + lineMargin*i;
                canvas.drawLine(targetRect.left, y, targetRect.right, y, getPaint(0xffdadada));
            }
        }
    }
}
