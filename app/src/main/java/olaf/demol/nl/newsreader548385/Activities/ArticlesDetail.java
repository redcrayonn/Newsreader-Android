package olaf.demol.nl.newsreader548385.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import olaf.demol.nl.newsreader548385.R;
import olaf.demol.nl.newsreader548385.net.Models.Article;
import olaf.demol.nl.newsreader548385.net.Models.User;
import olaf.demol.nl.newsreader548385.net.Services.ArticleService;
import olaf.demol.nl.newsreader548385.net.WebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class ArticlesDetail extends AppCompatActivity implements View.OnClickListener {

    public static final String VIEW_NAME_ARTICLE_IMAGE = "view_name_article_image";
    public static final String VIEW_NAME_ARTICLE_TITLE = "view_name_article_title";
    public static final String VIEW_NAME_ARTICLE_SUMMARY = "view_name_article_summary";

    private ToggleButton likeButton;
    private ImageView imageView;
    private TextView publishView;
    private TextView titleView;
    private TextView summaryView;

    private Article article;
    private ArticleService articleService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_detail);

        context = this;

        WebClient rest = new WebClient();
        articleService = rest.createArticleService();

        initViews();

        Intent intent = getIntent();
        article = intent.getParcelableExtra(ArticlesMain.INTENT_ARTICLE);

        if (article == null) return;

        String url = article.getImage();
        Picasso.with(this).load(url).into(imageView);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        publishView.setText(df.format(article.getPublishDate()));
        titleView.setText(String.format(Locale.getDefault(), "%s", article.getTitle()));
        summaryView.setText(String.format(Locale.getDefault(), "%s", article.getSummary()));
        likeButton.setChecked(article.getIsLiked());
    }

    private void initViews() {
        Button readMoreBtn = findViewById(R.id.read_more_btn);
        readMoreBtn.setOnClickListener(this);

        imageView = findViewById(R.id.article_image);
        publishView = findViewById(R.id.article_publish_date);
        titleView = findViewById(R.id.article_title);
        summaryView = findViewById(R.id.article_summary);

        likeButton = findViewById(R.id.like_btn);
        likeButton.setOnClickListener(this);
        if (User.isLoggedIn()) {
            likeButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                share();
                return true;
            case android.R.id.home:
                setupDataPassback();
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_more_btn:
                openArticle();
                break;
            case R.id.like_btn:
                likeArticle();
                break;
            default:
                break;
        }
    }

    private void setupDataPassback() {
        Intent backIntent = new Intent();
        backIntent.putExtra(ArticlesMain.INTENT_ARTICLE, article);
        setResult(RESULT_OK, backIntent);
    }

    private void likeArticle() {
        if (!article.getIsLiked()) {
            articleService.putLikeArticle(User.getAuthtoken(), article.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(context, getResources().getString(R.string.like), Toast.LENGTH_SHORT).show();
                    //set prop
                    article.setIsLiked(true);
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
                    likeButton.setChecked(true);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite_border, 0);
                    likeButton.setChecked(false);
                }
            });
        } else {
            articleService.deleteLikeArticle(User.getAuthtoken(), article.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(context, getResources().getString(R.string.unlike), Toast.LENGTH_SHORT).show();
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite_border, 0);
                    article.setIsLiked(false);
                    likeButton.setChecked(false);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
                    likeButton.setChecked(true);
                }
            });
        }
    }

    public void openArticle() {
        Intent openUrlIntent = new Intent();
        openUrlIntent.setData(Uri.parse(article.getUrl()));
        openUrlIntent.setAction(Intent.ACTION_VIEW);
        startActivity(openUrlIntent);
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getSummary());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
    }
}
