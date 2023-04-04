package com.example.test.enity;

public class Daka
{
    public int id;
    public String date;
    public String keyword;
    public String summary;

    public Daka()
    {
    }

    public Daka(String date, String keyword, String summary)
    {
        this.date = date;
        this.keyword = keyword;
        this.summary = summary;
    }

    @Override
    public String toString()
    {
        return "Daka{" + "id=" + id + ", date='" + date + '\'' + ", keyword='" + keyword + '\'' + ", summary='" + summary + '\'' + '}';
    }
}
