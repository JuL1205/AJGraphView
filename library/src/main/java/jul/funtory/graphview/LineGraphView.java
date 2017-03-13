package jul.funtory.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

import jul.funtory.graphview.model.LineGraphModel;


/**
 * Created by JuL on 2017. 3. 7..
 */

public class LineGraphView extends AbsGraphView {
    private int ANIM_THRESHOLD;
    private final static int LINE_COUNT = 6;

    private Drawable pointDrawable;     //각 포인트 똥그래미
    private Drawable pointSelectedDrawable;     //강조 포인트 똥그래미
    private Point[] pointCoord;    //각 포인트의 x,y 좌표들
    private Point[] animCoord;  //anim을 위한 좌표들

    private int animDashYCoord;

    private Point[] avgPointCoord;    //평균 값 각 포인트의 x,y 좌표들
    private Point[] avgAnimCoord;    //평균 값 anim을 위한 좌표들

    private List<LineGraphModel> dataList;
    private RectF targetRect;
    private RectF graphRect;

    private int pointRadius;
    private int pointSelectedRadius;

    private int graphMargin;
    private int axisTextMargin;
    private int graphPadding;
    private int lineWidth;

    public LineGraphView(Context context) {
        super(context);

        init();
    }

    public LineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LineGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        pointDrawable = getResources().getDrawable(R.drawable.default_shape_line_point);
        pointSelectedDrawable = getResources().getDrawable(R.drawable.default_shape_line_point_selected);

        graphMargin = getResources().getDimensionPixelOffset(R.dimen.graph_margin);
        axisTextMargin = getResources().getDimensionPixelOffset(R.dimen.axis_text_margin);
        graphPadding = getResources().getDimensionPixelOffset(R.dimen.line_graph_padding);
        lineWidth = getResources().getDimensionPixelOffset(R.dimen.line_graph_width);


