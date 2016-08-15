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
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rnewton on 8/8/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder {
        TextView tvTitle;
        ImageView imageView;
        TextView tvSnippet;
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_normal);
    }

    public enum ArticleStyle {
        NORMAL, NO_THUMBNAIL, HIGHLIGHTED
    }

    @Override
    public int getItemViewType(int position) {
        Article article = getItem(position);
        if (article.getThumbnailImage().isEmpty()) {
            return ArticleStyle.NO_THUMBNAIL.ordinal();
        } else if (position % 4 == 0) {
            return ArticleStyle.HIGHLIGHTED.ordinal();
        }
        return ArticleStyle.NORMAL.ordinal();
    }

    @Override public int getViewTypeCount() {
        return ArticleStyle.values().length;
    }

    private View getInflatedLayoutForType(int type) {
        if (type == ArticleStyle.HIGHLIGHTED.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_article_highlighted, null);
        } else if (type == ArticleStyle.NO_THUMBNAIL.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_article_no_image, null);
        } else
            return LayoutInflater.from(getContext()).inflate(R.layout.item_article_normal, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = this.getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            int type = getItemViewType(position);
            convertView = getInflatedLayoutForType(type);

            if (!article.getThumbnailImage().isEmpty()) {
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            }
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//            viewHolder.tvSnippet = (TextView) convertView.findViewById(R.id.tvSnippet);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!article.getThumbnailImage().isEmpty()) {
            viewHolder.imageView.setImageResource(0);
        }

        viewHolder.tvTitle.setText(article.getHeadline());
//        viewHolder.tvSnippet.setText(article.getSnippet());
        String thumbnail = article.getThumbnailImage();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext())
                    .load(thumbnail)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .fit()
                    .centerInside()
                    .into(viewHolder.imageView);
        }
        return convertView;
    }
}
