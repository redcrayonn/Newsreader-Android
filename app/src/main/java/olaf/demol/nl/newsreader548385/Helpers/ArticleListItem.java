package olaf.demol.nl.newsreader548385.Helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import olaf.demol.nl.newsreader548385.R;
import olaf.demol.nl.newsreader548385.net.Models.Article;
import olaf.demol.nl.newsreader548385.net.Models.User;

public class ArticleListItem extends RecyclerView.ViewHolder {
    private final TextView titleView;
    private final TextView descriptionView;
    private final ImageView imageView;

    public ArticleListItem(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.article_image);
        titleView = itemView.findViewById(R.id.article_title);
        descriptionView = itemView.findViewById(R.id.article_summary);
    }

    public void fill(final Context context, final Article article) {
        String url = article.getImage();
        Picasso.with(context)
                .load(url)
                .error(R.drawable.ic_action_favorite)
                .placeholder(R.drawable.ic_action_favorite)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setImageTintList(null);
                        imageView.clearColorFilter();
                    }

                    @Override
                    public void onError() {
                        imageView.setColorFilter(ContextCompat.getColor(context, R.color.primaryColor));
                    }
                });

        titleView.setText(article.getTitle());

        if (User.isLoggedIn() && article.getIsLiked()) {
            titleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
        } else {
            titleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }

        // Summary
        descriptionView.setText(article.getSummary());
    }
}
