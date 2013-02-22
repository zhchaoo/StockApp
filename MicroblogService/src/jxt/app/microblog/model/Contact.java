package jxt.app.microblog.model;

/**
 * 联系人模型类
 * @author 祁毅
 * 2011-06-28
 */
public class Contact{
	private int id;
	private String u_id;
	private String u_name;
	private int province;
	private int city;
	private String location;
	private String description;
	private String url;
	private String profile_image_url;
	private String domain;
	private String gender;
	private int followers_count;
	private int friends_count;
	private int statuses_count;
	private int favourites_count;
	private String created_at;
	private String verified;
	
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
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profileImageUrl) {
		profile_image_url = profileImageUrl;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followersCount) {
		followers_count = followersCount;
	}
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friendsCount) {
		friends_count = friendsCount;
	}
	public int getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(int statusesCount) {
		statuses_count = statusesCount;
	}
	public int getFavourites_count() {
		return favourites_count;
	}
	public void setFavourites_count(int favouritesCount) {
		favourites_count = favouritesCount;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
}
