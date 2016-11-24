package infinnov.udacity.capstoneproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginHome extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {

    final static int RC_SIGN_IN = 1;
    final static String worldhighscoreurl = "http://cutshort-data.s3.amazonaws.com/cloudfront/public/temporary/highscore.json";
    GoogleApiClient mGoogleApiClient;

    //"https://docs.google.com/document/d/e/2PACX-1vQKM2YvkYSHxhURLX8XfeFUW3VMzl8JyKwErk0G7TYXGfBRU9JQnMs4jGy7g5XUjG45VvThK1N9eifG/pub?embedded=true";//https://www.dropbox.com/s/w52qqbbaucxqkmb/highscore.json?dl=0";
    @BindView(R.id.ivPlayerImage)
    ImageView ivPlayerImage;
    @BindView(R.id.tvHighScore)
    TextView tvHighScore;
    @BindView(R.id.tvWorldHighScore)
    TextView tvWorldHighScore;
    @BindView(R.id.btnPlay)
    Button btnPlay;
    @BindView(R.id.sign_in_button)
    com.google.android.gms.common.SignInButton sign_in_button;

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
        tvWorldHighScore.setVisibility(View.GONE);
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
            Log.d("Nuances App", "--------------------------" + acct.getPhotoUrl());
            //ivPlayerImage.setImageResource();
            sign_in_button.setVisibility(View.GONE);
            ivPlayerImage.setVisibility(View.VISIBLE);
            tvHighScore.setVisibility(View.VISIBLE);
            tvWorldHighScore.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
            Picasso.with(this.getApplicationContext()).load(acct.getPhotoUrl()).into(ivPlayerImage);
            getSupportLoaderManager().initLoader(1, null, this);
            GetWorldHighScore runner = new GetWorldHighScore();
            runner.execute(worldhighscoreurl);
        } else {
            Log.d("Nuances App", "--------------------------Signin FIALED with handleSignInResult");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Nuances App", "--------------------------Signin FIALED");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = "content://infinnov.udacity.capstoneproject/scores/1";

        Uri scoreUri = Uri.parse(URL);
        CursorLoader cursorLoader = new CursorLoader(this.getApplicationContext(), scoreUri,
                null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (c.moveToFirst()) {
            Integer Score = c.getInt(c.getColumnIndex(ScoreProvider.SCORE));
            tvHighScore.setText(getString(R.string.highscore) + " " + Score);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class GetWorldHighScore extends AsyncTask<String, String, String> {

        private String resp;
        private TextView whs;

        public GetWorldHighScore() {
            this.whs = tvWorldHighScore;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                //Log.e("-----------",""+builder.toString());
                JSONObject topLevel = new JSONObject(builder.toString());

                resp = topLevel.getString("score");
                //Log.e("-----------",""+resp);
                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            whs.setText(getString(R.string.worldhighscore) + " " + result);
        }


        @Override
        protected void onPreExecute() {
        }
    }
}
