/*
 * Você foi contratado para automatizar um treino de Fórmula1.
 * As regras estabelecidas pela direção das provas são simples:
 * “No máximo 5 carros das 7 escuderias (14 carros no total) presentes podem en_
 * trar na pista simultaneamente, mas apenas um carro de cada equipe. O segundo
 * carro deve ficar à espera, caso um companheiro de equipe já esteja na pista.
 * Cada piloto deve dar 3 voltas na pista. O tempo de cada volta deverá ser exi_
 * bido e a volta mais rápida de cada piloto deve ser armazenada para, ao final,
 * exibir o grid de largada, ordenado do menor tempo para o maior.”
 */

package controller;

import java.util.concurrent.Semaphore;

public class ThreadCorrida extends Thread {

	private Semaphore semaforoPista;
	private Semaphore[] semaforoEscuderia;
	private int idCarro;
	private int idEscuderia;
	private static int[][] menorTempo = new int[2][14];
	private static int qntCarros;
	
	public ThreadCorrida(int idCarro, Semaphore semaforoPista, int idEscuderia, Semaphore[] semaforoEscuderia) {
		this.idCarro = idCarro;
		this.idEscuderia = idEscuderia;
		this.semaforoPista = semaforoPista;
		this.semaforoEscuderia = semaforoEscuderia;
	}
	
	@Override
	public void run() {
		try {
			semaforoPista.acquire();
			if(idCarro == idEscuderia + 1 || idCarro == 2 * (idEscuderia + 1)) {
				semaforoEscuderia[idEscuderia].acquire();
			}
			carroNaPista();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforoPista.release();
			if(idCarro == idEscuderia + 1 || idCarro == 2 * (idEscuderia + 1)) {
				semaforoEscuderia[idEscuderia].release();
			}
		}
		
		if(qntCarros == 14) {
			ordenaGrid();
		}
	}
	
	private void carroNaPista() {
		System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia
				+" entrou na pista.");
		
		menorTempo[1][idCarro-1] = 1000;
		
		for(int volta = 1; volta <= 3; volta++) {
			int tempoVolta = (int)((Math.random() * 501) + 100);
			try {
				sleep(tempoVolta);  //Simulação do tempo para dar uma volta na pista
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia
					+" completou a volta #"+ volta+" em "+tempoVolta+" s.");
			
			if(tempoVolta <= menorTempo[1][idCarro-1]) {
				//Armazenando o tempo da melhor volta
				menorTempo[1][idCarro-1] = tempoVolta;  //Armazena o tempo
				menorTempo[0][idCarro-1] = idCarro;  //Armazena o carro
			}
		}
		qntCarros++;  //Quantidade de carros que já passaram pela pista para poder gerar o Grid
		System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia
				+" saiu.");
	}
	
	private void ordenaGrid() {
		//Ordena pelo menor tempo
		int qntColunas = menorTempo[1].length;
		for(int i = 0; i < qntColunas; i++) {
			for(int j = 0; j < qntColunas - 1; j++) {
				if(menorTempo[1][j] > menorTempo[1][j + 1]) {
					//Ordenando o carro associado ao tempo
					int auxLinha0 = menorTempo[0][j];
					menorTempo[0][j] = menorTempo[0][j + 1];
					menorTempo[0][j + 1] = auxLinha0;
					//Ordenando o tempo
					int auxLinha1 = menorTempo[1][j];
					menorTempo[1][j] = menorTempo[1][j + 1];
					menorTempo[1][j + 1] = auxLinha1;
				}
			}
		}
		
		//Imprime o Grid ordenado
		System.out.println("\n===============================================");
		System.out.println("\tGRID DE LARGADA\n");
		System.out.println("Posição\t|Carro\t|Tempo");
		for(int j = 0; j < qntColunas; j++) {
			System.out.println((j+1)+"\t|"+menorTempo[0][j]+"\t|"+menorTempo[1][j]);
		}
		System.out.println("===============================================");
	}
	
}
