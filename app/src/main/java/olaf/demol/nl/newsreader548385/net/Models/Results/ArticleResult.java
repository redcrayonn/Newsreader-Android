package olaf.demol.nl.newsreader548385.net.Models.Results;

import java.util.ArrayList;

import olaf.demol.nl.newsreader548385.net.Models.Article;

/**
 * Created by olaf on 15-10-17.
 */


public class ArticleResult {
    private ArrayList<Article> Results;
    private int NextId;

    public ArrayList<Article> getResults() {
        return this.Results;
    }

    public void setResults(ArrayList<Article> Results) {
        this.Results = Results;
    }

    public int getNextId() {
        return this.NextId;
    }

    public void setNextId(int NextId) {
        this.NextId = NextId;
    }
}
