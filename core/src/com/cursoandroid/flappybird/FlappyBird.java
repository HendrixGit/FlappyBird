package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;//rendeniza as imagens
	private  Texture passaro;
	private  Texture fundo;

	//Atributos de configuracao
	private int movimento = 0;
	private int larguraDispositivo = 0;
	private int alturaDispositivo  = 0;

	@Override
	public void create () {
		batch  = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo   = new Texture("fundo.png");
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  =  Gdx.graphics.getHeight();
	}


	@Override
	public void render(){//chamando animacoes
		movimento ++;
		batch.begin();//inicia a exibicai das imagnes
		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//Colocando e ajeitando o fundo
		batch.draw(passaro, movimento,400);
		batch.end();

	}




}
