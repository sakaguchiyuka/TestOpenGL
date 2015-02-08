package jp.ysakaguchi.testopengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private boolean isSupportOpenGl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        isSupportOpenGl = configurationInfo.reqGlEsVersion >= 0x20000; // 端末がOpenGL ES 2.0をサポートしているかチェック

        if (!isSupportOpenGl) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            final TextView tv = new TextView(this);
            tv.setText("端末がOpenGL ES 2.0に対応していません。");
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(tv, lp);
            setContentView(relativeLayout);
            return;
        }

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);  // OpenGLバージョンを設定
        mGLSurfaceView.setRenderer(new MyRenderer());  // レンダラを設定
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSupportOpenGl && null != mGLSurfaceView) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSupportOpenGl && null != mGLSurfaceView) {
            mGLSurfaceView.onPause();
        }
    }
}
