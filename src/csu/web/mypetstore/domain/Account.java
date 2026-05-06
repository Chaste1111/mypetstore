package csu.web.mypetstore.domain;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 8751282105532159742L;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    // 修正：统一为美式拼写 favoriteCategoryId
    private String favoriteCategoryId;
    private String languagePreference;
    private boolean listOption;
    private boolean bannerOption;
    private String bannerName;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    // 移除：Stripes 框架的 Validate 注解（项目已不依赖 Stripes）
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    // 移除：Stripes 框架的 Validate 注解
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 修正：getter 方法名同步改为 getFavoriteCategoryId
    public String getFavoriteCategoryId() {
        return this.favoriteCategoryId;
    }

    // 修正：setter 方法名同步改为 setFavoriteCategoryId
    public void setFavoriteCategoryId(String favoriteCategoryId) {
        this.favoriteCategoryId = favoriteCategoryId;
    }

    public String getLanguagePreference() {
        return this.languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public boolean isListOption() {
        return this.listOption;
    }

    public void setListOption(boolean listOption) {
        this.listOption = listOption;
    }

    public boolean isBannerOption() {
        return this.bannerOption;
    }

    public void setBannerOption(boolean bannerOption) {
        this.bannerOption = bannerOption;
    }

    public String getBannerName() {
        return this.bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }
}