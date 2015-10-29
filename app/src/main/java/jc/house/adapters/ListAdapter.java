package jc.house.adapters;

import java.util.List;

import jc.house.R;
import jc.house.models.BaseModel;
import jc.house.models.ChatUser;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.views.CircleView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter<T extends BaseModel> extends BaseAdapter {
	private Context context;
	private List<T> lists;
	private ModelType type;
	private CircleView circleView;
	private boolean hasCircleView;
	
	public ListAdapter(Context context, List<T> lists, ModelType modelType) {
		this.context = context;
		this.lists = lists;
		this.type = modelType;
		this.hasCircleView = false;
	}
	
	public ListAdapter(Context context, List<T> lists, ModelType modelType, CircleView circleView) {
		this.context = context;
		this.lists = lists;
		this.type = modelType;
		this.circleView = circleView;
		this.hasCircleView = (null == this.circleView);
	}

	@Override
	public int getCount() {
		return this.lists.size() + (this.hasCircleView ? 1 : 0);
	}

	@Override
	public Object getItem(int pos) {
		if(this.hasCircleView && 0 == pos) {
			return this.circleView;
		} else {
			return this.lists.get(pos - 1);
		}
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(pos == 0 && null != circleView) {
			convertView = circleView;
		} else {
			int mPos = this.hasCircleView ? pos -1 : pos;
			switch(type) {
				case CHAT_USER:
					ViewHolderChatUser viewHolderChatUser;
					if(null == convertView) {
						convertView = LayoutInflater.from(context).inflate(R.layout.chat_user_list_item, parent, false);
						viewHolderChatUser = new ViewHolderChatUser();
						viewHolderChatUser.portrait = (ImageView)convertView.findViewById(R.id.portrait);
						viewHolderChatUser.name = (TextView)convertView.findViewById(R.id.name);
						viewHolderChatUser.msg = (TextView)convertView.findViewById(R.id.msg);
						viewHolderChatUser.time = (TextView)convertView.findViewById(R.id.time);
						convertView.setTag(viewHolderChatUser);
					} else {
						viewHolderChatUser = (ViewHolderChatUser)convertView.getTag();
					}
					ChatUser user = (ChatUser)this.lists.get(mPos);
					viewHolderChatUser.portrait.setImageResource(user.getImageResId());
					viewHolderChatUser.name.setText(user.getName());
					viewHolderChatUser.msg.setText(user.getMsg());
					viewHolderChatUser.time.setText(user.getTime());
					break;
				case NEWS:
					ViewHolderNews viewHolderNews;
					if(null == convertView) {
						convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);
						viewHolderNews = new ViewHolderNews();
						viewHolderNews.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderNews.author = (TextView)convertView.findViewById(R.id.author);
						viewHolderNews.title = (TextView)convertView.findViewById(R.id.title);
						viewHolderNews.date = (TextView)convertView.findViewById(R.id.date);
						convertView.setTag(viewHolderNews);
					} else {
						viewHolderNews = (ViewHolderNews)convertView.getTag();
					}
					News news = (News)this.lists.get(mPos);
					viewHolderNews.picture.setImageResource(R.drawable.user_mao);
					viewHolderNews.author.setText(news.getAuthor());
					viewHolderNews.title.setText(news.getTitle());
					viewHolderNews.date.setText(news.getDate());
					break;
				case HOUSE:
					break;
				default:
					break;
			}
		}
		return convertView;
	}
	
	private static final class ViewHolderChatUser {
		public ImageView portrait;
		public TextView name;
		public TextView msg;
		public TextView time;
	}
	
	private static final class ViewHolderNews {
		public TextView title;
		public ImageView picture;
		public TextView author;
		public TextView date;
	}

}
