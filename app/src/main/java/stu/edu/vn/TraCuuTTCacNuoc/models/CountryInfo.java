package stu.edu.vn.TraCuuTTCacNuoc.models;

import java.util.List;

public class CountryInfo {
    private String id;
    private String name;
    private String isoCode;
    private String capital;
    private int population;
    private String continent;
    private String currencyCode;
    private double latitude;
    private double longitude;
    private double area;
    private List<String> neighbours;
    private String flagUrl;

    public CountryInfo() {}

    public CountryInfo(String id ,String name, String isoCode, String capital, int population, String continent,
                       String currencyCode, double latitude, double longitude, double area,
                       List<String> neighbours, String flagUrl) {
        this.id=id;
        this.name = name;
        this.isoCode = isoCode;
        this.capital = capital;
        this.population = population;
        this.continent = continent;
        this.currencyCode = currencyCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
        this.neighbours = neighbours;
        this.flagUrl = flagUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public List<String> getNeighbours() { return neighbours; }
    public void setNeighbours(List<String> neighbours) { this.neighbours = neighbours; }
    public String getFlagUrl() { return flagUrl; } // Getter cho flagUrl
    public void setFlagUrl(String flagUrl) { this.flagUrl = flagUrl; }
}
