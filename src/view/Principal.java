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

package view;

import java.util.concurrent.Semaphore;

import controller.ThreadCorrida;

public class Principal {

	public static void main(String[] args) {
		Semaphore[] semaforoEscuderia = new Semaphore[7];
		for(int i = 0; i < 7; i++) {
			semaforoEscuderia[i] = new Semaphore(1);
		}
		
		Semaphore semaforoPista = new Semaphore(5);
		
		int idEscuderia = 0;
		for(int idCarro = 1; idCarro <= 14; idCarro++) {
			if(idEscuderia == 7) {
				idEscuderia = 0;
			}
			Thread tCorrida = new ThreadCorrida(idCarro, semaforoPista, idEscuderia, semaforoEscuderia);
			tCorrida.start();
			idEscuderia++;
		}
	}

}
