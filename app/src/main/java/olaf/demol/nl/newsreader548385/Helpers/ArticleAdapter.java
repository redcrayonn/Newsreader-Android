package olaf.demol.nl.newsreader548385.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import olaf.demol.nl.newsreader548385.R;
import olaf.demol.nl.newsreader548385.net.Models.Article;
import olaf.demol.nl.newsreader548385.net.Models.Results.ArticleResult;
import olaf.demol.nl.newsreader548385.net.Models.User;
import olaf.demol.nl.newsreader548385.net.Services.ArticleService;
import olaf.demol.nl.newsreader548385.net.WebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by olaf on 15-10-17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleListItem> implements Callback<ArticleResult> {

    // Property fields
    private final ProgressBar progressBar;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final ListItemClick listItemClickListener;
    private final ArticleService articleService;

    // data storage
    private final List<Article> items;

    // Flag fields
    private int nextId = -1;
    private boolean loading;

    public ArticleAdapter(Context context, List<Article> items, ProgressBar progressBar) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.listItemClickListener = (ListItemClick) context;
        this.progressBar = progressBar;

        WebClient restClient = new WebClient();
        articleService = restClient.createArticleService();
    }

    public Article getItem(int position) {
        return items.get(position);
    }

    public void updateArticle(Article article) {
        for (int i = 0; i < items.size(); i++) {
            // get the item for the collection based on the current loop index
            Article inList = getItem(i);
            // check article id's
            if (inList.getId() == article.getId()) {
                // found the item to update
                items.set(i, article);
            }
        }
    }

    public void reload() {
        this.items.clear();
        nextId = -1;
        onLoadMore();
        refresh();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public ArticleListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create the view and viewholder
        View view = layoutInflater.inflate(R.layout.article_listitem, parent, false);
        return new ArticleListItem(view);
    }

    @Override
    public void onBindViewHolder(final ArticleListItem holder, int position) {
        // get model data from list based on position loaded
        final Article article = getItem(position);

        // Fill view holder
        holder.fill(context, article);

        // click handler for list item
        final int clickedItem = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemClickListener.onItemClick(view, clickedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onLoadMore() {
        // add new item for continuous scrolling
        // load from backend here
        if (loading) return;
        loading = true;

        // Show the loading icon if the action takes longer than 'loadingThresholdMs'
        int loadingThresholdMs = 100;
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loading)
                    progressBar.setVisibility(View.VISIBLE);
            }
        }, loadingThresholdMs);

        // service layer call
        if (nextId < 0) {
            articleService.getArticles(User.getAuthtoken(), null, null, null, null).enqueue(this);
        } else {
            articleService.getArticle(User.getAuthtoken(), nextId, 20, null, null, null).enqueue(this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<ArticleResult> call, @NonNull Response<ArticleResult> response) {
        if (response.isSuccessful() && response.body() != null) {
            ArticleResult result = response.body();
            nextId = result.getNextId();
            // Fill the list of items
            for (Article article : result.getResults()) {
                // save to local collection
                items.add(article);
            }
            notifyDataSetChanged();
        }
        clearRequest();
    }

    @Override
    public void onFailure(@NonNull Call<ArticleResult> call, @NonNull Throwable t) {
        // log failure here
        Log.e(this.getClass().toString().toUpperCase(), "Tried to connect, but something went wrong", t);
        clearRequest();
        //TODO: Show message here
    }

    private void clearRequest() {
        loading = false;
        // Hide the progressbar
        progressBar.setVisibility(View.GONE);
    }
}

