package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;//rendeniza as imagens
	private  Texture[] passaros;
	private  Texture canoTopo;
	private  Texture canoBaixo;

	private  Texture fundo;
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical = 0;
	private float posicaoMovimentoCanoHorizontal = 0;
	private float espacoEntreCanos = 0;
	private float deltaTime = 0;
	private Random numeroRandomico;
	private float alturaEntreCanosRandomica = 0;

	//Atributos de configuracao
	private int larguraDispositivo = 0;
	private int alturaDispositivo  = 0;

	@Override
	public void create () {
		batch  = new SpriteBatch();
		numeroRandomico = new Random();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro1.png");


		fundo   = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo  = new Texture("cano_topo.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  =  Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 300;
	}


	@Override
	public void render(){//chamando animacoes
		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 2;//calcula a diferenca de execuacao do render
		if (variacao > 2){ variacao = 0;}

		velocidadeQueda++;
		if ((posicaoInicialVertical > 0) || (velocidadeQueda < 0)) {
				posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
		}

		posicaoMovimentoCanoHorizontal -= deltaTime * 200;//movimento dos canos

		if (Gdx.input.justTouched()){//toque na tela faz o passaro voar
			if (alturaDispositivo - 100 > posicaoInicialVertical) {
				velocidadeQueda = -15;
				Gdx.app.log("Toque", "Toque na Tela");
			}

		}

		if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()){//reinicia os canos ao sair da tela
			posicaoMovimentoCanoHorizontal = larguraDispositivo;//reinicia os canos fora da tela
			alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;//gera numeros positivos entre 1 e 400 para p cano subir e descer

		}

		batch.begin();//inicia a exibicai das imagnes

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//Colocando e ajeitando o fundo
		batch.draw(canoTopo,posicaoMovimentoCanoHorizontal,alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(canoBaixo,posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos /2 + alturaEntreCanosRandomica);
		batch.draw(passaros[ (int) variacao],120,posicaoInicialVertical);//desenha o passaro



		batch.end();
	}




}
