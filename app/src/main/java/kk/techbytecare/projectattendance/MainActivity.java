package kk.techbytecare.projectattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.ncorti.slidetoact.SlideToActView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kk.techbytecare.projectattendance.Common.Common;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnContinue)
    SlideToActView slideToActView;

    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                Toast.makeText(this, "" + loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (loginResult.wasCancelled()) {
                Toast.makeText(this, "Login Cancelled.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Common.IS_LOGIN, true);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);


        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Common.IS_LOGIN, true);
            startActivity(intent);
            finish();
        }

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        imageView.setAnimation(animation);

        slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                phoneLogIn();
            }
        });
    }


    private void phoneLogIn() {
        Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, Common.REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideToActView.resetSlider();
    }
}
