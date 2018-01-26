package data.Parsers;

import org.jsoup.nodes.Document;

/**
 * Created by Mr.Nobody43 on 15.01.2018.
 */

public interface IParser {

    public Document DownloadSchedule(String query, String semestr, String porok);
    public void ParseDocument(Document document);

}
