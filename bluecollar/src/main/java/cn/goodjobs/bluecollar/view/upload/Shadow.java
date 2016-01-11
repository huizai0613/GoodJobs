package cn.goodjobs.bluecollar.view.upload;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/12/7 0007.
 */
public class Shadow extends View {

    private int progress;

    public Shadow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Shadow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#88000000"));
        canvas.drawRect(0, 0, getWidth(), shadowHeight(), paint);
    }

    private int shadowHeight() {
        return getHeight() - progress * getHeight() / 100 ;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
