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
import com.squareup.picasso.Picasso;

import java.util.List;

import jc.house.R;
import jc.house.chat.model.ChatUser;
import jc.house.global.Constants;
import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.utils.GeneralUtils;
import jc.house.views.CircleView;
import jc.house.views.RatingView;

public class ListAdapter extends BaseAdapter {
	private Context context;
	private List<? extends BaseModel> dataSet;
	private ModelType type;
	private CircleView circleView;
	private boolean hasCircleView;
	private static final boolean DEBUG = Constants.DEBUG;
	
	public ListAdapter(Context context, List<? extends BaseModel> dataSet, ModelType modelType) {
		this(context, dataSet, modelType, null);
	}
	
	public ListAdapter(Context context, List<? extends BaseModel> dataSet, ModelType modelType, CircleView circleView) {
		this.context = context;
		this.dataSet = dataSet;
		this.type = modelType;
		this.circleView = circleView;
		this.hasCircleView = (null != this.circleView);
	}

	@Override
	public int getCount() {
		return this.dataSet.size() + (this.hasCircleView ? 1 : 0);
	}

	@Override
	public Object getItem(int pos) {
		if(this.hasCircleView && 0 == pos) {
			return this.circleView;
		} else if(this.hasCircleView) {
			return this.dataSet.get(pos - 1);
		} else {
			return this.dataSet.get(pos);
		}
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		if(pos == 0 && this.hasCircleView) {
			convertView = circleView;
		} else {
			final int mPos = this.hasCircleView ? pos - 1 : pos;
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
					ChatUser user = (ChatUser)this.dataSet.get(mPos);
					viewHolderChatUser.portrait.setImageResource(R.drawable.qq);
					viewHolderChatUser.name.setText(user.getName());
					viewHolderChatUser.msg.setText(user.getMsg());
					viewHolderChatUser.time.setText(user.getTime());
					break;
				case NEWS:
					ViewHolderNews viewHolderNews;

					if(null == convertView || null == convertView.getTag()) {
						convertView = LayoutInflater.from(context).inflate(R.layout.listview_news_item, parent, false);
						viewHolderNews = new ViewHolderNews();
						viewHolderNews.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderNews.author = (TextView)convertView.findViewById(R.id.author);
						viewHolderNews.title = (TextView)convertView.findViewById(R.id.title);
						viewHolderNews.date = (TextView)convertView.findViewById(R.id.date);
						convertView.setTag(viewHolderNews);
					} else {
						viewHolderNews = (ViewHolderNews)convertView.getTag();
					}
					News news = (News)this.dataSet.get(mPos);
					if (DEBUG) {
						viewHolderNews.picture.setImageResource(Integer.valueOf(news.getPicUrl().trim()));
					} else {
						loadImageWithPicasso(viewHolderNews.picture, news.getPicUrl());
					}
					viewHolderNews.author.setText(news.getAuthor());
					viewHolderNews.title.setText(news.getTitle());
					viewHolderNews.date.setText(news.getTime());
					break;
				case HOUSE:
					ViewHolderHouse viewHolderHouse;
					if(null == convertView) {
						convertView = LayoutInflater.from(context).inflate(R.layout.listview_house_item, parent, false);
						viewHolderHouse = new ViewHolderHouse();
						viewHolderHouse.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderHouse.name = (TextView)convertView.findViewById(R.id.name);
						viewHolderHouse.description = (TextView)convertView.findViewById(R.id.description);
						viewHolderHouse.phone = (TextView)convertView.findViewById(R.id.phone);
						viewHolderHouse.ratingView = (RatingView)convertView.findViewById(R.id.rating_view);
						convertView.setTag(viewHolderHouse);
					} else {
						viewHolderHouse = (ViewHolderHouse)convertView.getTag();
					}
					House house = (House)this.dataSet.get(mPos);
					if (DEBUG) {
						viewHolderHouse.picture.setImageResource(Constants.resHouse[(int) (Math.random() * 4)]);
					} else {
						loadImageWithPicasso(viewHolderHouse.picture, house.getUrl());
					}
					viewHolderHouse.name.setText(house.getName());
					viewHolderHouse.description.setText(house.getIntro());
					viewHolderHouse.ratingView.setParams(house.getStars(), 3);
					viewHolderHouse.phone.setText(house.getPhone());
					break;
				case ACTIVITY:
					JCActivity activityModel = (JCActivity)this.dataSet.get(mPos);
					ViewHolderActivity viewHolderActivity;
					if(null == convertView || convertView.getTag() == null) {
						convertView = LayoutInflater.from(context).inflate(R.layout.listview_activity_item, parent, false);
						viewHolderActivity = new ViewHolderActivity();
						viewHolderActivity.picture = (ImageView)convertView.findViewById(R.id.picture);
						viewHolderActivity.title = (TextView)convertView.findViewById(R.id.title);
						viewHolderActivity.postTime = (TextView)convertView.findViewById(R.id.post_time);
						convertView.setTag(viewHolderActivity);
					} else {
						viewHolderActivity = (ViewHolderActivity)convertView.getTag();
					}

					if(DEBUG) {
						viewHolderActivity.picture.setImageResource(Constants.resActivity[(int) (Math.random() * 5)]);
						viewHolderActivity.title.setText(activityModel.getTitle());
						viewHolderActivity.postTime.setText("2012-08-12");
					} else {
						loadImageWithPicasso(viewHolderActivity.picture, activityModel.getPicUrl());
						viewHolderActivity.title.setText(activityModel.getTitle());
						convertView.setId(activityModel.id);
						viewHolderActivity.postTime.setText(GeneralUtils.getDateString(activityModel.getPostTime()));
					}
					break;
				default:
					break;
			}
		}
		return convertView;
	}

	private void loadImageWithPicasso(ImageView imageView, String url) {
		Picasso.with(context).load(Constants.IMAGE_URL + url).into(imageView);
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
		public RatingView ratingView;
	}

	private static final class ViewHolderActivity {
		public ImageView picture;
		public TextView title;
		public TextView postTime;
	}

}
