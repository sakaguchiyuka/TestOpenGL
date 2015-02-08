package jp.ysakaguchi.testopengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 端末がOpenGL ES 2.0をサポートしているかチェック
        if (0x20000 > ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion) {
            final RelativeLayout rl = new RelativeLayout(this);
            final TextView tv = new TextView(this);
            tv.setText("未対応端末");
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(rl.CENTER_IN_PARENT);
            rl.addView(tv, lp);
            setContentView(rl);
            return;
        }


        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);  // OpenGLバージョンを設定
        mGLView.setRenderer(new MyRenderer(getText(this, "sample.vsh"), getText(this, "sample.fsh")));  // レンダラを設定
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mGLView) {
            mGLView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mGLView) {
            mGLView.onPause();
        }
    }

    public static String getText(final Context context, final String fileName) {
        if (null == context || null == fileName) {
            return null;
        }
        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                is = context.getAssets().open(fileName);
                br = new BufferedReader(new InputStreamReader(is));

                // 改行を付加
                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            e.printStackTrace();
            // エラー発生時の処理
            return null;
        }
        return text;
    }
}
