package com.gridgain.gen.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Country {

	@QuerySqlField
	private String code;
	@QuerySqlField
	private String name;
	@QuerySqlField
	private String continent;
	@QuerySqlField
	private String region;
	@QuerySqlField
	private Double surfacearea;
	@QuerySqlField
	private Integer indepyear;
	@QuerySqlField
	private Integer population;
	@QuerySqlField
	private Double lifeexpectancy;
	@QuerySqlField
	private Double gnp;
	@QuerySqlField
	private Double gnpold;
	@QuerySqlField
	private String localname;
	@QuerySqlField
	private String governmentform;
	@QuerySqlField
	private String headofstate;
	@QuerySqlField
	private Integer capital;
	@QuerySqlField
	private String code2;


    public Country (String code, String name, String continent, String region, Double surfacearea, Integer indepyear, Integer population, Double lifeexpectancy, Double gnp, Double gnpold, String localname, String governmentform, String headofstate, Integer capital, String code2) {
		this.code = code;
		this.name = name;
		this.continent = continent;
		this.region = region;
		this.surfacearea = surfacearea;
		this.indepyear = indepyear;
		this.population = population;
		this.lifeexpectancy = lifeexpectancy;
		this.gnp = gnp;
		this.gnpold = gnpold;
		this.localname = localname;
		this.governmentform = governmentform;
		this.headofstate = headofstate;
		this.capital = capital;
		this.code2 = code2;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public Double getSurfacearea() {
        return surfacearea;
    }

    public void setSurfacearea(Double surfacearea) {
        this.surfacearea = surfacearea;
    }
    public Integer getIndepyear() {
        return indepyear;
    }

    public void setIndepyear(Integer indepyear) {
        this.indepyear = indepyear;
    }
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
    public Double getLifeexpectancy() {
        return lifeexpectancy;
    }

    public void setLifeexpectancy(Double lifeexpectancy) {
        this.lifeexpectancy = lifeexpectancy;
    }
    public Double getGnp() {
        return gnp;
    }

    public void setGnp(Double gnp) {
        this.gnp = gnp;
    }
    public Double getGnpold() {
        return gnpold;
    }

    public void setGnpold(Double gnpold) {
        this.gnpold = gnpold;
    }
    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }
    public String getGovernmentform() {
        return governmentform;
    }

    public void setGovernmentform(String governmentform) {
        this.governmentform = governmentform;
    }
    public String getHeadofstate() {
        return headofstate;
    }

    public void setHeadofstate(String headofstate) {
        this.headofstate = headofstate;
    }
    public Integer getCapital() {
        return capital;
    }

    public void setCapital(Integer capital) {
        this.capital = capital;
    }
    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

}
