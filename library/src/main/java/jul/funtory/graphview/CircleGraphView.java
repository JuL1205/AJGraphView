package jul.funtory.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

import jul.funtory.graphview.model.CircleGraphModel;


/**
 * Created by JuL on 2017. 3. 2..
 */

public class CircleGraphView extends AbsGraphView {
    private final int ANIM_THRESHOLD = 3;

    private final int START_DEGREE = -90;

    private PointF centerCoord = new PointF();
    private RectF targetRect;
    private float radius;   //반지름

    private int graphMargin;

    private List<CircleGraphModel> dataList;

    private float[] sweepDegree;        //애니메이션을 위한 degree 값.valueDegree에 매핑됨.
    private float[] dstDegree;        //각 arc의 degree 값

    public CircleGraphView(Context context) {
        super(context);

        init();
    }

    public CircleGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CircleGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        graphMargin = getResources().getDimensionPixelOffset(R.dimen.graph_margin);
    }

    public void setData(List<CircleGraphModel> datas) {
        dataList = datas;

        //각 영역 각도 계산
        float total = 0;
        dstDegree = new float[dataList.size()];
        for (CircleGraphModel circleGraphModel : dataList) {
            total += circleGraphModel.getValue();
        }
        for (int i = 0; i < dataList.size(); i++) {
            dstDegree[i] = 360 * (dataList.get(i).getValue() / total);
        }

        //애니메이션을 위한 각도 변수
        sweepDegree = new float[dstDegree.length];

        startDraw();
    }

//    private void forceFinishAnim(){
//        if(dstDegree != null){
//            sweepDegree = dstDegree.clone();
//        }
//    }

    @Override
    protected boolean onDrawGraph(Canvas canvas) {
        if (dataList == null || dataList.size() == 0) {
            return true;
        }

        drawArc(canvas);

        drawDivider(canvas);

        drawText(canvas);

        drawCenter(canvas);

        return Arrays.equals(sweepDegree, dstDegree);
    }

    @Override
    protected void resetDependOnViewSize() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if (viewWidth == 0 || viewHeight == 0) {
            return;
        }

        centerCoord.x = viewWidth / 2.f;
        centerCoord.y = viewHeight / 2.f;

        int aspectSize = Math.min(viewWidth, viewHeight) - (graphMargin * 2);

        float left = (viewWidth - aspectSize) / 2.f;
        float top = (viewHeight - aspectSize) / 2.f;
        targetRect = new RectF(left, top, left + aspectSize, top + aspectSize);

        radius = targetRect.width() / 2.f;

        //애니메이션을 위한 각도 변수
        sweepDegree = new float[dstDegree.length];
    }


    private void drawArc(Canvas canvas) {
        float startDegree = START_DEGREE;
        for (int i = 0; i < dstDegree.length; i++) {
            if (i > 0) {
                startDegree += dstDegree[i - 1];
            }

            sweepDegree[i] += ANIM_THRESHOLD;
            if (sweepDegree[i] > dstDegree[i]) {
                sweepDegree[i] = dstDegree[i];
            }

            canvas.drawArc(targetRect, startDegree, sweepDegree[i], true, getPaint(dataList.get(i).getColor()));
        }
    }

    private void drawText(Canvas canvas) {
        float preDegree = 0;
        for (int i = 0; i < dataList.size(); i++) {
            float radAngle = degreeToRadian(preDegree + (dstDegree[i] / 2) + START_DEGREE);
            preDegree += dstDegree[i];

            int curValue = (int) (dataList.get(i).getValue() * (sweepDegree[i] / dstDegree[i]));

            String text = (curValue) + "%";
            Paint paint = getPaint(Color.WHITE);
            PointF rotateDot = getTextPoint(radAngle, paint, text);
            canvas.drawText(text, rotateDot.x, rotateDot.y, getPaint(Color.WHITE));
        }
    }

    private PointF getTextPoint(float radAngle, Paint textPaint, String rateText) {
        PointF targetPoint = new PointF();
        Rect rect = new Rect();
        textPaint.getTextBounds(rateText, 0, rateText.length(), rect);
        targetPoint.x = (float) (centerCoord.x + radius / 1.35f * Math.cos(radAngle) - rect.width() / 2);
        targetPoint.y = (float) (centerCoord.y + radius / 1.35f * Math.sin(radAngle) + rect.height() / 2);
        return targetPoint;
    }

    private PointF getLineRotatePoint(float degree) {
        PointF targetPoint = new PointF();
        targetPoint.x = (float) (centerCoord.x + radius * Math.cos(degreeToRadian(degree)));
        targetPoint.y = (float) (centerCoord.y + radius * Math.sin(degreeToRadian(degree)));
        return targetPoint;
    }

    private float degreeToRadian(float angle) {
        return (float) (angle * Math.PI / 180);
    }

    private void drawDivider(Canvas canvas) {
        float targetDegree = 0;
        for (float degree : dstDegree) {
            targetDegree += degree;
            PointF rotateDot = getLineRotatePoint(targetDegree + START_DEGREE);
            canvas.drawLine(centerCoord.x, centerCoord.y, rotateDot.x, rotateDot.y, getPaint(Color.WHITE));
        }
    }

    private void drawCenter(Canvas canvas) {
//            if (bg != null) {
//                Bitmap mask = makeDst();
//
//                Canvas c = new Canvas(bitmap);
//                c.drawBitmap(bg, 0, 0, null);
//
//                Paint paint = new Paint();
//                paint.setFilterBitmap(false);
//                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//
//                c.drawBitmap(mask, 0, 0, paint);
//
//                canvas.drawBitmap(bitmap, 0, 0, null);
//            }

        if (canvas != null) {
            Paint paint = getPaint(Color.WHITE);
            canvas.drawCircle(centerCoord.x, centerCoord.y, radius / 2, paint);
        }
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true); //text anti alias
        paint.setColor(color);
        paint.setStrokeWidth(20);
        paint.setTextSize(radius / 5);
        return paint;
    }

}
