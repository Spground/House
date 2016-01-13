package jc.house.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import jc.house.R;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;

//TODO 记住密码， 下一次免登录
public class CustomerHelperLoginActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = "CustomerHelperLoginActivity";
    private  String huanxinid, pwd;

    private EditText idET, pwdET;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_customer_helper_login);
        setTitleBarTitle("客服人员登录");
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        idET = (EditText)findViewById(R.id.edittext_huanxinid);
        pwdET = (EditText)findViewById(R.id.edittext_pwd);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        huanxinid = idET.getText().toString().trim();
        pwd = pwdET.getText().toString().trim();

        //TODO 应当进一步验证输入合法性
        if(huanxinid.length() == 0 || pwd.length() == 0) {
            ToastUtils.show(this, "用户名或密码不能为空");
            return;
        }
        this.progressDialog.setMessage("正在登录...");
        this.progressDialog.show();
        loginHuanXin(huanxinid, pwd);
    }

    /**
     * 登录环信
     */
    private void loginHuanXin(final String huanxinid, final String pwd) {

        /**login huanxin**/
        EMChatManager.getInstance().login(huanxinid, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        LogUtils.debug(TAG, "登陆聊天服务器成功！");
                        LogUtils.debug(TAG, "login succeed huanxin id is " + huanxinid + " pwd is " + pwd);
                        //登录成功加载所有的数据库记录到内存
                        EMChatManager.getInstance().loadAllConversations();
                        CustomerHelperLoginActivity.this.progressDialog.hide();
                        ToastUtils.show(CustomerHelperLoginActivity.this, "登录成功");
                        startActivity(new Intent(CustomerHelperLoginActivity.this, HomeActivity.class));
                        CustomerHelperLoginActivity.this.finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtils.debug(TAG, "登陆聊天服务器失败！");
                runOnUiThread(new Runnable() {
                    public void run() {
                        CustomerHelperLoginActivity.this.progressDialog.hide();
                        ToastUtils.show(CustomerHelperLoginActivity.this, "登录失败， 用户名或密码错误");
                    }
                });
            }
        });
    }
}
