package jul.funtory.graphview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

/**
 * Created by JuL on 2017. 3. 6..
 */

public abstract class AbsGraphView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private SurfaceHolder holder;

    public AbsGraphView(Context context) {
        super(context);

        init();
    }

    public AbsGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AbsGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resetDependOnViewSize();
        startDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDraw();
    }

    private void stopDraw() {
        if (drawThread != null) {
            drawThread.finish();
            drawThread = null;
        }
    }

    protected void startDraw() {
        stopDraw();

        drawThread = new DrawThread();
        drawThread.start();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        stopDraw();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resetDependOnViewSize();

                startDraw();
            }
        });
    }

    /**
     * 모든 그래프가 그려졌다 판달 될 때까지 계속 호출 됨.
     * @return 다 그려졌으면 true. 그렇지 않으면 false.
     */
    protected abstract boolean onDrawGraph(Canvas canvas);
    protected abstract void resetDependOnViewSize();

    class DrawThread extends Thread {
        private boolean isDrawing = true;

        DrawThread() {
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (isDrawing && getContext() != null) {
                Log.d("test", "DrawThread onDraw");
                try {
                    canvas = holder.lockCanvas();
                    if(canvas == null){
                        finish();
                        continue;
                    }

                    canvas.drawColor(Color.WHITE);

                    synchronized (holder) {
                        if(onDrawGraph(canvas)){
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }

            Log.e("test", "DrawThread finish");
        }

        void finish() {
            isDrawing = false;
        }
    }
}
