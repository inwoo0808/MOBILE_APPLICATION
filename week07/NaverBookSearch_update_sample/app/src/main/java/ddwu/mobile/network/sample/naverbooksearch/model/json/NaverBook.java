package ddwu.mobile.network.sample.naverbooksearch.model.json;

import android.text.Html;
import android.text.Spanned;

public class NaverBook {

    private int id;
    private String title;
    private String author;
    private String link;
    private String image;
    private String imageFileName;       // 외부저장소에 저장했을 때의 파일명

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getTitle() {
        Spanned spanned = Html.fromHtml(title);     // HTML 태그 제거
        return spanned.toString();
//        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " (" + author + ')';
    }
}
