package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	//Atributos de configuracao
	private int larguraDispositivo = 0;
	private int alturaDispositivo  = 0;

	@Override
	public void create () {
		batch  = new SpriteBatch();
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
		variacao += Gdx.graphics.getDeltaTime() * 2;//calcula a diferenca de execuacao do render
		if (variacao > 2){ variacao = 0; }

		velocidadeQueda++;
		if ((posicaoInicialVertical > 0) || (velocidadeQueda < 0)) {
				posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
		}

		if (Gdx.input.justTouched()){
			if (alturaDispositivo - 100 > posicaoInicialVertical) {
				velocidadeQueda = -15;
				Gdx.app.log("Toque", "Toque na Tela");
			}

		}

		batch.begin();//inicia a exibicai das imagnes

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//Colocando e ajeitando o fundo
		batch.draw(canoTopo,posicaoMovimentoCanoHorizontal,alturaDispositivo / 2 + espacoEntreCanos);
		batch.draw(canoBaixo,posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos /2);
		batch.draw(passaros[ (int) variacao],30,posicaoInicialVertical);//desenha o passaro

		batch.end();
	}




}
