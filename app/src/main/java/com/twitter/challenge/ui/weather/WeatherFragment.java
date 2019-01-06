package com.twitter.challenge.ui.weather;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.challenge.R;
import com.twitter.challenge.TemperatureConverter;
import com.twitter.challenge.api.WeatherApi;
import com.twitter.challenge.data.MultiDayWeather;
import com.twitter.challenge.data.WeatherWrapper;
import com.twitter.challenge.repository.WeatherRepository;

public class WeatherFragment extends Fragment {

    private WeatherViewModel mViewModel;
    private static int FORECAST_DAYS = 5;
    private TextView mTemperature;
    private TextView mWind;
    private ImageView mWeatherIcon;
    private ProgressBar mCurrentProgress;
    private ProgressBar mStdDevProgress;
    private View mCurrentCard;
    private View mStdCard;

    private Button mStdBtn;
    private TextView mStdDev;
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        mStdBtn = (Button) fragmentView.findViewById(R.id.btn_std);
        mStdDev = (TextView) fragmentView.findViewById(R.id.std_val);
        mWeatherIcon = (ImageView) fragmentView.findViewById(R.id.cloud_icon);
        mCurrentProgress = (ProgressBar) fragmentView.findViewById(R.id.progress_current);
        mStdDevProgress = (ProgressBar) fragmentView.findViewById(R.id.progress_std);
        mCurrentCard = fragmentView.findViewById(R.id.current_card);
        mStdCard = fragmentView.findViewById(R.id.std_card);

        mStdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStd();
            }
        });

        mTemperature = fragmentView.findViewById(R.id.temperature);
        mWind = fragmentView.findViewById(R.id.wind);

        if (isNetworkAvailable()) {
            showLoading(true);
        } else {
            showNetworkError(getString(R.string.network_error));
        }


        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isNetworkAvailable()) {
            loadFromNetwork();
        }

    }

    private void showLoading(boolean current) {
        if (current) {
            mCurrentProgress.setVisibility(View.VISIBLE);
            mCurrentCard.setVisibility(View.GONE);
        } else {
            mStdDevProgress.setVisibility(View.VISIBLE);
            mStdCard.setVisibility(View.GONE);
        }
    }

    private void showNetworkError(String error) {
        mCurrentProgress.setVisibility(View.GONE);
        mStdDevProgress.setVisibility(View.GONE);
        mStdCard.setVisibility(View.GONE);
        mCurrentCard.setVisibility(View.GONE);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    private void loadFromNetwork() {
        showLoading(true);
        mViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        mViewModel.init(new WeatherRepository(WeatherApi.getInstance(getString(R.string.endpoint)).getWeatherInterface()));
        mViewModel.getCurrentWeather().observe(this, new Observer<WeatherWrapper>() {
            @Override
            public void onChanged(@Nullable WeatherWrapper weather) {

                if (weather == null) {
                    showNetworkError(getString(R.string.api_error));
                } else {
                    float temp = weather.weather.temp;
                    mTemperature.setText(getString(R.string.temperature, temp, TemperatureConverter.celsiusToFahrenheit(temp)));
                    mWind.setText(getString(R.string.wind_speed, weather.wind.speed));

                    if (weather.clouds.isCloudy()) {
                        mWeatherIcon.setVisibility(View.VISIBLE);
                        mWeatherIcon.setImageResource(R.drawable.ic_clouds);
                    } else {
                        mWeatherIcon.setVisibility(View.GONE);
                    }

                    reloadData(true);
                }

            }
        });

        if (mViewModel.getMultidayWeather() != null) {
            //For activity relaunches, set the previously loaded standard deviation

            mViewModel.getMultidayWeather().observe(this, new Observer<MultiDayWeather>() {
                @Override
                public void onChanged(@Nullable MultiDayWeather weather) {

                    if (weather == null) {
                        showNetworkError(getString(R.string.api_error));
                    } else {
                        String stdDev = String.format("%.2f", weather.calculateStdDev());

                        mStdDev.setText(stdDev);
                    }

                }
            });
        }

    }

    private void loadStd() {
        showLoading(false);

        mViewModel.resetWeather();

        mViewModel.reloadMultiDayWeather(FORECAST_DAYS).observe(this, new Observer<MultiDayWeather>() {
            @Override
            public void onChanged(@Nullable MultiDayWeather weather) {

                if (weather == null) {
                    showNetworkError(getString(R.string.api_error));
                } else {
                    reloadData(false);
                    String stdDev = String.format("%.2f", weather.calculateStdDev());
                    mStdDev.setText(stdDev);
                }

            }
        });
    }

    private void reloadData(final boolean current) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (current) {
                    mCurrentProgress.setVisibility(View.GONE);
                    mCurrentCard.setVisibility(View.VISIBLE);
                } else {
                    mStdDevProgress.setVisibility(View.GONE);
                    mStdCard.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
