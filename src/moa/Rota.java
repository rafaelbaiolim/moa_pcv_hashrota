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
        Random gerador = new Random();

        int ind1 = gerador.nextInt(48);
        int ind2 = gerador.nextInt(48);

        int ind3 = gerador.nextInt(48);
        int ind4 = gerador.nextInt(48);

        ArrayList<Cidade> cloneCnjInicial = (ArrayList<Cidade>) cnjInicial.clone();
        Collections.swap(cloneCnjInicial, ind1, ind2);
        Collections.swap(cloneCnjInicial, ind3, ind4);

        return cloneCnjInicial;
    }

}
