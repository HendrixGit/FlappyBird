package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private  SpriteBatch batch;//rendeniza as imagens
	private  Texture[] passaros;
	private  Texture canoTopo;
	private  Texture canoBaixo;
    private  Texture logo;
    private  Texture gameOver;

    private  Stage stage;
    private  Image imagemBotaoJogar;
    private  Image imagemBotaoReiniciar;
    private  Image imagemBotaoMenu;
    private  Image imagemBotaoPlacar;
    private  Image imagemBotaoSair;

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
    private boolean clicksTelaMenu = true;
    private boolean clicksTelaGameOver = false;

	//Atributos de configuracao
	private float larguraDispositivo = 0;
	private float alturaDispositivo  = 0;
	private int estadoJogo = 0; // 0 - jogo nao iniciado - 1 jogo iniciado -  2 Game Over

    private OrthographicCamera camera;
    private Viewport viewport;//ocupa o tamanho da tela
    private final float VIRTUAL_WIDTH  = 768;
    private final float VIRTUAL_HEIGHT = 1024;


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
        fonte = new BitmapFont();//Placar
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);//tamando fonte

        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

		larguraDispositivo =  VIRTUAL_WIDTH; //Gdx.graphics.getWidth(); pegada a largu e altura virtuais
		alturaDispositivo  =  VIRTUAL_HEIGHT; //Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 300;

        camera   = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);//alinhando a camera posicao x,y e z no caso como nao foi usado 3d o memo e 0
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);//largura, altura e camera utiliazada no viewport resolucao generica para todos so dispositivos

        stage = new Stage();
        stage.setViewport(viewport);

        imagemBotaoJogar = new Image(new Texture(Gdx.files.internal("botaojogar.png")));
        imagemBotaoJogar.setPosition(250, posicaoInicialVertical + 180);
        imagemBotaoJogar.setSize(300, 80);
        imagemBotaoJogar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clicksTelaMenu == true) {
                    estadoJogo = 1;
                    clicksTelaGameOver = false;
                    clicksTelaMenu = false;
                }
            }
        });
        stage.addActor(imagemBotaoJogar);

        imagemBotaoPlacar = new Image(new Texture(Gdx.files.internal("botaoplacar.png")));
        imagemBotaoPlacar.setPosition(250, posicaoInicialVertical + 80);
        imagemBotaoPlacar.setSize(300, 80);
        stage.addActor(imagemBotaoPlacar);

        imagemBotaoSair = new Image(new Texture(Gdx.files.internal("botaosair.png")));
        imagemBotaoSair.setPosition(250, posicaoInicialVertical - 20);
        imagemBotaoSair.setSize(300, 80);
        imagemBotaoSair.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clicksTelaMenu == true) {
                    Gdx.app.exit();
                }
            }
        });
        stage.addActor(imagemBotaoSair);

        imagemBotaoReiniciar = new Image(new Texture(Gdx.files.internal("botaoreiniciar.png")));
        imagemBotaoReiniciar.setPosition(larguraDispositivo / 2 - imagemBotaoReiniciar.getWidth() / 2, alturaDispositivo / 2 - 100);
        imagemBotaoReiniciar.setSize(300, 80);
        imagemBotaoReiniciar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clicksTelaGameOver == true) {
                    estadoJogo = 1;
                    resetaValores();
                    clicksTelaGameOver = false;
                }
            }
        });
        stage.addActor(imagemBotaoReiniciar);

        imagemBotaoMenu = new Image(new Texture(Gdx.files.internal("botaomenu.png")));
        imagemBotaoMenu.setPosition(larguraDispositivo / 2 - imagemBotaoReiniciar.getWidth() / 2, alturaDispositivo / 2 - 200);
        imagemBotaoMenu.setSize(300, 80);
        imagemBotaoMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clicksTelaGameOver == true) {
                    estadoJogo = 0;
                    resetaValores();
                    clicksTelaMenu = true;
                }
            }
        });
        stage.addActor(imagemBotaoMenu);

        Gdx.input.setInputProcessor(stage);
	}

    private void resetaValores() {
        marcouPonto = false;
        pontuacao  = 0;
        velocidadeQueda = 0;
        posicaoInicialVertical = alturaDispositivo / 2;
        if (estadoJogo != 0){//if feita para os canos aparecerem na tela de menu
            posicaoMovimentoCanoHorizontal = larguraDispositivo;
        }
        else{
            posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
        }
    }

    @Override
	public void render() {//chamando animacoes
        camera.update();//atualiza os valores da camera

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);//Limpa os frames para uma melhor performace

		deltaTime = Gdx.graphics.getDeltaTime();//movimento asa passaro
		variacao += deltaTime * 2;//calcula a diferenca de execuacao do render
        if (variacao > 2) {
            variacao = 0;
        }


        if (estadoJogo != 0) {
            velocidadeQueda++; // queda do passaro
            if (((posicaoInicialVertical > 0) || (velocidadeQueda < 0)) && (estadoJogo != 2)) {
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
            }

            if(estadoJogo == 1){//jogo iniciado

                posicaoMovimentoCanoHorizontal -= deltaTime * 200;//movimento dos canos

                if (Gdx.input.justTouched()) {//toque na tela faz o passaro voar
                        velocidadeQueda = -15;
                }

                if (posicaoMovimentoCanoHorizontal < - canoTopo.getWidth()) {//reinicia os canos ao sair da tela
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
		}


		batch.setProjectionMatrix(camera.combined);//configurando as configuracoes da camera
        batch.begin();//inicia a exibicao das imagnes

            batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);//Colocando e ajeitando o fundo
            batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
            batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
            batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);//desenha o passaro

            if (estadoJogo == 0){
                batch.draw(logo, 200, posicaoInicialVertical + 300);
                imagemBotaoJogar.draw(batch,1);//desenhando botao jogar
                imagemBotaoPlacar.draw(batch, 1);
                imagemBotaoSair.draw(batch, 1);
            }
            else {
                if (estadoJogo == 2) {
                    batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
                    imagemBotaoReiniciar.draw(batch ,1);
                    imagemBotaoMenu.draw(batch,1);
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
            clicksTelaGameOver = true;
		}
	}

    @Override//sobreescrevendo o metodo resize para colocar a resolucao generica para todos os dispositivos
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
