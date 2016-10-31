package infinnov.udacity.capstoneproject;

import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment implements GestureDetector.OnGestureListener  {

    @BindView(R.id.pb11) ImageView pb11;
    @BindView(R.id.pb12) ImageView pb12;
    @BindView(R.id.pb13) ImageView pb13;
    @BindView(R.id.pb14) ImageView pb14;
    @BindView(R.id.pb21) ImageView pb21;
    @BindView(R.id.pb22) ImageView pb22;
    @BindView(R.id.pb23) ImageView pb23;
    @BindView(R.id.pb24) ImageView pb24;
    @BindView(R.id.pb31) ImageView pb31;
    @BindView(R.id.pb32) ImageView pb32;
    @BindView(R.id.pb33) ImageView pb33;
    @BindView(R.id.pb34) ImageView pb34;
    @BindView(R.id.pb41) ImageView pb41;
    @BindView(R.id.pb42) ImageView pb42;
    @BindView(R.id.pb43) ImageView pb43;
    @BindView(R.id.pb44) ImageView pb44;

    @BindView(R.id.imgstart) ImageView imgstart;

    private GestureDetectorCompat mDetector;

    final int[][] arrval= new int[4][4];
    final Boolean[][] arrbool= new Boolean[4][4];
    final ImageView[][] img = new ImageView[4][4];
    static int score = 0 ;
    static int HSCORE = 0;
    static String MODE4= "";
    static String music="";
    @BindView(R.id.lblhscore)    TextView lblhscore;
    @BindView(R.id.lblscore) TextView lblscore;
    @BindView(R.id.reset) TextView reset;
    MediaPlayer mjoin;
    MediaPlayer mnojoin;
    static Animation animationFadeIn;

    public GameFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("====================","-----------------------");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        view.setLongClickable(true);
        Log.e("====================","+++++++++++++++++++++++++++++++");

        //ButterKnife.bind(this,view);
        animationFadeIn = AnimationUtils.loadAnimation(getContext() , R.anim.fadein);
        mDetector = new GestureDetectorCompat(getContext(),this);

        pb11 = (ImageView) view.findViewById(R.id.pb11);
        pb12 = (ImageView) view.findViewById(R.id.pb12);
        pb13 = (ImageView) view.findViewById(R.id.pb13);
        pb14 = (ImageView) view.findViewById(R.id.pb14);

        pb21 = (ImageView) view.findViewById(R.id.pb21);
        pb22 = (ImageView) view.findViewById(R.id.pb22);
        pb23 = (ImageView) view.findViewById(R.id.pb23);
        pb24 = (ImageView) view.findViewById(R.id.pb24);

        pb31 = (ImageView) view.findViewById(R.id.pb31);
        pb32 = (ImageView) view.findViewById(R.id.pb32);
        pb33 = (ImageView) view.findViewById(R.id.pb33);
        pb34 = (ImageView) view.findViewById(R.id.pb34);

        pb41 = (ImageView) view.findViewById(R.id.pb41);
        pb42 = (ImageView) view.findViewById(R.id.pb42);
        pb43 = (ImageView) view.findViewById(R.id.pb43);
        pb44 = (ImageView) view.findViewById(R.id.pb44);

        lblscore = (TextView) view.findViewById(R.id.lblscore);
        lblhscore = (TextView) view.findViewById(R.id.lblhscore);
        imgstart = (ImageView) view.findViewById(R.id.imgstart);
        reset = (TextView) view.findViewById(R.id.reset);

        img[0][0] = pb11;
        img[0][1] = pb12;
        img[0][2] = pb13;
        img[0][3] = pb14;
        img[1][0] = pb21;
        img[1][1] = pb22;
        img[1][2] = pb23;
        img[1][3] = pb24;
        img[2][0] = pb31;
        img[2][1] = pb32;
        img[2][2] = pb33;
        img[2][3] = pb34;
        img[3][0] = pb41;
        img[3][1] = pb42;
        img[3][2] = pb43;
        img[3][3] = pb44;

        mjoin = MediaPlayer.create(getContext(), R.raw.join);
        mnojoin = MediaPlayer.create(getContext(), R.raw.nojoin);
        //reset.setText("asdasdasdasd");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                resetgame();
                createnew(arrbool , arrval , img);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });
        return view;
    }

    public void setimgwin()
    {
        imgstart.setImageResource(R.drawable.a2bb);
    }

    public void getHscore(String MODE4)
    {
        HSCORE = 300;
        /*
        DbOpt connection = new DbOpt( Nuances.this );
        connection.open();
        Cursor u = connection.searchByMode(MODE4);
        boolean findUser=false;
        String Hscore="";
        if(u!=null && u.getCount()>0)
        {
            if(u.moveToFirst())
            {
                findUser=true;
                Hscore= u.getString(u.getColumnIndex("HScore"));
                if(findUser==true)
                {
                    HSCORE = Integer.parseInt(Hscore);
                }
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();

        /*
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++)
        {
            for(int j=0 ; j<4 ; j++)
                str.append(arrval[i][j]).append(",");
        }
        editor.putString(MODE4 + "arrval", str.toString());


        StringBuilder str1 = new StringBuilder();
        for (int i = 0; i < 4; i++)
        {
            for(int j=0 ; j<4 ; j++)
                str1.append(arrbool[i][j]).append(",");
        }
        editor.putString(MODE4 + "arrbool", str1.toString());
        editor.putInt(MODE4 +"score", score);

        editor.commit();	//saves the prefs
        if(gac.isConnected())
        {
            Games.Leaderboards.submitScore(getApiClient(),getString(R.string.leaderboard_NORMAL), HSCORE);
        }
        gac.disconnect();*/
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
/*
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String savedString = prefs.getString(MODE4 + "arrval", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        if(savedString.equalsIgnoreCase(""))
        {
            resetgame();
            createnew(arrbool, arrval, img);
            setimgwin();
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                for(int j=0 ; j<4 ; j++)
                    arrval[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        String savedString1 = prefs.getString(MODE4 + "arrbool", "");
        StringTokenizer st1 = new StringTokenizer(savedString1, ",");
        if(savedString1.equalsIgnoreCase(""))
        {
            resetgame();
            createnew(arrbool, arrval, img);
            setimgwin();
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                for(int j=0 ; j<4 ; j++)
                    arrbool[i][j] = Boolean.valueOf(st1.nextToken());
            }
            getHscore(MODE4);
            setimgwin();
            score = prefs.getInt(MODE4+"score", 0);
            lblscore.setText("SCORE : "+ score);
            lblhscore.setText("HIGH SCORE : "+ HSCORE);
            for (int i = 0; i < 4; i++)
            {
                for(int j=0 ; j<4 ; j++)
                {
                    setimg(i ,j ,arrbool , arrval ,  img);
                }
            }
        }
        */
        //gac.connect();
    }

    public void setimg(int i , int j , Boolean[][] arrbool , int[][] arrval , ImageView[][] img)
    {
        int x=arrval[i][j];
        if(x == 0)
            img[i][j].setImageResource(R.drawable.blank);
        if (x == 2)
            img[i][j].setImageResource(R.drawable.a2bb);
        if (x == 4)
            img[i][j].setImageResource(R.drawable.a4bb);
        if (x == 8)
            img[i][j].setImageResource(R.drawable.a8bb);
        if (x == 16)
            img[i][j].setImageResource(R.drawable.a16bb);
        if (x == 32)
            img[i][j].setImageResource(R.drawable.a32bb);
        if (x == 64)
            img[i][j].setImageResource(R.drawable.a64bb);
        if (x == 128)
        {
            img[i][j].setImageResource(R.drawable.a128bb);
            //Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_half_way__normal));
        }
        if (x == 256)
            img[i][j].setImageResource(R.drawable.a256bb);
        if (x == 512)
            img[i][j].setImageResource(R.drawable.a512bb);
        if (x == 1024)
            img[i][j].setImageResource(R.drawable.a1024bb);
        if (x == 2048)
        {
            img[i][j].setImageResource(R.drawable.a2048);
            //Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_won_normal));
            //showDialog(1);
            resetgame();
        }
    }

    public void setimgnew(int i , int j , ImageView[][] img)
    {
        img[i][j].startAnimation(animationFadeIn);
        img[i][j].setImageResource(R.drawable.a2bb);
    }

    public void checkloose()
    {
        int flag = 0;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (arrbool[i][j] == false)
                    flag = 1;
                else
                    continue;
            }
        }
        if (flag == 0)
        {
            //showDialog(2);
            resetgame();
        }
    }

    public void createnew( Boolean[][] arrbool , int[][] arrval , ImageView[][] img)
    {
        int flag = 0;
        int x, y;
        Random rnd = new Random();
        while (flag == 0)
        {
            x = rnd.nextInt(4);
            y = rnd.nextInt(4);
            if (arrbool[x][y] == false)
            {
                arrbool[x][y] = true;
                arrval[x][y] = 2;
                setimgnew(x, y , img);
                flag = 1;
            }
            else
                flag = 0;
        }
    }

    public void setscore()
    {
        score = 0;
        for(int i=0; i<4 ;i++)
        {
            for(int j=0; j<4 ; j++)
            {
                score = score + (arrval[i][j] * 2);
            }
        }
        if(score > HSCORE)	//
        {
            updatehscore(score);
            HSCORE = score;
        }
        lblscore.setText("SCORE : "+score);
        lblhscore.setText("HIGH SCORE : "+ HSCORE);
    }

    public void updatehscore(int score)
    {
        /*
        DbStruct l1 = new DbStruct();
        l1.mode =MODE4;
        l1.HScore = score;

        DbOpt connection = new DbOpt( Nuances.this );
        connection.open();
        connection.updatetab(l1);
        connection.close();*/
    }

    public void resetgame()
    {
        score = 0;
        for(int i=0; i<4 ;i++)
        {
            for(int j=0; j<4 ; j++)
            {
                img[i][j].setImageResource(R.drawable.blank);
                arrval[i][j] = 0;
                arrbool[i][j] = false;
            }
        }
        createnew(arrbool , arrval , img);
        setscore();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.e("fling","------------------");
        return false;
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

}
