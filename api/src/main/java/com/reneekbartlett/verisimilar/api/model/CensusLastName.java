package com.reneekbartlett.verisimilar.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Index;

@SuppressWarnings("unused")
@Entity
@Table(
  //schema="portfolio_prod", 
  name="config_fullname_last_census"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CensusLastName {

    //@Id
    //@Column(name = "id")
    //private String id;
    //private int census_id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    private int year;

    @Column(name = "rank")
    private int rank;

    @Column(name = "count")
    private int count;

    private double prop100k;
    private double cum_prop100k;
    private double pctwhite;
    private double pctblack; 
    private double pctapi; 
    private double pctaian; 
    private double pct2prace;
    private double pcthispanic;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    //public int getRank() {
    //    return this.rank;
    //}

    public void setCount(int count) {
        this.count = count;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //public int getCount() {
    //    return this.count;
    //}
}
