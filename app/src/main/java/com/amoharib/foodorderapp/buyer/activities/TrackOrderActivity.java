package com.amoharib.foodorderapp.buyer.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amoharib.foodorderapp.interfaces.DataListeners;
import com.amoharib.foodorderapp.models.Restaurant;
import com.amoharib.foodorderapp.R;
import com.amoharib.foodorderapp.services.DataService;
import com.amoharib.foodorderapp.utils.OrderStatus;
import com.amoharib.foodorderapp.utils.VectorDrawableUtils;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrackOrderActivity extends AppCompatActivity implements DataListeners.OrderStatusListener {


    private TextView restName;
    private TimelineView sentTimeLine;
    private TimelineView seenTimeLine;
    private TimelineView onTheWayTimeLine;
    private CircleProgressBar timer;
    private TextView timerText;
    private String restaurantId;
    private String orderId;
    private Restaurant rest;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        restaurantId = getIntent().getStringExtra("restaurant");
        orderId = getIntent().getStringExtra("orderId");

        initView();

        DataService.getInstance().getRestaurant(restaurantId, new DataListeners.RestaurantListener() {
            @Override
            public void onRestaurantRetrieved(Restaurant restaurant) {
                if (restaurant != null) {
                    rest = restaurant;
                    initCustomViews();

                    new CountDownTimer(45 * 60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            timerText.setText(timeFormat.format(new Date(millisUntilFinished)));
                            Integer timeLeft = 100 - ((int) ((double) millisUntilFinished / ((double) 45 * 60000) * 100));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(timeLeft, true);
                            } else {
                                timer.setProgress(timeLeft);
                            }
                        }

                        @Override
                        public void onFinish() {
                            timerText.setText(timeFormat.format(new Date(0)));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timer.setProgress(100, true);
                            } else {
                                timer.setProgress(100);
                            }

                        }
                    }.start();
                }
            }
        });
    }

    private void initCustomViews() {
        restName.setText(rest.getTitle());
        DataService.getInstance().addOnOrderStatusChanged(orderId, this);
    }

    private void initView() {
        restName = (TextView) findViewById(R.id.restName);
        sentTimeLine = (TimelineView) findViewById(R.id.sentTimeLine);
        seenTimeLine = (TimelineView) findViewById(R.id.seenTimeLine);
        onTheWayTimeLine = (TimelineView) findViewById(R.id.onTheWayTimeLine);
        timer = (CircleProgressBar) findViewById(R.id.timer);
        timerText = (TextView) findViewById(R.id.timerText);


        sentTimeLine.initLine(TimelineView.getTimeLineViewType(0, 3));
        onTheWayTimeLine.initLine(TimelineView.getTimeLineViewType(2, 3));

    }

    @Override
    public void onStatusChanged(OrderStatus status) {
        changeStatus(status);
    }

    private void changeStatus(OrderStatus status) {
        switch (status) {
            case SENT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_empty_circle));
                break;
            case SEEN:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_dotted_circle));
                break;
            case OUT:
                sentTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                seenTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                onTheWayTimeLine.setMarker(VectorDrawableUtils.getDrawable(this, R.drawable.ic_done));
                break;
        }

        refreshStatusView();
    }

    private void refreshStatusView() {
        sentTimeLine.invalidate();
        seenTimeLine.invalidate();
        onTheWayTimeLine.invalidate();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
        finish();
    }
}
