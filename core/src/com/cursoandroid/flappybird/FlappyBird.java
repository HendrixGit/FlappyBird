package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;//rendeniza as imagens
	private  Texture[] passaros;
	private  Texture fundo;
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical = 0;

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
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  =  Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
	}


	@Override
	public void render(){//chamando animacoes
		variacao += Gdx.graphics.getDeltaTime() * 2;//calcula a diferenca de execuacao do render
		if (variacao > 2){ variacao = 0; }

		velocidadeQueda ++;
		if (posicaoInicialVertical > 0){
			posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
		}

		batch.begin();//inicia a exibicai das imagnes

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//Colocando e ajeitando o fundo
		batch.draw(passaros[ (int) variacao],30,posicaoInicialVertical);

		batch.end();
	}




}
