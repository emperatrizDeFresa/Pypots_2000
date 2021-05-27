package emperatriz.pypots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import emperatriz.pypots.common.DrawUtils;
import emperatriz.pypots.common.Sys;

import static emperatriz.pypots.common.DrawUtils.canvas;

public class CanvasView  extends View {

    public int width=454;
    public int height=454;

    Context context;
    Paint timePaint,restPaint,mBackgroundPaint;
    MainActivity ma;
    Bitmap swr;

    public Bitmap n0;
    public Bitmap n1;
    public Bitmap n2;
    public Bitmap n3;
    public Bitmap n4;
    public Bitmap n5;
    public Bitmap n6;
    public Bitmap n7;
    public Bitmap n8;
    public Bitmap n9;
    public Bitmap by;
    public Bitmap halo;
    Paint p2;
    Typeface font2;
    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;



        swr = BitmapFactory.decodeResource(getResources(), R.drawable.swr);
        swr =  Bitmap.createScaledBitmap(swr,598,768, true);

        font2 = Typeface.createFromAsset(c.getAssets(), "fonts/Square.ttf");

        p2 = new Paint();
        p2.setTypeface(font2);


        n0 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.cero);
        n1 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.uno);
        n2 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.dos);
        n3 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.tres);
        n4 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.cuatro);
        n5 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.cinco);
        n6 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.seis);
        n7 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.siete);
        n8 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.ocho);
        n9 = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.nueve);

        by = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.nonot);

        halo = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.halo);

        DrawUtils.offsetX=69;
        DrawUtils.offsetY=158;
        DrawUtils.height=454;
        DrawUtils.width=454;
        DrawUtils.p20=Math.round(454/20);

        update.run();
    }

    public void setMainActivity(MainActivity ma){
        this.ma=ma;
    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(598, 768);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable update = new Runnable(){
        public void run(){
            invalidate();
            handler.postDelayed(this,16);
        }
    };

    public int notifications = 0;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawUtils.canvas=canvas;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar now = Calendar.getInstance();

        canvas.drawBitmap(swr,0,0,null);
        DrawUtils.drawTime(sdf.format(now.getTime()),n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,p2);

        //DrawUtils.drawSeconds(0xffffffff, now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND), halo);
        DrawUtils.drawSecondsMulti(now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND), Sys.getInt(Sys.SETTINGS_DIVISIONES,2,context), halo);
        DrawUtils.drawDayTimes(0xffeeaa22, "07:30","14:00", "21:30","03:00",now, p2);
        DrawUtils.drawLeftComplication(0xffff4466, 56,getResources().getString(R.string.reloj),"56", p2);

        if (Sys.getBoolean(Sys.SETTINGS_PASOS,false, context)){
            DrawUtils.drawRightComplication(0xff00bbee, 36,getResources().getString(R.string.pasos), "3617", p2);
        }
        else{
            DrawUtils.drawRightComplication(0xff00bbee, 84,getResources().getString(R.string.movil), "84", p2);
        }



        DrawUtils.drawDate(now,  font2);
        if (notifications>0){
            DrawUtils.drawNotifications(Math.min(notifications,10),  Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES,false, context), font2, now.getTimeInMillis());
        }
        else{
            DrawUtils.drawNoNotifications(by,p2);
        }


    }

    public void clearCanvas() {
        invalidate();
    }

    public void incrementNotification(){
        notifications = (notifications+1)%11;
    }



}
