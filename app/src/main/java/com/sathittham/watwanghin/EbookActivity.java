package com.sathittham.watwanghin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EbookActivity extends Activity {

	private ListView listView;
	private ImageAdapter imageAdapter;
	private Handler handler = new Handler();

	public static final int DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ProgressBar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_ebook);

		new LoadContentFromServer().execute();

	}

	public void ShowThumbnailData() {
		// ListView and imageAdapter
		listView = (ListView) findViewById(R.id.ebook_listview);
		listView.setClipToPadding(false);
		imageAdapter = new ImageAdapter(getApplicationContext());
		listView.setAdapter(imageAdapter);

	}

	// Download
	public void startDownload(final int position) {

		Runnable runnable = new Runnable() {
			int Status = 0;

			public void run() {

				String urlDownload = MyArrList.get(position)
						.get("ImagePathFull").toString();
				int count = 0;
				try {

					URL url = new URL(urlDownload);
					URLConnection conexion = url.openConnection();
					conexion.connect();

					int lenghtOfFile = conexion.getContentLength();
					Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

					InputStream input = new BufferedInputStream(
							url.openStream());

					// Get File Name from URL
					String fileName = urlDownload.substring(
							urlDownload.lastIndexOf('/') + 1,
							urlDownload.length());

					OutputStream output = new FileOutputStream(
							"/mnt/sdcard/mydata/" + fileName);

					byte data[] = new byte[1024];
					long total = 0;

					while ((count = input.read(data)) != -1) {
						total += count;
						Status = (int) ((total * 100) / lenghtOfFile);
						output.write(data, 0, count);

						// Update ProgressBar
						handler.post(new Runnable() {
							public void run() {
								updateStatus(position, Status);
							}
						});

					}

					output.flush();
					output.close();
					input.close();

				} catch (Exception e) {
				}

			}
		};
		new Thread(runnable).start();
	}

	private void updateStatus(int index, int Status) {

		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());

		// Update ProgressBar
		ProgressBar progress = (ProgressBar) v.findViewById(R.id.progressBar);
		progress.setProgress(Status);

		// Update Text to ColStatus
		TextView txtStatus = (TextView) v.findViewById(R.id.ColStatus);
		txtStatus.setPadding(10, 0, 0, 0);
		txtStatus.setText("โหลด : " + String.valueOf(Status) + "%");

		// Enabled Button View
		if (Status >= 100) {
			Button btnView = (Button) v.findViewById(R.id.btnView);
			btnView.setTextColor(Color.RED);
			btnView.setEnabled(true);
		}

	}

	// @Override
	// protected Dialog onCreatedDialog(int id) {
	// switch (id) {
	// case DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS:
	// mProgressDialog = new ProgressDialog(this);
	// mProgressDialog.setMessage("กำลังดาวโหลดเนื้อหา .....");
	// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// mProgressDialog.setCancelable(true);
	// return mProgressDialog;
	// default:
	// return null;
	// }
	// }

	class LoadContentFromServer extends AsyncTask<Object, Integer, Object> {

		protected void onPreExecute() {
			super.onPreExecute();
			// setProgressBarIndeterminateVisibility(true);
			// showDialog(DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS);
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Object doInBackground(Object... params) {

			String url = "http://www.sathittham.com/watwanghin/getJSON.php";

			JSONArray data;
			try {
				data = new JSONArray(getJSONUrl(url));

				MyArrList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> map;

				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					map = new HashMap<String, Object>();
					map.put("ID", (String) c.getString("ID"));
					map.put("Name", (String) c.getString("Name"));

					// Thumbnail Get ImageBitmap To Object
					map.put("ImagePathThum",
							(String) c.getString("ImageThumbPath"));
					map.put("ImageThumBitmap",
							(Bitmap) loadBitmap(c.getString("ImageThumbPath")));

					// Full File (for View Popup)
					map.put("ImagePathFull",
							(String) c.getString("ImageFullPath"));

					MyArrList.add(map);
					publishProgress(i);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

//		@Override
//		public void onProgressUpdate(Integer... progress) {
//			imageAdapter.notifyDataSetChanged();
//		}

		@Override
		protected void onPostExecute(Object result) {
			setProgressBarIndeterminateVisibility(false);
			ShowThumbnailData();
			// dismissDialog(DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS);
			// removeDialog(DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS);
		}
	}

	class ImageAdapter extends BaseAdapter {

		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return MyArrList.size();
		}

		public Object getItem(int position) {
			return MyArrList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ebook_column, null);
			}

			// ColImage
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.ColImgPath);
			imageView.getLayoutParams().height = 160;
			imageView.getLayoutParams().width = 160;
			imageView.setPadding(10, 10, 10, 10);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			try {
				imageView.setImageBitmap((Bitmap) MyArrList.get(position).get(
						"ImageThumBitmap"));
			} catch (Exception e) {
				// When Error
				imageView
						.setImageResource(android.R.drawable.ic_menu_report_image);
			}

			// ColImgID
			TextView txtImgID = (TextView) convertView
					.findViewById(R.id.ColImgID);
			txtImgID.setPadding(10, 0, 0, 0);
			txtImgID.setText("รหัส : "
					+ MyArrList.get(position).get("ID").toString());

			// ColImgName
			TextView txtPicName = (TextView) convertView
					.findViewById(R.id.ColImgName);
			txtPicName.setPadding(10, 0, 0, 0);
			txtPicName.setText("ชื่อ : "
					+ MyArrList.get(position).get("Name").toString());

			// ColStatus
			TextView txtStatus = (TextView) convertView
					.findViewById(R.id.ColStatus);
			txtStatus.setPadding(10, 0, 0, 0);
			txtStatus.setText("...");

			// btnDownload
			final Button btnDownload = (Button) convertView
					.findViewById(R.id.btnDownload);
			btnDownload.setTextColor(Color.RED);
			btnDownload.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					btnDownload.setEnabled(false);
					btnDownload.setTextColor(Color.GRAY);

					startDownload(position);

				}
			});

			// btnView
			Button btnView = (Button) convertView.findViewById(R.id.btnView);
			btnView.setEnabled(false);
			btnView.setTextColor(Color.GRAY);
			btnView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ViewImageSDCard(position);

				}
			});

			// progressBar
			ProgressBar progress = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			progress.setPadding(10, 0, 10, 0);

			return convertView;

		}

	}

	// View Image from SD Card
	public void ViewImageSDCard(int position) {
		final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.ebook_full_reader,
				(ViewGroup) findViewById(R.id.layout_root));
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);

		String urlDownload = MyArrList.get(position).get("ImagePathFull")
				.toString();

		// Get File Name from URL
		String fileName = urlDownload.substring(
				urlDownload.lastIndexOf('/') + 1, urlDownload.length());

		String strPath = "/mnt/sdcard/mydata/" + fileName;
		Bitmap bm = BitmapFactory.decodeFile(strPath); // Path from SDCard
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);

		image.setImageBitmap(bm);

		String strName = MyArrList.get(position).get("Name").toString();
		imageDialog.setIcon(android.R.drawable.btn_star_big_on);
		imageDialog.setTitle("View : " + strName);
		imageDialog.setView(layout);
		imageDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		imageDialog.create();
		imageDialog.show();
	}

	/***** Get Image Resource from URL (Start) *****/
	private static final String TAG = "Image";
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 1;

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (IOException e) {
			Log.e(TAG, "Could not load Bitmap from: " + url);
		} finally {
			closeStream(in);
			closeStream(out);
		}

		return bitmap;
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Log.e(TAG, "Could not close stream", e);
			}
		}
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/***** Get Image Resource from URL (End) *****/

	/*** Get JSON Code from URL ***/
	public String getJSONUrl(String url) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Download OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download file..");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}

}
