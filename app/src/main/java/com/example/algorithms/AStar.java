package com.example.algorithms;

import android.location.Location;

import org.json.JSONArray;

import java.util.Locale;

public class AStar {

    double[] g_score, F_score;
    double[] latitude, longtitude;
    double g_score_sementara;
    JSONArray ambilsimpul = null;
    int jalur;
    long sec;
    int openset, came_from[], closedset, current, neighbor;
    long startTime;
    long endTime;
    long astarTime;

    public int jalurastar(int start, int tujuan) {
        startTime = System.nanoTime();
        closedset = 0; //empty set

        came_from[start] = 0; //empty map
        g_score[start] = 0;
        F_score[start] = g_score[start]+ heuristic_cost_estimate(start, tujuan);

        while (openset != 0) {
            current = openset; //node in openset having the lowest f -[score] value
            if(current == tujuan )
                return reconstruct_path(new int[]{came_from[current]}, tujuan);
                current = closedset;
                for (int i = neighbor; i <= current; neighbor++){
                    if (neighbor == closedset)
                        continue;
                    }

                    g_score_sementara = g_score[current] + heuristic_cost_estimate(current, neighbor);

                    if (neighbor != openset || g_score_sementara <= g_score[neighbor]) came_from[neighbor] = current;
                    g_score[neighbor] = g_score_sementara;
                    F_score[neighbor] = g_score[neighbor] + heuristic_cost_estimate(neighbor, tujuan);

                    if (neighbor != openset)
                        openset = neighbor;

            }

        endTime = System.nanoTime();
        endTime = (endTime - startTime) / 1000000;
        return jalur;
    }

    float heuristic_cost_estimate(int start, int tujuan) {

        float heuristic[] =  new float[10];
        Location.distanceBetween(latitude[start], longtitude[start], latitude[tujuan], latitude[tujuan], heuristic);

        return heuristic[0];
    }

    public void getAmbilsimpul (JSONArray simpul) {
        this.ambilsimpul = simpul;
    }

    int reconstruct_path(int[] came_from, int tujuan){

        int p;
        if (came_from[tujuan] != closedset){
            p = reconstruct_path(came_from, came_from[current]);
            return (p + jalur);
        }

        return jalur;
    }

    public void getAmbilNeighbor(int neighbor) {
        this.neighbor = neighbor;
    }

    public void getopenset(int neighbor){
        this.neighbor = neighbor;
    }


}
