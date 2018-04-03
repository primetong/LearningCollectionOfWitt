package cn.gx.model;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class ArticleInfo {

	public void convert(Article article) {
		setTitle(article.getTitle());
		setAid(article.getAid());
		setCreationdate(article.getCreationdate());
		setUid(article.getUid());

		/* 获取摘要以及预览图 */
		if (article.getContent() != null) {
			Document document = Jsoup.parse(article.getContent());
			if (document != null) {
				String text = document.text();
				setContent(text.substring(0, Math.min(200, text.length())));
				
				Element image = document.select("img").first();
				if (image != null) {					
					String url = image.absUrl("src");
					setPreview(url);
				}
			}
		}
	}
	
    public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	private Integer aid;

    private Integer uid;
    
    private String author;

    private String title;
    
    private String preview = "imgs/default_article_icon.png";

    private Date creationdate;

    private String content;
}


/*
 * [Jsoup]
 * (http://tool.oschina.net/apidocs/apidoc?api=jsoup-1.6.3) 
 */
