package com.gridgain.gen.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Countrylanguage {

	@QuerySqlField
	private String countrycode;
	@QuerySqlField
	private String language;
	@QuerySqlField
	private String isofficial;
	@QuerySqlField
	private Double percentage;


    public Countrylanguage (String countrycode, String language, String isofficial, Double percentage) {
		this.countrycode = countrycode;
		this.language = language;
		this.isofficial = isofficial;
		this.percentage = percentage;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getIsofficial() {
        return isofficial;
    }

    public void setIsofficial(String isofficial) {
        this.isofficial = isofficial;
    }
    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

}
