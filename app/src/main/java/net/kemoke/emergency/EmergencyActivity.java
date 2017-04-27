package net.kemoke.emergency;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.kemoke.emergency.api.HttpApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirstViewHolder firstViewHolder;
    private SecondViewHolder secondViewHolder;
    private GoogleApiClient googleApiClient;
    private ThirdViewHolder thirdViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstViewHolder = new FirstViewHolder(getLayoutInflater().inflate(R.layout.second_layout, null));
        secondViewHolder = new SecondViewHolder(getLayoutInflater().inflate(R.layout.third_layout, null));
        thirdViewHolder = new ThirdViewHolder(getLayoutInflater().inflate(R.layout.fourth_layout, null));
        setContentView(firstViewHolder.view);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        firstViewHolder.view.postDelayed(new Runnable() {
            @Override
            public void run() {
                googleApiClient.connect();
                setContentView(secondViewHolder.view);
            }
        }, 3000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final ApiLocation apiLocation = new ApiLocation();
        apiLocation.lat = lastLocation.getLatitude();
        apiLocation.lon = lastLocation.getLongitude();
        apiLocation.jmbg = "21312312321";
        if (conMgr.getActiveNetworkInfo().isConnected()) {
            HttpApi.getLocationService().sendLocation(apiLocation).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() != 200) {
                        HttpApi.getLocationService().sendLocation(apiLocation).enqueue(this);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //TODO: sendSms(apiLocation);
                }
            });
        } else {
            //TODO: sendSms(apiLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class FirstViewHolder {
        View view;

        FirstViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.view = view;
        }

        @OnClick(R.id.cancel_btn)
        public void onCancel() {
            EmergencyActivity.this.finish();
        }
    }

    class SecondViewHolder {
        @BindView(R.id.self_btn)
        Button selfBtn;
        @BindView(R.id.other_btn)
        Button otherBtn;
        @BindView(R.id.many_btn)
        Button manyBtn;
        private View view;

        SecondViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.self_btn, R.id.other_btn, R.id.main_btn})
        public void onClick() {
            EmergencyActivity.this.setContentView(thirdViewHolder.view);
        }
    }

    class ThirdViewHolder {
        @BindView(R.id.reason1)
        Button reason1;
        @BindView(R.id.reason2)
        Button reason2;
        @BindView(R.id.reason3)
        Button reason3;
        @BindView(R.id.reason4)
        Button reason4;
        @BindView(R.id.reason5)
        Button reason5;
        private View view;

        ThirdViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.reason1, R.id.reason2, R.id.reason3, R.id.reason4, R.id.reason5})
        public void onClick(){
            EmergencyActivity.this.setContentView(R.layout.last_layout);
        }
    }
}
