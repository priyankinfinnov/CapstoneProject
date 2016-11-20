package infinnov.udacity.capstoneproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginHome extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    final static int RC_SIGN_IN = 1;

    @BindView(R.id.ivPlayerImage)    ImageView ivPlayerImage;
    @BindView(R.id.tvHighScore)    TextView tvHighScore;
    @BindView(R.id.btnPlay)    Button btnPlay;
    @BindView(R.id.sign_in_button)    com.google.android.gms.common.SignInButton sign_in_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(), Game.class);
                startActivity(intent3);
            }
        });
        ivPlayerImage.setVisibility(View.GONE);
        tvHighScore.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Nuances App", "--------------------------handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Nuances App", "--------------------------"+acct.getPhotoUrl());
            //ivPlayerImage.setImageResource();
            sign_in_button.setVisibility(View.GONE);
            ivPlayerImage.setVisibility(View.VISIBLE);
            tvHighScore.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
            Picasso.with(this.getApplicationContext()).load(acct.getPhotoUrl()).into(ivPlayerImage);

            String URL = "content://infinnov.udacity.capstoneproject/scores/1";

            Uri scoreUri = Uri.parse(URL);
            //Cursor c = managedQuery(scoreUri, null, null, null, "score");
            CursorLoader cursorLoader = new CursorLoader(this.getApplicationContext(), scoreUri,
                    null, null, null, null);
            Cursor c = cursorLoader.loadInBackground();

            if (c.moveToFirst()) {
                Integer Score = c.getInt(c.getColumnIndex( ScoreProvider.SCORE));
                tvHighScore.setText("Your High "+Score);
                //Toast.makeText(getApplicationContext(), "Your High "+Score, Toast.LENGTH_SHORT).show();
            }
            //mStatusTextView.setText(getString(R.string.signedin_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Nuances App", "--------------------------Signin FIALED");
    }
}
