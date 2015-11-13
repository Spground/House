package jc.house.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jc.house.R;

/**
 * Created by WuJie on 2015/11/13.
 */
public class AboutFragment extends JCNetFragment {

    private View view;
    private TextView contentTextView;

    private String htmlSrc = "<<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "      <title></title>\n" +
            "      <meta charset=\"utf-8\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "      <div style=\"width: 640px; margin-left: 30px;\">\n" +
            "            <p>\n" +
            "                  &nbsp; &nbsp; 大连金宸集团成立于2004年，<strong>是集房地产开发</strong>（连大房地产开发有限责任公司、君达房地产开发有限公司、金宸房地产开发有限公司、金宸德誉置业有限公司、金宸文润置业有限公司）、<strong>建筑工程</strong>（国家一级建筑资质）、<strong>装饰装修</strong>、<strong>工业发展</strong>（占地1500亩金宸<strong>生态科技产业园</strong>以“生态”、“科技”、“创新”为开发建设主导思想，采用新思路、新体制与新机制推进整体的开发建设）以及<strong>物业管理服务</strong>于一体的<strong>多元化企业集团</strong>。</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp;&nbsp; 集团员工3000余人，其中高、中级技术职称人员500多名，总资产45亿元，年总产值30个亿，年纳税近1亿元。&nbsp;&nbsp;</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp;&nbsp; 集团自成立以来，以自身良好的资信建设、深厚的企业文化、雄厚的开发实力、其独特的建筑设计、专业的施工队伍、优良的建筑品质、贴心的物业管理和强大的品牌影响力，为提升大连人居环境做出了积极贡献，赢得了社会各界广泛赞誉。</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp;&nbsp; 集团先后开发建设了蓝天星海、经济适用住房、“连大·文润金宸”、 “金宸·蓝郡”、“金宸·联郡”、“金宸·晟府”等十几个项目，在行业内具有一定影响力。先后荣获了“中国住宅百强企业”、 “辽宁省重合同守信用单位”、 “辽宁省房地产行业30强企业” 、“大连市AAA级信用企业”、 “大连市房地产行业优秀会员单位”、 “纳税大户” “市民最信赖地产开发企业”、“大连市消费者满意单位”等荣誉。</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp;&nbsp; 金宸集团坚持“以质量求生存，以诚信求发展”的发展理念，积极打造奋发进取的职业经理团队，秉承“诚信、勤奋、创新、发展”共享企业发展成果的企业文化，以高度的责任感和使命感，竭诚与社会各界携手并进，共谋发展，共创美好未来。</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp; 在取得成绩的同时不忘投身公益慈善事业，集团成立先后设立金宸集团教育慈善基金；马国君老董事长个人慈善基金；马金玲董事长金州新区扶贫帮困慈善资金，10年来捐助善款善物达1.5亿元左右，赢得社会各界广泛的赞誉和好评。</p>\n" +
            "            <p>\n" +
            "                  &nbsp;&nbsp; 在实现金宸集团愿景的征程上，金宸集团会为加盟的每一位员工提供一片任意翱翔的天空，让大家尽展才华，实现梦想。回眸过去，我们欣慰有加；展望未来，我们任重而道远。我们有信心勇敢面对征途上的挑战，您将会看到更多的金宸的项目、金宸的品牌闪耀于滨城！</p>\n" +
            "            <p>\n" +
            "                  &nbsp;</p>\n" +
            "      </div>\n" +
            "</body>\n" +
            "</html>";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_about, container, false);
        return this.view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contentTextView = (TextView)this.view.findViewById(R.id.about_content_tv);
        init();
    }

    private void init(){
        if(this.contentTextView != null){
            this.contentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
            this.contentTextView.setText(Html.fromHtml(this.htmlSrc));
        }
    }
}
