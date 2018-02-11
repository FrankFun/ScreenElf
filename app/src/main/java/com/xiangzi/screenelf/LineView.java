package com.xiangzi.screenelf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

/**
 * LineView
 *
 * @author wuyongxiang
 *         2018/2/5
 */

public class LineView extends View {

    PointF controlPoint, leftPoint, rightPoint;
    Paint paintArc = new Paint();
    Paint paintLine = new Paint();
    Path linePath = new Path();

    public LineView(Context context) {
        super(context);
        controlPoint = new PointF(getMeasuredWidth() * 0.5f, 0);
        leftPoint = new PointF(getMeasuredWidth() * 0.1f, 0);
        rightPoint = new PointF((getMeasuredWidth() * 0.9f), 0);
        paintArc.setColor(getResources().getColor(R.color.colorAccent));
        paintArc.setStyle(Paint.Style.STROKE);
        paintArc.setStrokeWidth(20);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(20);
    }


    public void setControlPoint(float x, float y) {
        this.controlPoint.x = x * 2;
        this.controlPoint.y = y * 2;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        leftPoint.x = getMeasuredWidth() * 0.3f;
        rightPoint.x = getMeasuredWidth() * 0.7f;
        if (controlPoint.x == 0 || controlPoint.y < 30) {
            controlPoint.y = 30;
            controlPoint.x = getMeasuredWidth() * 0.5f;
        }
        canvas.drawCircle(leftPoint.x, leftPoint.y + 30, 20, paintArc);
        canvas.drawCircle(rightPoint.x, rightPoint.y + 30, 20, paintArc);
        linePath.reset();
        linePath.moveTo(leftPoint.x, leftPoint.y + 30);
        linePath.quadTo(controlPoint.x, controlPoint.y + 30, rightPoint.x, rightPoint.y + 30);
        canvas.drawPath(linePath, paintLine);

    }
}
