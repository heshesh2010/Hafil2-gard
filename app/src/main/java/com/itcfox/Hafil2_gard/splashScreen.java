package com.itcfox.Hafil2_gard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends Activity {

private final static int MSG_CONTINUE = 1234;
private final static long DELAY = 2000;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    mHandler.sendEmptyMessageDelayed(MSG_CONTINUE, DELAY);

}

@Override
protected void onDestroy() {
    mHandler.removeCallbacksAndMessages(MSG_CONTINUE);
    super.onDestroy();
}

private void _continue() {
    startActivity(new Intent(splashScreen.this, LoginActivity.class));
    finish();
}

private final Handler mHandler = new Handler() {
    public void handleMessage(android.os.Message msg) {
        switch(msg.what) {
            case MSG_CONTINUE:
                _continue();
                break;
        }
    }
};

}