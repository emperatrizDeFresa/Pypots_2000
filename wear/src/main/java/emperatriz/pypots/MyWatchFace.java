package emperatriz.pypots;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import org.shredzone.commons.suncalc.SunTimes;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import emperatriz.pypots.common.DrawUtils;
import emperatriz.pypots.common.Sys;

import static emperatriz.pypots.common.DrawUtils.height;
import static emperatriz.pypots.common.DrawUtils.width;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't
 * shown. On devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient
 * mode. The watch face is drawn with less contrast in mute mode.
 * <p>
 * Important Note: Because watch face apps do not have a default Activity in
 * their project, you will need to set your Configurations to
 * "Do not launch Activity" for both the Wear and/or Application modules. If you
 * are unsure how to do this, please review the "Run Starter project" section
 * in the Google Watch Face Code Lab:
 * https://codelabs.developers.google.com/codelabs/watchface/index.html#0
 */
public class MyWatchFace extends CanvasWatchFaceService implements
        MessageClient.OnMessageReceivedListener, SensorEventListener
{
    Typeface font1,font2;

    int steps=0;
    int todaySteps=0;
    int mTapCount=0;
    /*
     * Updates rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = 1;//TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

//    public class Engine extends CanvasWatchFaceService.Engine {
//        Engine() {
//            // Sets useHardwareCanvas to true to request hardware acceleration
//            super(true);
//        }
//    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Sys.BATTERY_KEY)) {
            final String message = new String(messageEvent.getData());
            Sys.save("battery",message,this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps =  (int)event.values[0];
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

            if (!Sys.getString("lastSteps","",getApplicationContext()).equals(sdf.format(new Date()))){
                Sys.save("lastSteps", sdf.format(new Date()), MyWatchFace.this);
                todaySteps=steps-1;
                todaySteps = Math.max(todaySteps,0);
                Sys.save("todaySteps", todaySteps, MyWatchFace.this);
            }
            Sys.save("steps", steps, MyWatchFace.this);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static class EngineHandler extends Handler {
        private final WeakReference<MyWatchFace.Engine> mWeakReference;

        public EngineHandler(MyWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            MyWatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        /* Handler to update the time once a second in interactive mode. */
        private final Handler mUpdateTimeHandler = new EngineHandler(this);
        private Calendar mCalendar;
        private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };
        private boolean mRegisteredTimeZoneReceiver = false;
        private boolean mMuteMode;
        private Paint mBackgroundPaint;
        private boolean mAmbient;

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

        Set<Node> nodes;


        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(MyWatchFace.this)
                    //.setStatusBarGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                    .setAcceptsTapEvents(true)
                    .build());

            mCalendar = Calendar.getInstance();

            font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Square.ttf");




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


            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(Color.BLACK);





        }





        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            mAmbient = inAmbientMode;

            /* Check and trigger whether or not timer should be running (only in active mode). */
            updateTimer();
        }



        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);
            boolean inMuteMode = (interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE);

            /* Dim display in mute mode. */
            if (mMuteMode != inMuteMode) {
                mMuteMode = inMuteMode;
                invalidate();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            DrawUtils.height=height;
            DrawUtils.width=width;
            DrawUtils.p20=Math.round(width/20);

        }



        /**
         * Captures tap event (and tap type). The {@link WatchFaceService#TAP_TYPE_TAP} case can be
         * used for implementing specific logic to handle the gesture.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    mTapCount++;
//                    Log.i("MCH Professional","Tap "+mTapCount+" "+x+","+y);
                    if (Calendar.getInstance().getTimeInMillis()-Sys.getLong("lastTap", 0,getApplicationContext())<200) {

                        if ((x-width/2)*(x-width/2) + (y-height/2)*(y-height/2) < (width*0.33)*(width*0.33) ){


                            if (Sys.getBoolean(Sys.SETTINGS_TORCH,false, getApplicationContext())){
                                //Toast.makeText(getApplicationContext(),"dentro",Toast.LENGTH_SHORT).show();
                                PackageManager pm = getPackageManager();
                                Intent intent = pm.getLaunchIntentForPackage("com.google.android.clockwork.flashlight");
                                if (intent!=null){
                                    startActivity(intent);
                                }

//                        Intent startMain = new Intent(Intent.ACTION_MAIN);
//                        startMain.addCategory(Intent.CATEGORY_HOME);
//                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(startMain);
                            }

                        }
                        else{
                            //Toast.makeText(getApplicationContext(),"fuera",Toast.LENGTH_SHORT).show();

                        }




                    }
                    Sys.save("lastTap",Calendar.getInstance().getTimeInMillis(),getApplicationContext());

                    break;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            long now = System.currentTimeMillis();
            mCalendar.setTimeInMillis(now);

            drawBackground(canvas);
            drawWatchFace(canvas);
        }

        private void drawBackground(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
        }

        private void drawWatchFace(Canvas canvas) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            DrawUtils.canvas=canvas;
            DrawUtils.isInAmbientMode=mAmbient;
            Paint p2 = new Paint();
            p2.setTypeface(font2);

            if (width!=454){
                n0 = Bitmap.createScaledBitmap(n0,Sys.size(62,width),Sys.size(62,width), true);
                n1 = Bitmap.createScaledBitmap(n1,Sys.size(62,width),Sys.size(62,width), true);
                n2 = Bitmap.createScaledBitmap(n2,Sys.size(62,width),Sys.size(62,width), true);
                n3 = Bitmap.createScaledBitmap(n3,Sys.size(62,width),Sys.size(62,width), true);
                n4 = Bitmap.createScaledBitmap(n4,Sys.size(62,width),Sys.size(62,width), true);
                n5 = Bitmap.createScaledBitmap(n5,Sys.size(62,width),Sys.size(62,width), true);
                n6 = Bitmap.createScaledBitmap(n6,Sys.size(62,width),Sys.size(62,width), true);
                n7 = Bitmap.createScaledBitmap(n7,Sys.size(62,width),Sys.size(62,width), true);
                n8 = Bitmap.createScaledBitmap(n8,Sys.size(62,width),Sys.size(62,width), true);
                n9 = Bitmap.createScaledBitmap(n9,Sys.size(62,width),Sys.size(62,width), true);
                n9 = Bitmap.createScaledBitmap(n9,Sys.size(62,width),Sys.size(62,width), true);
                by = Bitmap.createScaledBitmap(by,Sys.size(114,width),Sys.size(27,width), true);
                halo = Bitmap.createScaledBitmap(halo,width,width, true);
            }

            NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int status = nm.getCurrentInterruptionFilter();
            if (status!=Sys.getInt("dndStatus",0, getApplicationContext())){
                Sys.save("dndStatus", status, getApplicationContext());
                if (Sys.getBoolean(Sys.SETTINGS_DND,false, getApplicationContext())) {
                    syncDND();
                }
            }

            pollPhoneBattery(false);
            pollPhoneLocation(false);

            int notifications = Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS,false, getApplicationContext()) ? getUnreadCount() : getNotificationCount();

            double lat = 42.3d;
            double lon = -8.41d;

            try{
                String location = Sys.getString(Sys.LOCATION_KEY, "42.3,-8,41", getApplicationContext());
                lat = Double.parseDouble(location.split(",")[0]);
                lon = Double.parseDouble(location.split(",")[1]);
            }catch (Exception ex){

            }

            SunTimes times = SunTimes.compute().today().at(lat, lon).execute();

            Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scaleB = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int watchBattery = Math.round(level*100 / (float)scaleB);
            int phoneBattery = Sys.getInt(Sys.BATTERY_KEY, 0, getApplicationContext());

            todaySteps = Sys.getInt("todaySteps", 0, MyWatchFace.this);
            steps = Sys.getInt("steps", 0, MyWatchFace.this);
            if (steps-todaySteps<0){
                Sys.save("todaySteps", steps, MyWatchFace.this);
                todaySteps=steps;
            }

            DrawUtils.drawTime(sdf.format(mCalendar.getTime()),n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,p2);
            //DrawUtils.drawSeconds(0xffffffff, mCalendar.get(Calendar.SECOND), mCalendar.get(Calendar.MILLISECOND), halo);
            DrawUtils.drawSecondsMulti(mCalendar.get(Calendar.SECOND), mCalendar.get(Calendar.MILLISECOND), Sys.getInt(Sys.SETTINGS_DIVISIONES,2,getApplicationContext()), halo);
//            DrawUtils.drawSpin(0xadffffff,7, 3, 0.55f, true, true);
//            DrawUtils.drawSpin(0x99ffffff,9, 2, 0.40f, false, true);
//            DrawUtils.drawSpin(0x67ffffff,13, 1, 0.60f, true, true);
//            DrawUtils.drawSecondsSpin(0xffffffff, 45,mCalendar.getTimeInMillis(), halo );

            DrawUtils.drawDayTimes(0xffeeaa22, sdf.format(times.getRise()),sdf.format(times.getNoon()), sdf.format(times.getSet()), sdf.format(times.getNadir()),mCalendar, p2);
            DrawUtils.drawLeftComplication(0xffff4466, watchBattery,getResources().getString(R.string.reloj),watchBattery+"", p2);
            if (Sys.getBoolean(Sys.SETTINGS_PASOS,false, getApplicationContext())){
                DrawUtils.drawRightComplication(0xff00bbee, Math.min(Math.round((steps-todaySteps)/100f),100),getResources().getString(R.string.pasos), (steps-todaySteps)+"", p2);
            }
            else{
                DrawUtils.drawRightComplication(0xff00bbee, phoneBattery,getResources().getString(R.string.movil), phoneBattery+"", p2);
            }
            DrawUtils.drawDate(mCalendar,  font2);
            if (notifications>0){
                DrawUtils.drawNotifications(Math.min(notifications,10),  Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES,false, getApplicationContext()), font2, mCalendar.getTimeInMillis());
            }
            else{
                DrawUtils.drawNoNotifications(by,p2);
            }




        }

        private void pollPhoneBattery(boolean force) {
            long nextPoll = Long.parseLong(Sys.getString("nextPoll","0",getApplicationContext()));
            boolean canPoll = Calendar.getInstance().getTimeInMillis()>nextPoll;
            if (canPoll || force){
                Sys.save("nextPoll", (Calendar.getInstance().getTimeInMillis()+Sys.POLLING_INTERVAL*60*1000)+"",getApplicationContext());
                new SendMessage(Sys.BATTERY_KEY,"").start();
            }
        }

        private void pollPhoneLocation(boolean force) {
            long nextLoc = Long.parseLong(Sys.getString("nextLoc","0",getApplicationContext()));
            boolean canPoll = Calendar.getInstance().getTimeInMillis()>nextLoc;
            if (canPoll || force){
                Sys.save("nextLoc", (Calendar.getInstance().getTimeInMillis()+Sys.LOCATION_INTERVAL*60*1000)+"",getApplicationContext());
                new SendMessage(Sys.LOCATION_KEY,"").start();
            }
        }

        private void syncDND() {
            try{
                NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                int status = nm.getCurrentInterruptionFilter();
                new SendMessage(Sys.DND_KEY,status+"").start();
            }
            catch (SecurityException se){

            }

        }



        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                /* Update time zone in case it changed while we weren't visible. */
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            } else {
                unregisterReceiver();
            }

            /* Check and trigger whether or not timer should be running (only in active mode). */
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            MyWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            MyWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts/stops the {@link #mUpdateTimeHandler} timer based on the state of the watch face.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer
         * should only run in active mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !mAmbient;
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }



        class SendMessage extends Thread {
            String path;
            String message;

            SendMessage(String p, String m) {
                path = p;
                message = m;
            }

            public void run() {
                Task<List<Node>> nodeListTask = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
                try {
                    List<Node> nodes = Tasks.await(nodeListTask);
                    for (Node node : nodes) {
                        Task<Integer> sendMessageTask = Wearable.getMessageClient(getApplicationContext()).sendMessage(node.getId(), path, message.getBytes());
                        try {
                            Integer result = Tasks.await(sendMessageTask);
                        } catch (ExecutionException exception) {

                        } catch (InterruptedException exception) {

                        }

                    }

                } catch (ExecutionException exception) {

                } catch (InterruptedException exception) {

                }
            }
        }


    }
}
