package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;

import java.awt.AWTEventMulticaster;
import java.awt.Button;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private  SpriteBatch batch;//rendeniza as imagens
	private  Texture[] passaros;
	private  Texture canoTopo;
	private  Texture canoBaixo;
    private  Texture logo;
    private  Texture gameOver;
    private  Texture botaoJogar;

	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;

	private  Texture fundo;
	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical = 0;
	private float posicaoMovimentoCanoHorizontal = 0;
	private float espacoEntreCanos = 0;
	private float deltaTime = 0;
	private Random numeroRandomico;
	private float alturaEntreCanosRandomica = 0;
    private BitmapFont fonte;
    private BitmapFont mensagem;
    private int pontuacao = 0;
    private boolean marcouPonto = false;

	//Atributos de configuracao
	private int larguraDispositivo = 0;
	private int alturaDispositivo  = 0;
	private int estadoJogo = 0; // 0 - jogo nao iniciado - 1 jogo iniciado -  2 Game Over

	@Override
	public void create () {
		batch  = new SpriteBatch();
		numeroRandomico = new Random();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro1.png");

		passaroCirculo = new Circle();//formas para as colisoes

		fundo   = new Texture("fundo.png");
        logo    = new Texture("logo.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo  = new Texture("cano_topo.png");
        gameOver  = new Texture("game_over.png");
        botaoJogar = new Texture("botaojogar.png");
        fonte = new BitmapFont();//Placar
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);//tamando fonte

        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo  =  Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 300;
	}


	@Override
	public void render() {//chamando animacoes

		deltaTime = Gdx.graphics.getDeltaTime();//movimento asa passaro
		variacao += deltaTime * 2;//calcula a diferenca de execuacao do render
        if (variacao > 2) {
            variacao = 0;
        }

		if (estadoJogo == 0) {//verifica se o jogo foi iniciado
			if (Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}
		else {
            velocidadeQueda++; // queda do passaro
            if ((posicaoInicialVertical > 0) || (velocidadeQueda < 0)) {
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
            }

            if(estadoJogo == 1){//jogo iniciado

                posicaoMovimentoCanoHorizontal -= deltaTime * 200;//movimento dos canos

                if (Gdx.input.justTouched()) {//toque na tela faz o passaro voar
                    if (alturaDispositivo - 100 > posicaoInicialVertical) {
                        velocidadeQueda = -15;
                    }
                }

                if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {//reinicia os canos ao sair da tela
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;//reinicia os canos fora da tela
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;//gera numeros positivos entre 1 e 400 para p cano subir e descer
                    marcouPonto = false;
                }

                if (posicaoMovimentoCanoHorizontal < 120){//cano passou o passaro portando a pontuacao incrementa
                    if (!marcouPonto) {
                        pontuacao++;
                        marcouPonto = true;
                    }
                }

            }
            else{//game over
                   if (Gdx.input.justTouched()){//reinicia o jogo
                       estadoJogo = 1;
                       pontuacao  = 0;
                       velocidadeQueda = 0;
                       posicaoInicialVertical = alturaDispositivo / 2;
                       posicaoMovimentoCanoHorizontal = larguraDispositivo;

                }
            }
		}

		batch.begin();//inicia a exibicao das imagnes

            batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);//Colocando e ajeitando o fundo
            batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
            batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
            batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);//desenha o passaro

            if (estadoJogo == 0){
                batch.draw(logo, 200, posicaoInicialVertical + 300);
            }
            else {
                if (estadoJogo == 2) {
                    batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
                    mensagem.draw(batch, "Toque para Reiniciar" ,larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
                }
                fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);
            }

		batch.end();

		//colisoes
		retanguloCanoBaixo = new Rectangle(//x,y largura e altura para criar a forma para o cano
			posicaoMovimentoCanoHorizontal,
			alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
			canoBaixo.getWidth(), canoBaixo.getHeight()
		);

		retanguloCanoTopo = new Rectangle(
			posicaoMovimentoCanoHorizontal,
			alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica,
			canoTopo.getWidth(), canoTopo.getHeight()
		);

		passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2 , passaros[0].getWidth() / 2);//desenha a forma em cima do passaro 120 mais a metade da largura do passaro
		//Teste de colisao
		if ((Intersector.overlaps(passaroCirculo,retanguloCanoBaixo)) || (Intersector.overlaps(passaroCirculo,retanguloCanoTopo))
        || (posicaoInicialVertical <= 0) || (posicaoInicialVertical >= alturaDispositivo) ){
            estadoJogo = 2;

		}
	}

}
