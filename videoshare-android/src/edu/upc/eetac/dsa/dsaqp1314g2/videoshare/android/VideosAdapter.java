package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.R;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.Videos;

public class VideosAdapter extends BaseAdapter {

	private ArrayList<Videos> data;
	private LayoutInflater inflater;

	private static class ViewHolder {
		TextView tvTitulo;
		TextView tvUsername;
		TextView tvView;
		// TextView tvDate;
	}

	public VideosAdapter(Context context, ArrayList<Videos> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {// devuelve numero total de filas que habria en la
							// lista , numero de datos q tu muestras
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {// modelo, cada posicion de la lista
											// tiene un modelo de la cual tiene
											// una serie de datos
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {// devuelve valor unico para una
											// determinada posicion
		// TODO Auto-generated method stub
		return Long.parseLong(((Videos) getItem(position)).getVideoid());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {// devueve
																			// ese
																			// layout
																			// qe
																			// hemos
																			// creado
																			// cn
																			// datos
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_videos, null);// listrowsting
																			// que
																			// habiamos
																			// creado
			viewHolder = new ViewHolder();
			viewHolder.tvTitulo = (TextView) convertView
					.findViewById(R.id.tvSubject);
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvView = (TextView) convertView
					.findViewById(R.id.tvDate);
			// viewHolder.tvDate = (TextView) convertView
			// .findViewById(R.id.tvDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String subject = data.get(position).getNombre_video();// recuperando
		String puntuacio = data.get(position).getPuntuacion();													// valores de
		//String view = data.get(position).get														// esa posicion
		String username = data.get(position).getUsername();
		
		// EN esta linea hay algo raro
		// String date = SimpleDateFormat.getInstance().format(data);
		viewHolder.tvTitulo.setTextColor(Color.BLACK);
		viewHolder.tvTitulo.setTextSize(25);
		viewHolder.tvTitulo.setTypeface(null, Typeface.BOLD_ITALIC);;
		viewHolder.tvTitulo.setText(subject);
		viewHolder.tvUsername.setText(username);
		viewHolder.tvUsername.setTextColor(Color.WHITE);
		viewHolder.tvUsername.setTypeface(null, Typeface.ITALIC);
		viewHolder.tvUsername.setTextSize(19);
		viewHolder.tvView.setText(puntuacio);
		viewHolder.tvView.setTextSize(25);
		viewHolder.tvView.setTextColor(Color.BLACK);
		viewHolder.tvView.setTypeface(null, Typeface.BOLD_ITALIC);
		// viewHolder.tvDate.setText(date);
		return convertView;
	}

}
