/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moa;

import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author guest-NyVQjg
 */
class Conjunto {

    protected ArrayList<Cidade> cnjPermutado;

    protected ArrayList<Cidade> getConjuntoCidade() {
        ArrayList<Cidade> cnjCidade = new ArrayList<>();
        String[] entrada = getEntrada();
        for (int i = 0; i < 48; i++) {
            Cidade cidade = new Cidade();
            String[] arrCordenada = entrada[i].split(" ");
            cidade.nome = arrCordenada[0];
            cidade.x = Integer.parseInt(arrCordenada[1]);  // coordenada X
            cidade.y = Integer.parseInt(arrCordenada[2]); // coordenada Y
            cnjCidade.add(cidade);
        }

        return cnjCidade;
    }

    protected ArrayList<Cidade> getPermutacaoCnjCidade(ArrayList<Cidade> cnjInicial) {
        Collections.shuffle(cnjInicial);
        return cnjInicial;
    }

    protected String[] getEntrada() {
        String ret = "1 6734 1453\n"
                + "2 2233 10\n"
                + "3 5530 1424\n"
                + "4 401 841\n"
                + "5 3082 1644\n"
                + "6 7608 4458\n"
                + "7 7573 3716\n"
                + "8 7265 1268\n"
                + "9 6898 1885\n"
                + "10 1112 2049\n"
                + "11 5468 2606\n"
                + "12 5989 2873\n"
                + "13 4706 2674\n"
                + "14 4612 2035\n"
                + "15 6347 2683\n"
                + "16 6107 669\n"
                + "17 7611 5184\n"
                + "18 7462 3590\n"
                + "19 7732 4723\n"
                + "20 5900 3561\n"
                + "21 4483 3369\n"
                + "22 6101 1110\n"
                + "23 5199 2182\n"
                + "24 1633 2809\n"
                + "25 4307 2322\n"
                + "26 675 1006\n"
                + "27 7555 4819\n"
                + "28 7541 3981\n"
                + "29 3177 756\n"
                + "30 7352 4506\n"
                + "31 7545 2801\n"
                + "32 3245 3305\n"
                + "33 6426 3173\n"
                + "34 4608 1198\n"
                + "35 23 2216\n"
                + "36 7248 3779\n"
                + "37 7762 4595\n"
                + "38 7392 2244\n"
                + "39 3484 2829\n"
                + "40 6271 2135\n"
                + "41 4985 140\n"
                + "42 1916 1569\n"
                + "43 7280 4899\n"
                + "44 7509 3239\n"
                + "45 10 2676\n"
                + "46 6807 2993\n"
                + "47 5185 3258\n"
                + "48 3023 1942";
        return ret.split("\n");
    }

}
