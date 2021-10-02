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
    public Bitmap n0_;
    public Bitmap n1_;
    public Bitmap n2_;
    public Bitmap n3_;
    public Bitmap n4_;
    public Bitmap n5_;
    public Bitmap n6_;
    public Bitmap n7_;
    public Bitmap n8_;
    public Bitmap n9_;
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

        n0_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n0);
        n1_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n1);
        n2_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n2);
        n3_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n3);
        n4_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n4);
        n5_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n5);
        n6_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n6);
        n7_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n7);
        n8_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n8);
        n9_ = BitmapFactory.decodeResource(getResources(), emperatriz.pypots.common.R.drawable.n9);

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
        if (!Sys.getBoolean(Sys.SETTINGS_NUMEROS_SOMBREADOS,false,context)) {
            DrawUtils.drawTime(sdf.format(now),n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,p2);
        }
        else{
            DrawUtils.drawTime(sdf.format(now),n0_,n1_,n2_,n3_,n4_,n5_,n6_,n7_,n8_,n9_,p2);
        }


        int divs = Sys.getInt(Sys.SETTINGS_DIVISIONES,2, context);
        if (divs<=3){
            DrawUtils.drawSecondsMulti(now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND), divs);
        }
        else if(divs<=7){
            DrawUtils.drawSecondsThin(now.get(Calendar.SECOND)*2+(now.get(Calendar.MILLISECOND)>500?1:0), now.get(Calendar.MILLISECOND), divs-4);
        }
        else if (divs==8){
            DrawUtils.drawSpin(0xadffffff,4, 1, 0.62f, true, true);
            DrawUtils.drawSpin(0x99ffffff,6, 2, 0.58f, false, true);
            DrawUtils.drawSpin(0x67ffffff,9, 3, 0.79f, true, true);
            DrawUtils.drawSecondsSpin(0xffffffff, 45,now.getTimeInMillis());
        }
        else if (divs==9){
            DrawUtils.drawSeconds2(0xffffffff, now.get(Calendar.SECOND), now.get(Calendar.MILLISECOND));
        }

        if (Sys.getBoolean(Sys.SETTINGS_HALO,true, context)) {
            DrawUtils.drawHalo(halo);
        }


        DrawUtils.drawDayTimes(0xffeeaa22, "07:30","14:00", "21:30","03:00",now, p2, !Sys.getBoolean(Sys.SETTINGS_DISCRETO,false, context));
        DrawUtils.drawLeftComplication(0xffff4466, 56,getResources().getString(R.string.reloj),"56", p2, !Sys.getBoolean(Sys.SETTINGS_DISCRETO,false, context));

        if (Sys.getBoolean(Sys.SETTINGS_PASOS,false, context)){
            DrawUtils.drawRightComplication(0xff00bbee, 36,getResources().getString(R.string.pasos), "3617", p2, !Sys.getBoolean(Sys.SETTINGS_DISCRETO,false, context));
        }
        else{
            DrawUtils.drawRightComplication(0xff00bbee, 84,getResources().getString(R.string.movil), "84", p2, !Sys.getBoolean(Sys.SETTINGS_DISCRETO,false, context));
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
