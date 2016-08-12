package moa;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

class Moa {

    static int idRotaGlobal = 0;
    HashMap<Integer, Rota> populacao = new HashMap<>();
    static final int TOTAL_FOR = 48;
    static final int TOTAL_POPULACAO = 20;
    static final int TAXA_MUTACAO = 20;
    static final int TAXA_SELECAO = 20;
    static final boolean FAZER_BUSCA_LOCAL = true;

    class Cidade {

        protected int idCidade;
        protected int x = 0;
        protected int y = 0;
    }

    class Rota implements Comparable<Rota> {

        protected double distancia;
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

    protected ArrayList<Cidade> getRotaInicial() {
        ArrayList<Cidade> rotaInicial = new ArrayList<>();
        String[] entrada = Utils.getEntrada();
        for (int i = 0; i < TOTAL_FOR; i++) {
            Cidade cidade = new Cidade();
            String[] arrCordenada = entrada[i].split(" ");
            cidade.idCidade = Integer.parseInt(arrCordenada[0]);
            cidade.x = Integer.parseInt(arrCordenada[1]);  // coordenada X
            cidade.y = Integer.parseInt(arrCordenada[2]); // coordenada Y
            rotaInicial.add(cidade);
        }
        return rotaInicial;
    }

    protected double calcularDistancia2Pontos(Cidade cidadeA, Cidade cidadeB) {
        return Math.sqrt(Math.pow((cidadeA.x - cidadeB.x), 2)
                + Math.pow((cidadeA.y - cidadeB.y), 2));
    }

    protected Rota gerarDistancias(Rota populacao) {
        ArrayList<Cidade> cnjInicial = populacao.getRota();
        for (int i = 0; i < cnjInicial.size() - 1; i++) {
            populacao.distancia
                    += calcularDistancia2Pontos(cnjInicial.get(i), cnjInicial.get(i + 1));
        }
        return populacao;
    }

    protected void avaliacao(HashMap<Integer, Rota> populacao) {
        Iterator it = populacao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Rota> rota = (Map.Entry) it.next();
            this.gerarDistancias(rota.getValue());
        }
    }

    protected void getSelecao(HashMap<Integer, Rota> populacao) {
        PriorityQueue<Rota> Q = new PriorityQueue<>();
        Iterator it = populacao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Rota> rota = (Map.Entry) it.next();
            Q.add(rota.getValue());
        }

