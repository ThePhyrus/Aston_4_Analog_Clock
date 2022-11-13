package roman.bannikov.aston_4_analog_clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class AnalogClockView extends View {

    public AnalogClockView(Context context) {
        super(context);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;
    private Paint paintCircle;
    private Paint paintCenterOfCircle;
    private int radius;
    private int screenWidth;
    private int screenHeight;
    private Calendar mCalendar;
    private static final int NEED_INVALIDATE = 0x23;

    private final Handler mHandler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == NEED_INVALIDATE) {
                mCalendar = Calendar.getInstance();
                invalidate();
                mHandler.sendEmptyMessageDelayed(NEED_INVALIDATE, 1000);
            }
        }
    };


    protected void onDraw(Canvas canvas) {
        init();
        drawCircle(canvas);
        drawTimePoints(canvas);
        drawLine(canvas);
    }


    private void drawLine(Canvas canvas) {  // Рисует стрелки и двигает

        paintHour.setStrokeWidth(radius / 16f);    // часовая ширина
        paintHour.setColor(Color.BLACK);           // часовая цвет
        paintMinute.setStrokeWidth(radius / 20f);  // минутная ширина
        paintMinute.setColor(Color.RED);             // минутная цвет
        paintSecond.setStrokeWidth(radius / 30f);  // секундная ширина
        paintSecond.setColor(Color.BLUE);          // секундная цвет

        // Получить системное время
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);

        // Углы поворота стрелок
        float hourDegrees = (hour / 12f * 360) + (minute / 60f * 30) + 180;
        float minDegrees = (minute / 60f * 360) + 180;
        float secDegrees = (second / 60f * 360) + 180;

        // Движение часов
        // Переместить начальные координаты холста в центр круга
        canvas.drawCircle(screenWidth / 2f, screenHeight / 2f, 1, paintCenterOfCircle);

        // Нарисовать часовую стрелку
        canvas.save();
        canvas.rotate(hourDegrees, screenWidth / 2f, screenHeight / 2f);
        canvas.drawLine(
                screenWidth / 2f,
                screenHeight / 2f - radius / 2.75f,
                screenWidth / 2f,
                screenHeight / 2f + radius / 1.5f,
                paintHour);
        canvas.restore();

        // Нарисовать минутную стрелку
        canvas.save();
        canvas.rotate(minDegrees, screenWidth / 2f, screenHeight / 2f);
        canvas.drawLine(
                screenWidth / 2f,
                screenHeight / 2f - radius / 4f,
                screenWidth / 2f,
                screenHeight / 2f + radius / 2f,
                paintMinute);
        canvas.restore();

        // Нарисовать секундную стрелку
        canvas.save();
        canvas.rotate(secDegrees, screenWidth / 2f, screenHeight / 2f);
        canvas.drawLine(
                screenWidth / 2f,
                screenHeight / 2f - radius / 6f,
                screenWidth / 2f,
                screenHeight / 2f + radius / 2.75f,
                paintSecond);
        canvas.restore();

    }


    private void drawTimePoints(Canvas canvas) { //отметки на циферблате
        for (int i = 0; i < 60; i++) { // Установить начальную позицию отметок
            if (i % 5 == 0) {
                canvas.drawLine(
                        screenWidth / 2f,
                        screenHeight / 2f - radius,
                        screenWidth / 2f,
                        screenHeight / 2f - radius / 1.15f,
                        paintCircle);
            }
            // поворот на ТРИ градуса
            canvas.rotate(6, screenWidth / 2f, screenHeight / 2f);
        }
        canvas.save();
    }


    private void init() {
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        paintCircle = new Paint();
        paintHour = new Paint();
        paintMinute = new Paint();
        paintSecond = new Paint();
        paintCenterOfCircle = new Paint();
        mCalendar = Calendar.getInstance();
        radius = (Math.min(screenWidth, screenHeight)) / 2;
        radius = (int) (radius / 1.1f);
        mHandler.sendEmptyMessage(NEED_INVALIDATE); // для отслеживания секундной стрелки
    }


    private void drawCircle(Canvas canvas) {
        paintCircle.reset();
        paintCircle.setColor(Color.BLACK);
        paintCircle.setStrokeWidth(radius / 16f);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        canvas.drawCircle(screenWidth / 2f, screenHeight / 2f, radius, paintCircle);
    }


}