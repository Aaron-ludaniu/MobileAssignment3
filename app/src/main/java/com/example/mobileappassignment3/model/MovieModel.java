package com.example.mobileappassignment3.model;

public class MovieModel {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private String description;

    public String getTitle() { return Title; }
    public String getYear() { return Year; }
    public String getImdbID() { return imdbID; }
    public String getType() { return Type; }
    public String getPoster() { return Poster; }
    public String getDescription() { return description; }

    public void setTitle(String Title) { this.Title = Title; }
    public void setYear(String Year) { this.Year = Year; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }
    public void setType(String Type) { this.Type = Type; }
    public void setPoster(String Poster) { this.Poster = Poster; }
    public void setDescription(String description) { this.description = description; }

}
