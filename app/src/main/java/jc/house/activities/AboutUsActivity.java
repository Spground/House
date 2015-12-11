package jc.house.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import jc.house.R;

public class AboutUsActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_about_us);
        setTitleBarTitle("关于我们");
        long mainID = Thread.currentThread().getId();
        Log.d("Thread id", "A:" + mainID);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                long id = Thread.currentThread().getId();
                Log.d("Thread id", "B:" + id);
            }
        });
        threadBackground();
    }

    private void threadBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long id = Thread.currentThread().getId();
                Log.d("Thread id", "C:" + id);
                int sum = 0;
                for(int i = 0; i < 100; i++) {
                    sum += i;
                }
                final int summ = sum;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        huiDiao(summ);
                    }
                });
            }
        }).start();
    }

    private void huiDiao(int sum) {
        ToastS("sum is " + sum);
    }

}
