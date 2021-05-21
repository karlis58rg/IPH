package mx.ssp.iph.utilidades.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class Modelo_ContenedorFirma extends View {
    float x = 50;
    float y = 50;
    String accion = "accion";
    Path path = new Path();

    public Modelo_ContenedorFirma(Context context)
    {
        super(context);
    }

    public void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);

        int ancho = canvas.getWidth();

        if (accion== "down")
            path.moveTo(x, y);

        if(accion == "move")
            path.lineTo(x, y);

        if(accion == "limpiar")
            path.reset();

        canvas.drawPath(path, paint);
    }
    public boolean limpiarcanvas(){
        accion = "limpiar";
        invalidate();
        return true;
    }

    public boolean onTouchEvent(MotionEvent e){
        x = e.getX();
        y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN)
            accion="down";

        if (e.getAction() == MotionEvent.ACTION_MOVE)
            accion="move";

        invalidate();
        return true;
    }
}
