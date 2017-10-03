package com.jrfapplications.simplechat;

/**
 * Created by Jack on 03/10/2017.
 */

public class UserInfomation {

    private String firstnames;
    private String surname;
    private String dateofbirth;
    private String oneliner;
    private String bio;

    public UserInfomation()
    {

    }

    public String getFirstnames() {
        return firstnames;
    }

    public void setFirstnames(String firstnames) {
        this.firstnames = firstnames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOneliner() {
        return oneliner;
    }

    public void setOneliner(String oneliner) {
        this.oneliner = oneliner;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
