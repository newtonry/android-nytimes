package com.fadetoproductions.rvkn.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rnewton on 8/8/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(0);
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getHeadline());
        String thumbnail = article.getThumbnailImage();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            imageView = (ImageView) view.findViewById(R.id.ivImage);
        }

    }

    private ArrayList<Article> articles;
    private Context context;

    public ArticleArrayAdapter(Context context, ArrayList<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }



//    public ArticleArrayAdapter(Context context, List<Article> articles) {
//        super(context, R.layout.item_article_result);
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Article article = this.getItem(position);
//        ViewHolder viewHolder;
//
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
//
//            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
//            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//            convertView.setTag(viewHolder);
//
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.imageView.setImageResource(0);
//        viewHolder.tvTitle.setText(article.getHeadline());
//
//        String thumbnail = article.getThumbnailImage();
//        if (!TextUtils.isEmpty(thumbnail)) {
//            Picasso.with(getContext()).load(thumbnail).into(viewHolder.imageView);
//        }
//
//        return convertView;
//    }
}
