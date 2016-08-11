package moa;

import java.util.*;

/**
 *
 * @author guest-NyVQjg
 */
class Rota implements Comparable<Rota> {

    protected double distancia;
    private static int idRotaGlobal = 0;
    protected int idRota;
    ArrayList<Cidade> rota; 

    Rota() {
        this.idRota += idRotaGlobal;
        this.distancia = 0;
        rota = new ArrayList<>();
        idRotaGlobal++;
    }

    @Override
    public int compareTo(Rota o) {
        if (this.distancia < o.distancia) {
            return -1;
        }
        if (this.distancia > o.distancia) {
            return 1;
        }
        return 0;
    }

    protected void setRota(ArrayList< Cidade> rota) {
        this.rota = rota;
    }

    protected ArrayList<Cidade> getRota() {
        return this.rota;
    }

    protected ArrayList<Cidade> getPermutacaoCnjCidade(ArrayList<Cidade> cnjInicial) {
        ArrayList<Cidade> cloneCnjInicial = (ArrayList<Cidade>) cnjInicial.clone();
        Collections.shuffle(cloneCnjInicial);
        return cloneCnjInicial;
    }

}
