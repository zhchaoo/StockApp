package jxt.app.microblog.model;

/**
 * Œ¢≤©–≈œ¢
 * @author ∆Ó“„
 * 2011-06-28
 */
public class News {
	private int id;
	private String u_id;
	private String u_name;
	private String s_id;
	private String text;
	private String source;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	private String retweeted_status;
	private String created_at;
	private int is_read;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String uId) {
		u_id = uId;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String uName) {
		u_name = uName;
	}
	public String getS_id() {
		return s_id;
	}
	public void setS_id(String sId) {
		s_id = sId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getThumbnail_pic() {
		return thumbnail_pic;
	}
	public void setThumbnail_pic(String thumbnailPic) {
		thumbnail_pic = thumbnailPic;
	}
	public String getBmiddle_pic() {
		return bmiddle_pic;
	}
	public void setBmiddle_pic(String bmiddlePic) {
		bmiddle_pic = bmiddlePic;
	}
	public String getOriginal_pic() {
		return original_pic;
	}
	public void setOriginal_pic(String originalPic) {
		original_pic = originalPic;
	}
	public String getRetweeted_status() {
		return retweeted_status;
	}
	public void setRetweeted_status(String retweetedStatus) {
		retweeted_status = retweetedStatus;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String retweetedStatus) {
		created_at = retweetedStatus;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int isRead) {
		is_read = isRead;
	}
}
