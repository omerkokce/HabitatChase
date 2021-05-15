package com.example.proje;

public class Aliskanlik {

    private String isim;
    private int hedef;
    private int renk;
    private int yuzde;

    public Aliskanlik() {
    }

    public Aliskanlik(String isim, int hedef, int yuzde) {
        setIsim(isim);
        setHedef(hedef);
        setYuzde(yuzde);
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public int getHedef() {
        return hedef;
    }

    public void setHedef(int hedef) {
        this.hedef = hedef;
    }

    public int getRenk() {
        return renk;
    }

    public void setRenk(int renk) {
        this.renk = renk;
    }

    public int getYuzde() {
        return yuzde;
    }

    public void setYuzde(int yuzde) {
        this.yuzde = yuzde;
    }
}
