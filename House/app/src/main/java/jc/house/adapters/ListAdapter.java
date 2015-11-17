package jc.house.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.models.BaseModel;
import jc.house.models.ChatUser;
import jc.house.models.House;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.views.CircleView;

public class ListAdapter<T extends BaseModel> extends BaseAdapter {
	private Context context;
	private List<T> lists;
	private ModelType type;
	private CircleView circleView;
	private boolean hasCircleView;
	private DisplayImageOptions options;
	
	public ListAdapter(Context context, List<T> lists, ModelType modelType) {
		this(context, lists, modelType, null);
	}
	
	public ListAdapter(Context context, List<T> lists, ModelType modelType, CircleView circleView) {
		this.context = context;
		this.lists = lists;
		this.type = modelType;
		this.circleView = circleView;
		this.hasCircleView = (null != this.circleView);
		this.options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.caodi).showImageForEmptyUri(R.drawable.caodi).build();
	}

	@Override
	public int getCount() {
		return this.lists.size() + (this.hasCircleView ? 1 : 0);
	}

	@Override
	public Object getItem(int pos) {
		if(this.hasCircleView && 0 == pos) {
			return this.circleView;
		} else if(this.hasCircleView) {
			return this.lists.get(pos - 1);
		} else {
			return this.lists.get(pos);
		}
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(pos == 0 && this.hasCircleView) {
			convertView = circleView;
		} else {
			int mPos = this.hasCircleView ? pos - 1 : pos;
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
					viewHolderChatUser.portrait.setImageResource(R.drawable.user_mao);
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
					viewHolderNews.picture.setImageResource(R.drawable.caodi);
					viewHolderNews.author.setText(news.getAuthor());
					viewHolderNews.title.setText(news.getTitle());
					viewHolderNews.date.setText(news.getDate());
					break;
				case HOUSE:
					ViewHolderHouse viewHolderHouse;
					if(null == convertView) {
						convertView = LayoutInflater.from(context).inflate(R.layout.house_list_item, parent, false);
						viewHolderHouse = new ViewHolderHouse();
						viewHolderHouse.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderHouse.name = (TextView)convertView.findViewById(R.id.name);
						viewHolderHouse.description = (TextView)convertView.findViewById(R.id.description);
						viewHolderHouse.phone = (TextView)convertView.findViewById(R.id.phone);
						convertView.setTag(viewHolderHouse);
					} else {
						viewHolderHouse = (ViewHolderHouse)convertView.getTag();
					}
					House house = (House)this.lists.get(mPos);
					loadImage(viewHolderHouse.picture, house.getUrl());
					viewHolderHouse.name.setText(house.getName());
					viewHolderHouse.description.setText(house.getIntro());
					viewHolderHouse.phone.setText(house.getPhone());
					break;
				case ACTIVITY:
					ViewHolderActivity viewHolderActivity;
					if(null == convertView) {
						convertView = LayoutInflater.from(context).inflate(R.layout.activities_list_item, parent, false);
						viewHolderActivity = new ViewHolderActivity();
						viewHolderActivity.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderActivity.title = (TextView)convertView.findViewById(R.id.title);
						convertView.setTag(viewHolderActivity);
					} else {
						viewHolderActivity = (ViewHolderActivity)convertView.getTag();
					}
					JCActivity activity = (JCActivity)this.lists.get(mPos);
					viewHolderActivity.picture.setImageResource(R.drawable.caodi);
					viewHolderActivity.title.setText(activity.getName());
					break;
				default:
					break;
			}
		}
		return convertView;
	}

	private void loadImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(
				Constants.IMAGE_URL + url,
				imageView, options);
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

	private static final class ViewHolderHouse {
		public TextView name;
		public ImageView picture;
		public TextView description;
		public TextView phone;
	}

	private static final class ViewHolderActivity {
		public ImageView picture;
		public TextView title;
	}

}
