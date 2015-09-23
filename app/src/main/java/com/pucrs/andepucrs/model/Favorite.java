package com.pucrs.andepucrs.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by ScHaFeR on 21/09/2015.
 */
public class Favorite {

    private ArrayList<Preferencias> preferencias;
    private LatLng start;
    private Estabelecimentos finish;

    public Favorite(ArrayList<Preferencias> preferencias, LatLng start, Estabelecimentos finish) {
        this.preferencias = preferencias;
        this.start = start;
        this.finish = finish;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                " start=" + start +
                ", finish=" + finish.toString() +
                "preferencias=" + preferencias.toString() +
                '}';
    }

    public ArrayList<Preferencias> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(ArrayList<Preferencias> preferencias) {
        this.preferencias = preferencias;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public Estabelecimentos getFinish() {
        return finish;
    }

    public void setFinish(Estabelecimentos finish) {
        this.finish = finish;
    }
}
