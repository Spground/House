package jc.house.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;

public class UserFeedbackActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserFeedbackActivity";

    private EditText mEditText;
    private Button mButton;

    private String feedbackContent;

    private AsyncHttpClient mClient;
    private Map<String, String> reqParams;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        mClient = new AsyncHttpClient();
        setTitleBarTitle("用户反馈");
        initView();
        reqParams = new HashMap<>();
    }

    /**
     * initialize fucking view
     */
    private void initView(){
        this.mEditText = (EditText)findViewById(R.id.id_user_feedback_edit_text);
        this.mButton = (Button)findViewById(R.id.id_user_feedback_submit_button);
        this.mButton.setOnClickListener(this);
        this.progressDialog.setMessage("正在提交，请稍后...");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() != R.id.id_user_feedback_submit_button || mEditText  == null)
            return;
        //check the character limit, no more than 140 characters
        feedbackContent = mEditText.getText().toString().trim();
        if(feedbackContent.length() <=0 || feedbackContent.length() > 140){
            ToastUtils.show(this, "抱歉，字数不满足要求!");
            return;
        }
        showDlg();

        /**-----------perform network request----------------**/
        reqParams.clear();
        reqParams.put("content", feedbackContent);
        mClient.post(Constants.FEEDBACK_URL, new RequestParams(reqParams), new JsonHttpResponseHandler() {
            //the callback will happens UI thread
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //parse json, get the server's return info
                if(response == null){
                    ToastUtils.show(UserFeedbackActivity.this, "JSON解析错误");
                    return;
                }

                try {
                    code = response.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.debug(TAG, e.toString());
                }
                finally {
                    cancelDlg();
                }
                //submit successfully
                if(code == 1){
                    ToastUtils.show(UserFeedbackActivity.this, "谢谢您的反馈，我们将尽快处理");
                    UserFeedbackActivity.this.finish();
                }
                else{
                    ToastUtils.show(UserFeedbackActivity.this, "抱歉，出现错误，请您稍后再试");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelDlg();
                ToastUtils.show(UserFeedbackActivity.this, "网络出现错误，请您稍后再试");
            }
        });
    }

    private void showDlg(){
        if(this.progressDialog != null && this.progressDialog.isShowing() == false)
            this.progressDialog.show();
    }

    private void cancelDlg(){
        if(this.progressDialog != null && this.progressDialog.isShowing() == true)
            this.progressDialog.hide();
    }
}