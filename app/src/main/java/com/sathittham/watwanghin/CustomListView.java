package com.sathittham.watwanghin;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListView extends BaseAdapter {

	String[] result;
	Context context;
	int[] imageId;
	private static LayoutInflater inflater = null;

	public CustomListView(MainActivity mainActivity, String[] menuList,
			int[] menuImages) {
		result = menuList;
		context = mainActivity;
		imageId = menuImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.listview_layout, null);
		holder.tv = (TextView) rowView.findViewById(R.id.menuText);
		holder.img = (ImageView) rowView.findViewById(R.id.menuIcon);
		holder.tv.setText(result[position]);
		holder.img.setImageResource(imageId[position]);
		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, result[position], Toast.LENGTH_SHORT)
						.show();

				switch (position) {
				case 0: // News
					Intent intent0 = new Intent(context, NewsActivity.class);
					context.startActivity(intent0);
					break;

				case 1: // VDO
					Intent intent1 = new Intent(context, VDOActivity.class);
					context.startActivity(intent1);
					break;
				case 2: // Sound
					Intent intent2 = new Intent(context, SoundActivity.class);
					context.startActivity(intent2);
					break;
				case 3: // E-Book
					Intent intent3 = new Intent(context, EbookActivity.class);
					context.startActivity(intent3);
					break;
				case 4:
					Intent intent4 = new Intent(context, CalendarActivity.class);
					context.startActivity(intent4);
					break;
				}
			}

		});
		return rowView;
	}

}
