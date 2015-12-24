package jc.house.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class UserFeedbackActivity extends BaseNetActivity implements View.OnClickListener,
        DialogInterface.OnClickListener {
    private static final String TAG = "UserFeedbackActivity";

    private EditText mEditText;
    private Button mButton;

    private AlertDialog alertDialog;

    private String feedbackContent;

    private Map<String, String> reqParams;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_user_feedback);
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
        alertDialog = new AlertDialog.Builder(this).
                setMessage("提交反馈成功,谢谢您的反馈!")
                .setNeutralButton("知道了", UserFeedbackActivity.this)
                .setCancelable(false)
                .create();
    }

    @Override
    public void onClick(View v) {
        closeSoftInputWindow();
        if(v.getId() != R.id.id_user_feedback_submit_button || mEditText  == null)
            return;
        //check the character limit, no more than 140 characters
        feedbackContent = mEditText.getText().toString().trim();
        if(feedbackContent.length() <=0 || feedbackContent.length() > 140){
            ToastS("抱歉，字数不满足要求!");
            return;
        }
        showDlg();

        /**-----------perform network request----------------**/
        reqParams.clear();
        reqParams.put("content", feedbackContent);
        LogUtils.debug(TAG, "start network request");
        this.client.post(Constants.FEEDBACK_URL, new RequestParams(reqParams), new JsonHttpResponseHandler() {
            //the callback will happens UI thread
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //parse json, get the server's return info
                LogUtils.debug(TAG, response.toString());
                if (response == null) {
                    ToastS("JSON解析错误");
                    return;
                }
                try {
                    code = response.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.debug(TAG, e.toString());
                } finally {
                    cancelDlg();
                }
                //submit successfully
                if (code == 1) {
                    alertDialog.show();
                } else {
                    ToastS("抱歉，出现错误，请您稍后再试");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtils.debug(TAG, "network request failed");
                cancelDlg();
                ToastS("网络出现错误，请您稍后再试");
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        UserFeedbackActivity.this.finish();
    }

    private void showDlg(){
        if(!this.progressDialog.isShowing())
            this.progressDialog.show();
    }

    private void cancelDlg(){
        if(this.progressDialog.isShowing())
            this.progressDialog.hide();
    }

    /**
     * close soft input
     */
    private void closeSoftInputWindow(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
