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

    private static class ViewHolder {
        TextView tvTitle;
        ImageView imageView;
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_result);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = this.getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(0);
        viewHolder.tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbnailImage();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(viewHolder.imageView);
        }

        return convertView;
    }
}
