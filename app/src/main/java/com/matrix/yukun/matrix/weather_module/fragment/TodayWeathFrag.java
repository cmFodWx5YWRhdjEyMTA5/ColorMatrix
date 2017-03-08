package com.matrix.yukun.matrix.weather_module.fragment;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.anims.MyEvaluator;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.weather_module.bean.EventDay;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.present.TodayFragmentImpl;
import com.matrix.yukun.matrix.weather_module.present.TodayPresent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-3-6.
 */
public class TodayWeathFrag extends BaseFrag implements TodayFragmentImpl, View.OnClickListener {

    @BindView(R.id.today_back)
    LinearLayout todayBack;
    @BindView(R.id.today_titile)
    RelativeLayout todayTitile;
    @BindView(R.id.today_city)
    TextView todayCity;
    @BindView(R.id.today_refresh)
    ImageView todayRefresh;
    @BindView(R.id.today_refreshs)
    TextView todayRefreshs;
    @BindView(R.id.today_time)
    TextView todayTime;
    @BindView(R.id.today_class)
    TextView todayClass;
    @BindView(R.id.today_image)
    ImageView todayImage;
    @BindView(R.id.today_wendu)
    TextView todayWendu;
    @BindView(R.id.today_tomorrow)
    TextView todayTomorrow;
    @BindView(R.id.today_life)
    TextView todayLife;
    @BindView(R.id.today_destory)
    TextView todayDestory;
    @BindView(R.id.today_1)
    TextView today1;
    @BindView(R.id.today_2)
    TextView today2;
    @BindView(R.id.today_3)
    TextView today3;
    @BindView(R.id.today_4)
    TextView today4;
    @BindView(R.id.today_5)
    TextView today5;
    @BindView(R.id.today_6)
    TextView today6;
    @BindView(R.id.today_power_1)
    TextView todayPower1;
    @BindView(R.id.today_power_2)
    TextView todayPower2;
    @BindView(R.id.today_power_3)
    TextView todayPower3;
    @BindView(R.id.today_destory_alarm)
    TextView todayDestoryAlarm;
    @BindView(R.id.today_destory_text)
    TextView todayDestoryText;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.recyclerViews)
    RecyclerView recyclerViews;
    @BindView(R.id.search)
    ImageView search;
    private TodayPresent topPresent;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;
    private String city;
    private ValueAnimator animator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("刷新中...");
        progressDialog.show();
        Bundle arguments = getArguments();
        city = arguments.getString("city");
        topPresent = new TodayPresent(this, city);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.basePresent = topPresent;
        super.onCreate(savedInstanceState);
    }

    public static TodayWeathFrag newInstance(String arg) {
        TodayWeathFrag fragment = new TodayWeathFrag();
        Bundle bundle = new Bundle();
        bundle.putString("city", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wea_today, null);
        ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        getViews(inflate);
        setListener();
        return inflate;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos) {
        int pos = onEventpos.pos;
        if (pos == 2) {
            todayTitile.setBackgroundColor(getResources().getColor(R.color.color_82181818));
        } else if (pos == 3) {
            todayTitile.setBackgroundColor(getResources().getColor(R.color.color_00ffffff));
        }
    }

    @Override
    public void getViews(View view) {

    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getDestory(WeaDestory weaDestory) {
        List<WeaDestory.HeWeather5Bean.AlarmsBean> alarms = weaDestory.getHeWeather5().get(0).getAlarms();
        if (alarms != null) {
            todayDestoryAlarm.setText("type:" + alarms.get(0).getLevel() + "," + alarms.get(0).getTitle());
            todayDestoryText.setText("预警内容:" + alarms.get(0).getTxt());
        } else {
            todayDestoryAlarm.setText("无任何自然灾害预警");
            todayDestoryText.setText("预警内容:无");
        }
    }

    @Override
    public void getHoursInfo(WeaHours weaHours) {
        recyclerViews.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(getContext(), weaHours.getHeWeather5().get(0).getHourly_forecast());
        recyclerViews.setAdapter(recyclerAdapter);

    }

    @Override
    public void getInfo(WeaNow weaNow) {
        WeaNow.HeWeather5Bean.BasicBean basic = weaNow.getHeWeather5().get(0).getBasic();
        WeaNow.HeWeather5Bean.NowBean now = weaNow.getHeWeather5().get(0).getNow();
        todayCity.setText(/*basic.getProv()+*/basic.getCity());
        String loc = basic.getUpdate().getLoc();
        //时间
        todayTime.setText(loc);
        //天气
        todayClass.setText(now.getCond().getTxt());
        todayWendu.setText(now.getFl() + "℃");
//        addAnimation();
        today1.setText("体感温度:" + now.getFl() + "℃");
        today2.setText("相对湿度:" + now.getHum() + "%");
        today3.setText("降水量:" + now.getPcpn() + "mm");
        today4.setText("气压:" + now.getPres());
        today5.setText("温度:" + now.getTmp());
        today6.setText("能见度:" + now.getVis() + "km");
        todayPower1.setText("风向:" + now.getWind().getDir());
        todayPower2.setText("风力:" + now.getWind().getSc());
        todayPower3.setText("风速:" + now.getWind().getSpd() + "kmph");
        String code = now.getCond().getCode();
        EventBus.getDefault().post(new OnEventpos(Integer.valueOf(code)));
    }

    @Override
    public void dismissDialogs() {
        progressDialog.dismiss();
    }

    @Override
    public void setListener() {
        todayRefreshs.setOnClickListener(this);
        todayRefresh.setOnClickListener(this);
        todayTomorrow.setOnClickListener(this);
        todayDestory.setOnClickListener(this);
        todayLife.setOnClickListener(this);
        todayBack.setOnClickListener(this);
        search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_refresh:
                if (progressDialog != null) {
                    progressDialog.show();
                }
                topPresent.getInfo(city);
                break;
            case R.id.today_refreshs:
                if (progressDialog != null) {
                    progressDialog.show();
                }
                topPresent.getInfo(city);
                break;
            case R.id.today_tomorrow:
                EventBus.getDefault().post(new EventDay("tomorrow"));
                break;
            case R.id.today_destory:
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            //生活指数
            case R.id.today_life:
                EventBus.getDefault().post(new EventDay("life"));
                break;
            case R.id.today_back:
                EventBus.getDefault().post(new OnEventpos(1));
                break;
            case R.id.search:
                getSearchCity();
                break;
        }
    }

    private void getSearchCity() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.search, null);
        final EditText editText= (EditText) inflate.findViewById(R.id.search);
        new AlertDialog.Builder(getContext())
                .setTitle("城市搜索:")
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().toString()!=null){
                            city=editText.getText().toString().trim();
                            topPresent.getInfo(city);
                            if (progressDialog != null) {
                                progressDialog.show();
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addAnimation() {
        animator = ValueAnimator.ofInt(0, 20, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                todayWendu.layout(todayWendu.getLeft() + animatedValue, todayWendu.getTop(), todayWendu.getRight() + animatedValue, todayWendu.getBottom());
            }
        });

        animator.setDuration(500);
        animator.setEvaluator(new MyEvaluator());
        animator.start();
    }
}