package union.union_vr1;

public class Planet {

    private String name;
    private Integer distance;
    private String descr;
    private int idImg;



    public Planet(String name, Integer distance) {
        this.name = name;
        this.distance = distance;
    }

    public Planet(String name, String descr) {
        this.name = name;
        this.descr = descr;
    }

    public Planet(String name, Integer distance, String descr, int idImg) {
        this.name = name;
        this.distance = distance;
        this.descr = descr;
        this.idImg = idImg;
    }


    public String getName() {
        return name;
    }
    public void setNameX(String name) {
        this.name = name;
    }
    public Integer getDistance() {
        return distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getDescr() {
        return descr;
    }
    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getIdImg() {
        return idImg;
    }
    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }


}