package com.example.spring2020_final;

public class Schedule {
    String id;
    String name;
    String place;
    String date;
    String time;

    public Schedule(String id, String name, String place, String date, String time) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