        Rota rotaX = Q.remove();
        Rota rotaY = Q.remove();
        populacao.remove(rotaX.idRota);
        populacao.remove(rotaY.idRota);
        cruzamento(rotaX, rotaY);
    }

    protected void gerarMutacao(Rota perA, Rota perB) {
        PriorityQueue<Rota> Q = new PriorityQueue<>(Collections.reverseOrder());

        Iterator it = populacao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Rota> rota = (Map.Entry) it.next();
            Q.add(rota.getValue());
        }

        Rota maiorA = Q.remove();
        Rota maiorB = Q.remove();

        populacao.remove(maiorA.idRota);
        populacao.remove(maiorB.idRota);

        Rota mutA = new Rota();
        Rota mutB = new Rota();

        mutA.setRota(perA.getPermutacaoCnjCidade(perA.getRota()));
        gerarDistancias(mutA);

        mutB.setRota(perB.getPermutacaoCnjCidade(perB.getRota()));
        gerarDistancias(mutB);

        populacao.put(mutA.idRota, mutA);
        populacao.put(mutB.idRota, mutB);
    }

    protected void preencherNovaRota(
            Rota rotaX, Rota rotaY,
            HashMap<Integer, Integer> novaRotaA,
            int sizeRotas,
            HashMap<Integer, Cidade> hashFinder
    ) {
        boolean inHash = false;
        int j = 0;
        for (int i = 0; i < sizeRotas; i++) {
            if (novaRotaA.get(i) == -1) {
                inHash = false;
                while (!inHash) {
                    if (!novaRotaA.containsValue(rotaY.getRota().get(j).idCidade)) {
                        novaRotaA.put(i, rotaY.getRota().get(j).idCidade);
                        hashFinder.put(i, rotaY.getRota().get(j));
                        inHash = true;
                    }
                    j++;
                }
            }
        }
    }

    protected void inicializarNovasRotas(HashMap<Integer, Integer> novaRotaA,
            HashMap<Integer, Integer> novaRotaB, int sizeRotas) {
        for (int i = 0; i < sizeRotas; i++) {
            novaRotaA.put(i, -1);
            novaRotaB.put(i, -1);
        }
    }

    protected void cruzamento(Rota rotaX, Rota rotaY) {
        int meio = Math.round(rotaX.getRota().size() / 2) - 1;
        int pontoCorte = Math.round(rotaX.getRota().size() / 8);
        int sizeRotas = rotaX.getRota().size();
        boolean inHash = false;
        HashMap<Integer, Integer> novaRotaA = new HashMap<>();
        HashMap<Integer, Integer> novaRotaB = new HashMap<>();
        HashMap<Integer, Cidade> hashFinderA = new HashMap<>();
        HashMap<Integer, Cidade> hashFinderB = new HashMap<>();
        inicializarNovasRotas(novaRotaA, novaRotaB, sizeRotas);

        for (int i = meio - pontoCorte; i <= meio + pontoCorte; i++) {
            novaRotaA.put(i, rotaX.getRota().get(i).idCidade);
            hashFinderA.put(i, rotaX.getRota().get(i));
            novaRotaB.put(i, rotaY.getRota().get(i).idCidade);
            hashFinderB.put(i, rotaY.getRota().get(i));
        }

        preencherNovaRota(rotaX, rotaY, novaRotaA, sizeRotas, hashFinderA);
        preencherNovaRota(rotaY, rotaX, novaRotaB, sizeRotas, hashFinderB);

        ArrayList<Cidade> listRetA = new ArrayList<>(hashFinderA.values());
        ArrayList<Cidade> listRetB = new ArrayList<>(hashFinderB.values());

        Rota rotaA = new Rota();
        rotaA.setRota(listRetA);
        gerarDistancias(rotaA);
        populacao.put(rotaA.idRota, rotaA);

        Rota rotaB = new Rota();
        rotaB.setRota(listRetB);
        gerarDistancias(rotaB);
        populacao.put(rotaB.idRota, rotaB);
        gerarMutacao(rotaA, rotaB);
    }

    protected Rota executarBuscaLocal(Rota rotaAtual) {

        ArrayList<Cidade> vizinho = new ArrayList<>();
        vizinho = rotaAtual.getRota();

        for (int i = 3; i < vizinho.size(); i += 4) {
            for (int j = i + 3; j > 0; j--) {
                try {
                    Collections.swap(vizinho, j, i);
                    Collections.swap(vizinho, j + 1, j + 2);
                } catch (IndexOutOfBoundsException idb) {
                }
            }
        }
        Rota rotaVizinho = new Rota();
        Collections.reverse(vizinho);
        rotaVizinho.setRota(vizinho);
        gerarDistancias(rotaVizinho);
        if (rotaVizinho.distancia < rotaAtual.distancia) {
            return rotaVizinho;
        }
        return null;
    }

    protected Rota executarGeneticoPcv() {
        long t = System.currentTimeMillis();
        long end = t + 9000;

        Rota rotaInicial = new Rota();
        rotaInicial.setRota(getRotaInicial());

        populacao.put(rotaInicial.idRota, rotaInicial);
        avaliacao(populacao);

        for (int i = 0; i < 99; i++) {
            Rota conjuntoPermutado = new Rota();
            conjuntoPermutado.setRota(rotaInicial.getPermutacaoCnjCidade(rotaInicial.getRota()));
            populacao.put(conjuntoPermutado.idRota, conjuntoPermutado);
        }

        PriorityQueue<Rota> Q = new PriorityQueue<>();
        while (System.currentTimeMillis() < end) {
            avaliacao(populacao);
            getSelecao(populacao);
        }

        Iterator it = populacao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Rota> rota = (Map.Entry) it.next();
            Q.add(rota.getValue());
        }

        System.out.println("POP SIZE:  " + populacao.size());
        Rota menorDistancia = Q.remove();

        if (FAZER_BUSCA_LOCAL) {
            Rota possivelMelhor = executarBuscaLocal(menorDistancia);
            if (possivelMelhor != null) {
                menorDistancia = possivelMelhor;
            }
        }
        return menorDistancia;
    }

    protected static void main(String[] args) {
        Moa moa = new Moa();

        Rota result = moa.executarGeneticoPcv();
        System.out.println("Result : " + (int) result.distancia);

        for (Cidade c : result.getRota()) {
            System.out.print(c.idCidade + " ");
        }
    }

}
