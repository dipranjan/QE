package com.qualityengineer.dearc.qe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView[] dots;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername,mPhotoUrl,mEmailAddress;
    private int[] layouts = new int[]{R.layout.intro_slide1, R.layout.intro_slide2, R.layout.intro_slide3};
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null){
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            mEmailAddress = mFirebaseUser.getEmail();
            launchHomeScreen();
            finish();
        }

        // Checking for first time launch - before calling setContentView()
       /* if (!Globals.shouldShowSlider()) {
            launchHomeScreen();
            finish();
        }*/

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

        setBtnClickListener(R.id.btn_got_it);
        setBtnClickListener(R.id.btn_next);
        //setBtnClickListener(R.id.btn_skip);

        // adding bottom dots
        addBottomDots();
        // By default, select dot in the first position
        updateBottomDots(0, 0);

        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private void setBtnClickListener(int id) {
        Button button = findViewById(id);
        if (button != null) {
            button.setOnClickListener(this);
        }
    }

    private void showHideView(int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void launchHomeScreen() {
        //Globals.saveFirstTimeLaunch(false);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.putExtra("uri", mFirebaseUser.getPhotoUrl().toString());
        startActivity(intent);
        finish();
    }

    private void addBottomDots() {
        if ((dotsLayout == null) || (layouts == null))
            return;

        int dotSize = layouts.length;
        dotsLayout.removeAllViews();

        dots = new TextView[dotSize];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }
    }

    private void updateBottomDots(int prevPosition, int curPosition) {
        if (dots == null)
            return;

        int dotLength = dots.length;
        if ((dotLength > prevPosition) && (dotLength > curPosition)) {
            dots[prevPosition].setTextColor(getResources().getColor(R.color.dot_inactive));
            dots[curPosition].setTextColor(getResources().getColor(R.color.dot_active));
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        int prevPos = 0;

        @Override
        public void onPageSelected(int position) {

            updateBottomDots(prevPos, position);

            boolean isLastPage = (position == (layouts.length - 1));
            showHideView(R.id.btn_next, isLastPage ? View.GONE : View.VISIBLE);
            //showHideView(R.id.btn_skip, isLastPage ? View.GONE : View.VISIBLE);
            showHideView(R.id.btn_got_it, isLastPage ? View.VISIBLE : View.GONE);

            prevPos = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.btn_skip:
            case R.id.btn_got_it:
               signIn();
                break;
            case R.id.btn_next:
                showNextSlide();
                break;
        }
    }

    private void showNextSlide() {
        // Checking for last page
        // If last page home screen will be launched
        int nextIndex = viewPager.getCurrentItem() + 1;
        if ((viewPager != null) && (nextIndex < layouts.length)) {
            viewPager.setCurrentItem(nextIndex);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return (layouts != null) ? layouts.length : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return (view == obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener( this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),"Welcome to QE.",Toast.LENGTH_LONG).show();
                    launchHomeScreen();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(getApplicationContext(),"Sign in Failure.",Toast.LENGTH_LONG).show();
                    //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    //updateUI(null);
                }

            }
        });
    }
    // [END auth_with_google]

    // [START signin]

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    public void signOut() {
        // Firebase sign out
        mFirebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut();
    }

    public void revokeAccess() {
        // Firebase sign out
        mFirebaseAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess();
    }
}
