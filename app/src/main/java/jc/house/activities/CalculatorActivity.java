package jc.house.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jc.house.R;

public class CalculatorActivity extends BaseActivity implements View.OnClickListener {
    private Spinner paymentTypeSpinner, firstPaymentSpinner, loanTimeSpinner, rateDiscoutSpinner;
    private EditText unitPriceEdt;
    private EditText areaEdt, interestRateEdt;
    private Button calculateBtn;
    private TextView loanTotalTextView, repaymentMonthsTextView, repaymentPerMonthTextView, totalInterestTextView, totalPaymentTextView, repaymentPerText;
    private List<String> paymentTypeList, firstPayList, loanTimeList, rateDiscountList;
    private ArrayAdapter<String> paymentTypeAdapter, firstPayAdapter, loanTimeAdapter, rateDiscountAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_calculator);
        setTitleBarTitle("房贷计算器");
        initView();
    }

    /**
     * 初始化控件，绑定数据
     */
    private void initView() {
        this.paymentTypeSpinner = (Spinner) findViewById(R.id.payment_type);//还款类型(等额本金和等额本息)
        this.rateDiscoutSpinner = (Spinner) findViewById(R.id.interest_rate_discount);//年利率折扣
        this.firstPaymentSpinner = (Spinner) findViewById(R.id.first_payment_type);//（首付几层）
        this.loanTimeSpinner = (Spinner) findViewById(R.id.loan_time);//贷款时间
        this.unitPriceEdt = (EditText) findViewById(R.id.unit_price);//房价单价
        this.areaEdt = (EditText) findViewById(R.id.area);//面积
        this.interestRateEdt = (EditText) findViewById(R.id.interest_rate);//年利率用户输入，默认6.15
        this.calculateBtn = (Button) findViewById(R.id.calculate);//计算
        this.loanTotalTextView = (TextView) findViewById(R.id.loan_total);//贷款总金额
        this.repaymentMonthsTextView = (TextView) findViewById(R.id.repayment_months);//还款月数
        this.repaymentPerMonthTextView = (TextView) findViewById(R.id.repayment_per_month);//每月还款数
        this.totalInterestTextView = (TextView) findViewById(R.id.total_interest);//总支付利息
        this.totalPaymentTextView = (TextView) findViewById(R.id.total_payment);//本息合计
        this.repaymentPerText = (TextView) findViewById(R.id.repayment_first_month);//首月还款或者是每月还款
        //还款类型
        this.paymentTypeList = new ArrayList<>();
        paymentTypeList.add("等额本金");
        paymentTypeList.add("等额本息");
        this.paymentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentTypeList);
        this.paymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeSpinner.setAdapter(paymentTypeAdapter);

        //年利率折扣，7-10之间，每0.5一个间隔
        this.rateDiscountList = new ArrayList<>();
        for (double i = 7; i <= 10; i = i + 0.5) {
            rateDiscountList.add(i + "");
        }
        this.rateDiscountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rateDiscountList);
        this.rateDiscountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateDiscoutSpinner.setAdapter(rateDiscountAdapter);
        rateDiscoutSpinner.setSelection(6);
        //首付几成
        this.firstPayList = new ArrayList<>();
        for (int i = 2; i <= 9; i++) {
            firstPayList.add(i + "成");
        }
        this.firstPayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firstPayList);
        this.firstPayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstPaymentSpinner.setAdapter(firstPayAdapter);
        //贷款期限
        this.loanTimeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            loanTimeList.add(i + "年");
        }
        for (int i = 10; i <= 30; i = i + 5) {
            loanTimeList.add(i + "年");
        }
        this.loanTimeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loanTimeList);
        this.loanTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanTimeSpinner.setAdapter(loanTimeAdapter);
        this.calculateBtn.setOnClickListener(this);

    }

    /**
     *
     */
    private void calculate() {
        clearData();
//        Log.d("CalculatorActivity", "-------->>"+(String)paymentTypeSpinner.getSelectedItem());
        String unitPrices = unitPriceEdt.getText().toString();//需要校验
        String areas = areaEdt.getText().toString();//需要校验
        String interestRates = interestRateEdt.getText().toString();//需要校验

        if (check(unitPrices) && check(areas) && check(interestRates)) {
            double unitPrice = Double.parseDouble(unitPrices);
            double area = Double.parseDouble(areas);
            String firstPays = ((String) firstPaymentSpinner.getSelectedItem());//需要转化
            firstPays = trans(firstPays);
            double firstPay = Double.parseDouble(firstPays);
            Log.d("CalculatorActivity", "----------->>" + firstPay);
            String loanTimes = (String) loanTimeSpinner.getSelectedItem();//需要转化
            loanTimes = trans(loanTimes);
            int loanTime = Integer.parseInt(loanTimes);
            Log.d("CalculatorActivity", "----------->>" + loanTime);

            double interestRate = Double.parseDouble(interestRates) / 100;
            String rateDiscount = (String) rateDiscoutSpinner.getSelectedItem();
            double totalPrice = unitPrice * area;//总的房价
            double totalLoanMoney = totalPrice * (10 - firstPay) / 10;
            double repaymentPerMon = 0.d;//
            double totalInterest = 0.d;//总利息
            double totalMoney = 0.d;//本息合计
            double decreaseInterest = 0.d;
            DecimalFormat df = new DecimalFormat("#.##");
            if (((String) paymentTypeSpinner.getSelectedItem()).equals("等额本息")) {
                repaymentPerMon = (totalLoanMoney * (interestRate / 12) * Math.pow((1 + interestRate / 12), loanTime * 12)) / (Math.pow(1 + interestRate / 12, loanTime * 12) - 1);
                totalInterest = repaymentPerMon * loanTime * 12 - totalLoanMoney;
                repaymentPerMonthTextView.setText(df.format(repaymentPerMon) + "元");
                repaymentPerText.setText("每月还款：");
            } else if (((String) paymentTypeSpinner.getSelectedItem()).equals("等额本金")) {
                repaymentPerMon = totalLoanMoney / (loanTime * 12) + totalLoanMoney * (interestRate / 12);//首月应付款
                decreaseInterest = totalLoanMoney / (loanTime * 12) * (interestRate / 12);//每月递减
                repaymentPerText.setText("首月还款：");
                repaymentPerMonthTextView.setText(df.format(repaymentPerMon) + "元" + " 每月递减" + df.format(decreaseInterest));
                totalInterest = repaymentPerMon * loanTime * 12 - decreaseInterest * (loanTime * 12 * (loanTime * 12 - 1) / 2) - totalLoanMoney;
            }
            loanTotalTextView.setText(df.format(totalLoanMoney) + "元");
            repaymentMonthsTextView.setText(loanTime * 12 + "个月");
            totalInterestTextView.setText(df.format(totalInterest) + "元");
            totalPaymentTextView.setText(df.format(totalInterest + totalLoanMoney) + "元");
        }
    }


    @Override
    public void onClick(View v) {
        //close soft keyboard
        InputMethodManager imm =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        calculate();
    }

    /**
     * 截取字符串，去掉最后一个字符
     *
     * @param s
     * @return
     */
    private String trans(String s) {
        return s.substring(0, s.length() - 1);
    }

    /**
     * 校验输入数据是否为正实数
     *
     * @param s
     */
    private boolean check(String s) {
        boolean b = s.matches("^[1-9]\\d*(\\.\\d+)?$");
        Log.d("ClculatorActivity", b + "");
        if (!b) {//如果不匹配，正实数
            showAlert(s);
            return false;
        }
        return true;
    }

    private void showAlert(String s) {
//        AlertDialog dialog = new AlertDialog(this,s);
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(CalculatorActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("您输入的数据" + s + "格式错误，请重新输入");
        dialog.show();
    }

    private void clearData() {
        loanTotalTextView.setText("");
        repaymentMonthsTextView.setText("");
        totalInterestTextView.setText("");
        repaymentPerMonthTextView.setText("");
        totalPaymentTextView.setText("");
    }

}
