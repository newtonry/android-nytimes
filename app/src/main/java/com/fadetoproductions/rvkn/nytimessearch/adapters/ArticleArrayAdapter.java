package com.fadetoproductions.rvkn.nytimessearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by rnewton on 8/8/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbnailImage();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;

//        return super.getView(position, convertView, parent);
    }
}