        pointRadius = getResources().getDimensionPixelOffset(R.dimen.line_graph_point_radius);
        pointSelectedRadius = getResources().getDimensionPixelOffset(R.dimen.line_graph_point_selected_radius);
    }

    public void setData(List<LineGraphModel> datas) {
        dataList = datas;

        resetDependOnViewSize();

        startDraw();
    }

    @Override
    protected boolean onDrawGraph(Canvas canvas) {
        if (pointCoord == null || pointCoord.length == 0) {
            return true;
        }

        //anim값 변경
        for (int i = 0; i < pointCoord.length; i++) {
            animCoord[i].y -= ANIM_THRESHOLD;

            if (animCoord[i].y < pointCoord[i].y) {
                animCoord[i].y = pointCoord[i].y;
            }
        }
        for (int i = 0; i < avgPointCoord.length; i++) {
            avgAnimCoord[i].y -= ANIM_THRESHOLD;

            if (avgAnimCoord[i].y < avgPointCoord[i].y) {
                avgAnimCoord[i].y = avgPointCoord[i].y;
            }
        }

        drawLine(canvas);

        drawAvgRegion(canvas);

        if (Arrays.equals(animCoord, pointCoord)) {
            animDashYCoord += ANIM_THRESHOLD;
            if (animDashYCoord > graphRect.bottom) {
                animDashYCoord = (int) graphRect.bottom;
            }
            drawDash(canvas);
        }

        drawText(canvas);

        drawPoint(canvas);

        drawCaption(canvas);

        return Arrays.equals(animCoord, pointCoord) && Arrays.equals(avgAnimCoord, avgPointCoord) && (animDashYCoord == (int) graphRect.bottom);
    }

    private void drawCaption(Canvas canvas) {
        String text = "최고 인기시간";

        Paint paint = getPaint(0xff000000);
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        canvas.drawText(text, graphRect.right - textRect.width(), graphRect.top + textRect.height(), paint);

        Point center = new Point((int)graphRect.right - textRect.width() - pointSelectedRadius, (int)graphRect.top + (textRect.height() / 2));
        pointSelectedDrawable.setBounds(center.x - pointSelectedRadius/2, center.y - +pointSelectedRadius/2, center.x + pointSelectedRadius/2, center.y + pointSelectedRadius/2);
        pointSelectedDrawable.draw(canvas);

    }

    private void drawAvgRegion(Canvas canvas) {
        Path path = new Path();

        path.moveTo(avgAnimCoord[0].x, graphRect.bottom);
        for (int i = 0; i < avgAnimCoord.length; i++) {
            path.lineTo(avgAnimCoord[i].x, avgAnimCoord[i].y);
        }
        path.lineTo(avgAnimCoord[avgAnimCoord.length - 1].x, graphRect.bottom);
        path.close();

        canvas.drawPath(path, getPaint(0x88cacaca));
    }

    private void drawLine(Canvas canvas) {
        //bg line
        float lineMargin = graphRect.height() / (LINE_COUNT - 2 + 1);
        for (int i = 0; i < LINE_COUNT; i++) {
            if (i == 0) {
                canvas.drawLine(targetRect.left, targetRect.top, targetRect.right, targetRect.top, getPaint(0xffdadada));
            } else if (i == LINE_COUNT - 1) {
                canvas.drawLine(targetRect.left, graphRect.bottom, targetRect.right, graphRect.bottom, getPaint(0xff6a6a6a));
            } else {
                float y = targetRect.top + lineMargin * i;
                canvas.drawLine(targetRect.left, y, targetRect.right, y, getPaint(0xffdadada));
            }
        }


        //point 간 path
        for (int i = 1; i < animCoord.length; i++) {
            Point preCoord = animCoord[i - 1];
            Point coord = animCoord[i];

            canvas.drawLine(preCoord.x, preCoord.y, coord.x, coord.y, getPaint(0xffadadad));
        }

    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < dataList.size(); i += 2) {
            LineGraphModel lineGraphModel = dataList.get(i);
            String text = lineGraphModel.getXText();

            Rect textRect = new Rect();
            getPaint(0).getTextBounds(text, 0, text.length(), textRect);

            float x = pointCoord[i].x - (textRect.width() / 2.f);
            float y = targetRect.bottom;
            canvas.drawText(text, x, y, getPaint(0xff6a6a6a));
        }
    }

    private void drawDash(Canvas canvas) {
        float max = getMaxValue();
        for (int i = 0; i < pointCoord.length; i++) {
            if (dataList.get(i).getValue() == max) {
                Paint dashPaint = getPaint(0xffff0000);
                dashPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                canvas.drawLine(pointCoord[i].x, pointCoord[i].y, pointCoord[i].x, animDashYCoord, dashPaint);
            }
        }
    }

    private void drawPoint(Canvas canvas) {
        float max = getMaxValue();
        for (int i = 0; i < animCoord.length; i++) {
            Point coord = animCoord[i];

            if (dataList.get(i).getValue() == max) {
                pointSelectedDrawable.setBounds(coord.x - pointSelectedRadius, coord.y - +pointSelectedRadius, coord.x + pointSelectedRadius, coord.y + pointSelectedRadius);
                pointSelectedDrawable.draw(canvas);
            } else {
                pointDrawable.setBounds(coord.x - pointRadius, coord.y - +pointRadius, coord.x + pointRadius, coord.y + pointRadius);
                pointDrawable.draw(canvas);
            }
        }
    }

    @Override
    protected void resetDependOnViewSize() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if (viewWidth == 0 || viewHeight == 0) {
            return;
        }

        targetRect = new RectF(graphMargin, graphMargin, viewWidth - graphMargin, viewHeight - graphMargin);

        if (dataList != null && dataList.size() > 0) {
            pointCoord = new Point[dataList.size()];
            avgPointCoord = new Point[dataList.size()];

            //텍스트 높이 계산
            Rect textRect = new Rect();
            getPaint(0).getTextBounds(dataList.get(0).getXText(), 0, dataList.get(0).getXText().length(), textRect);
            int textHeight = textRect.height();

            //텍스트영역을 제외한 그래프만의 영역 계산
            graphRect = new RectF(targetRect.left, targetRect.top, targetRect.right, targetRect.bottom - axisTextMargin - textHeight);

            //point 간 간격 계산
            float dataCount = (float) dataList.size();
            float pointMargin = (graphRect.width() - (graphPadding * 2)) / (dataCount - 1);


            //각 point 의 좌표 계산
            float pixelPerValue = (graphRect.height() - pointSelectedRadius) / (Math.max(getMaxValue(), getAvgMaxValue()) * 1.3f);  //값1당 y픽셀값 계산
            for (int i = 0; i < dataList.size(); i++) {
                int x = (int) ((i * pointMargin) + graphMargin + graphPadding);
                int y = (int) (graphRect.bottom - dataList.get(i).getValue() * pixelPerValue) - pointSelectedRadius;
                int avgY = (int) (graphRect.bottom - dataList.get(i).getAvgValue() * pixelPerValue) - pointSelectedRadius;
                pointCoord[i] = new Point(x, y);
                avgPointCoord[i] = new Point(x, avgY);
            }


            //anim 을 위한 값 세팅
            animCoord = new Point[pointCoord.length];
            for (int i = 0; i < pointCoord.length; i++) {
                animCoord[i] = new Point(pointCoord[i].x, (int) (graphRect.bottom - pointSelectedRadius));
            }
            avgAnimCoord = new Point[avgPointCoord.length];
            for (int i = 0; i < avgPointCoord.length; i++) {
                avgAnimCoord[i] = new Point(avgPointCoord[i].x, (int) (graphRect.bottom - pointSelectedRadius));
            }

            float max = getMaxValue();
            for (int i = 0; i < pointCoord.length; i++) {
                if (dataList.get(i).getValue() == max) {
                    animDashYCoord = pointCoord[i].y;
                }
            }

            ANIM_THRESHOLD = (int) (graphRect.height() / 50);
        }
    }

    private float getMaxValue() {
        float max = 0;

        if (dataList != null && dataList.size() > 0) {
            for (LineGraphModel lineGraphModel : dataList) {
                if (lineGraphModel.getValue() > max) {
                    max = lineGraphModel.getValue();
                }
            }
        }

        return max;
    }

    private float getAvgMaxValue() {
        float max = 0;

        if (dataList != null && dataList.size() > 0) {
            for (LineGraphModel lineGraphModel : dataList) {
                if (lineGraphModel.getAvgValue() > max) {
                    max = lineGraphModel.getAvgValue();
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
        paint.setStrokeWidth(lineWidth);
        paint.setTextSize(pointSelectedRadius * 2);
        return paint;
    }

}
