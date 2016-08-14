package moa;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

class Moa {

    static int idRotaGlobal = 0;
    static int TOTAL_CIDADES = 0;
    static HashMap<Integer, Rota> populacao = new HashMap<>();
    static int TOTAL_POPULACAO = 40;
    static int TAMANHO_CORTE = 6;
    static boolean FIRST_IMPROVEMENT = false;

    class Cidade {

        protected int idCidade;
        protected double x = 0;
        protected double y = 0;
    }

    class Rota implements Comparable<Rota> {

        protected double distancia;
        protected int idRota;
        protected ArrayList<Cidade> rota;

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

        protected ArrayList<Cidade> getMutacaoCidade(ArrayList<Cidade> cnjInicial, int totalCidadaes) {
            Random gerador = new Random();
            int ind1 = gerador.nextInt(TOTAL_CIDADES);
            int ind2 = gerador.nextInt(TOTAL_CIDADES);

            ArrayList<Cidade> cloneCnjInicial = new ArrayList<>(cnjInicial);

            Collections.swap(cloneCnjInicial, ind1, ind2);

            ind1 = gerador.nextInt(TOTAL_CIDADES);
            ind2 = gerador.nextInt(TOTAL_CIDADES);
            Collections.swap(cloneCnjInicial, ind1, ind2);

            return cloneCnjInicial;
        }

        protected ArrayList<Cidade> getPermutacaoCnjCidade(ArrayList<Cidade> cnjInicial, int totalCidadaes) {
            Random gerador = new Random();

            ArrayList<Cidade> cloneCnjInicial = new ArrayList<>(cnjInicial);
            for (int i = 0; i < TOTAL_CIDADES / 2; i++) {
                int ind1 = gerador.nextInt(totalCidadaes);
                int ind2 = gerador.nextInt(totalCidadaes);
                Collections.swap(cloneCnjInicial, ind1, ind2);
            }

            return cloneCnjInicial;
        }

    }

    protected Rota getRotaInicial(int entradaM) {
        ArrayList<Cidade> cidadesIniciais = new ArrayList<>();
        Rota rota = new Rota();
        String[] entrada = Utils.getEntrada(entradaM);
        TOTAL_CIDADES = Integer.parseInt(entrada[0]);
        for (int i = 1; i <= TOTAL_CIDADES; i++) {
            Cidade cidade = new Cidade();
            String[] arrCordenada = entrada[i].split(" ");
            cidade.idCidade = Integer.parseInt(arrCordenada[0]);
            cidade.x = Integer.parseInt(arrCordenada[1]);  // coordenada X
            cidade.y = Integer.parseInt(arrCordenada[2]); // coordenada Y
            cidadesIniciais.add(cidade);
        }
        rota.setRota(cidadesIniciais);
        return rota;
    }

    /*
    protected Rota getRotaInicial() {
        Scanner scan = new Scanner(System.in);
        String linha = scan.nextLine();
        Rota rotaInicial = new Rota();

        int totalCidades = Integer.parseInt(linha.trim());
        TOTAL_CIDADES = totalCidades;
        ArrayList<Cidade> cidadesRotaInicial = new ArrayList<>();

        for (int i = 0; i < totalCidades; i++) {
            Cidade cidade = new Cidade();
            linha = scan.nextLine().replaceAll(" +", " ");
            String[] arrCordenada = linha.split(" ");
            cidade.idCidade = Integer.parseInt(arrCordenada[0]);
            cidade.x = Double.parseDouble(arrCordenada[1]);  // coordenada X
            cidade.y = Double.parseDouble(arrCordenada[2]); // coordenada Y
            cidadesRotaInicial.add(cidade);
        }
        rotaInicial.setRota(cidadesRotaInicial);
        return rotaInicial;
    }*/
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
        for (Map.Entry<Integer, Rota> rota : populacao.entrySet()) {
            this.gerarDistancias(rota.getValue());
        }
    }

    protected void getSelecao(HashMap<Integer, Rota> populacao) {
        PriorityQueue<Rota> Q = new PriorityQueue<>();
        for (Map.Entry<Integer, Rota> rota : populacao.entrySet()) {
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

        for (Map.Entry<Integer, Rota> rota : populacao.entrySet()) {
            Q.add(rota.getValue());
        }

        Rota maiorA = Q.remove();
        Rota maiorB = Q.remove();

        populacao.remove(maiorA.idRota);
        populacao.remove(maiorB.idRota);

        Rota mutA = new Rota();
        Rota mutB = new Rota();

        mutA.setRota(perA.getMutacaoCidade(perA.getRota(), TOTAL_CIDADES));
        gerarDistancias(mutA);

        mutB.setRota(perB.getMutacaoCidade(perB.getRota(), TOTAL_CIDADES));
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
        int pontoCorte = Math.round(rotaX.getRota().size() / TAMANHO_CORTE);
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

    protected Rota executarBuscaLocal(Rota rotaAtual, boolean first) {

        ArrayList<Cidade> rota = new ArrayList<>();
        rota = rotaAtual.getRota();
        Rota melhorRota = rotaAtual;
        for (int i = 0; i < TOTAL_CIDADES - 3; i++) {
            ArrayList<Cidade> cloneRota = new ArrayList<>(rota);
            Collections.swap(cloneRota, i, i + 3);
            Collections.swap(cloneRota, i + 1, i + 2);

            Rota rotaVizinho = new Rota();
            rotaVizinho.setRota(cloneRota);
            gerarDistancias(rotaVizinho);
            if (rotaVizinho.distancia < melhorRota.distancia) {
                melhorRota = rotaVizinho;
                if (first) {
                    return melhorRota;
                }
            }
            rota = cloneRota;
        }
        return melhorRota;
    }

    protected Rota executarGeneticoPcv(int rotaM) {
        long t = System.currentTimeMillis();
        long end = t + 9499;
        Rota rotaInicial = getRotaInicial(rotaM);

        populacao.put(rotaInicial.idRota, rotaInicial);
        avaliacao(populacao);

        for (int i = 0; i < (TOTAL_POPULACAO - 1); i++) {
            Rota conjuntoPermutado = new Rota();
            conjuntoPermutado.setRota(rotaInicial.getPermutacaoCnjCidade(rotaInicial.getRota(), TOTAL_CIDADES));
            populacao.put(conjuntoPermutado.idRota, conjuntoPermutado);
        }

        PriorityQueue<Rota> Q = new PriorityQueue<>();
        while (System.currentTimeMillis() < end) {
            avaliacao(populacao);
            getSelecao(populacao);
        }

        for (Map.Entry<Integer, Rota> rota : populacao.entrySet()) {
            Q.add(rota.getValue());
        }

        Rota menorDistancia = Q.remove();
        System.out.println((int) menorDistancia.distancia);
        menorDistancia = executarBuscaLocal(menorDistancia, FIRST_IMPROVEMENT);

        return menorDistancia;
    }

    public static void main(String[] args) {
        Moa moa = new Moa();
        //Scanner dados = new Scanner(System.in);
        int casoDeTeste = 48;

        Rota result = moa.executarGeneticoPcv(casoDeTeste);
        System.out.println("#" + (int) result.distancia);
    }

}
