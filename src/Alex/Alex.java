/*       UNESP Câmpus de Presidente Prudente
         SECOMPP - Semana da Computação 2019
       Minicurso:  Desenvolvimento de Jogos 2D

   Alex Kidd
   Desenvolvido por: Luiz F. P. Redondaro           */

package Alex;

import Cenario.Objeto;
import Inimigo.Inimigo;
import java.util.ArrayList;
import jplay.Sound;
import jplay.Sprite;

public class Alex {
    
    /** Constante ciclo máximo de pulo. */
    private final static int MAXPULO = 40;
    
    /** Imagens de animação do Alex. */
    private ArrayList<Sprite> animEsq, animDir, animVar;
    
    /** Movimentação auxiliar (x,y) e velocidade do movimento. */
    private double x, y,
                   velocidade;
    
    /** Estados do jogador 0:morreu 1:parado, 2:andando, 3:abaixado, 4:pulo, 5:soco_chão, 6:soco_pulo. */
    private int estado;
    
    /** Direção do movimento 1-esquerda 2-direita. */
    private int direção;
    
    /** Ciclo da animação do Alex. */
    private int ciclo;
    
    /** Ciclo do soco. */
    private int ciclosoco;
    
    /** Pontuação do jogador. */
    private int dinheiro;
    
    /** Efeito de soco, para permitir destruir apenas um objeto com soco. */
    private boolean efeitosoco;
    
    /** Sprites do personagem e da mão (caso dê soco), usados apenas para verificar colisão. */
    private Sprite desenho, mão;
    
    /** Entradas do jogador. */
    private boolean esquerda = false, direita = false, baixo = false, pulo = false, soco = false;
    
    /** Obter posição x. */
    public double getX() {
        return x;
    }
    
    /** Obter pontuação do jogador. */
    public int getDinheiro() {
        return dinheiro;
    }
    
    /** Método contrutor, que carrega as imagens de animação do Alex, define sua
     * posição inicial (x,y) e inicia os demais atributos.
     * 
     * @param x int posição inicial em x
     * @param y int posição inicial em y
     */
    public Alex(int x, int y) {
        animEsq = new ArrayList<Sprite>();
        animDir = new ArrayList<Sprite>();
        animVar = new ArrayList<Sprite>();
        
        //carregando animações do personagem
        animEsq.add(new Sprite("src//Alex//parado_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//anda1_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//anda2_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//anda3_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//baixo_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//pulo_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//soco_esq.png", 1));
        animEsq.add(new Sprite("src//Alex//mao_esq.png", 1));

        animDir.add(new Sprite("src//Alex//parado.png", 1));
        animDir.add(new Sprite("src//Alex//anda1.png", 1));
        animDir.add(new Sprite("src//Alex//anda2.png", 1));
        animDir.add(new Sprite("src//Alex//anda3.png", 1));
        animDir.add(new Sprite("src//Alex//baixo.png", 1));
        animDir.add(new Sprite("src//Alex//pulo.png", 1));
        animDir.add(new Sprite("src//Alex//soco.png", 1));
        animDir.add(new Sprite("src//Alex//mao.png", 1));
        
        animVar.add(new Sprite("src//Alex//morre1.png", 1));
        animVar.add(new Sprite("src//Alex//morre2.png", 1));
        animVar.add(new Sprite("src//Alex//morre3.png", 1));
        
        //imagens categóricas, para verificar colisões
        desenho = new Sprite("src//Alex//parado.png", 1);
        mão = new Sprite("src//Alex//mao.png", 1);
        velocidade = 3.5;
        dinheiro = 0;
        
        this.x = desenho.x = x;
        mão.x = x + 30;
        this.y = desenho.y = y;
        mão.y = y + 16;
        estado = 1;
        direção = 2;
        ciclo = 0;
        ciclosoco = 0;
    }
    
    /** Tratamento das entradas do jogador.
     * 
     * @param esquerda
     * @param direita
     * @param baixo
     * @param pulo
     * @param soco 
     */
    public void controle(boolean esquerda, boolean direita, boolean baixo, boolean pulo, boolean soco) {
        this.pulo = pulo;
        this.soco = soco;
        
        //pulo e soco prevalece sobre apertar baixo
        if (pulo || soco || estado > 3)
            this.baixo = false;
        else
            this.baixo = baixo;
        
        //apertar baixo prevalece sobre direcionais, aplicar XOR na leitura de direcionais
        if ((esquerda && direita) || this.baixo)
            this.esquerda = this.direita = false;
        else{
            this.esquerda = esquerda;
            this.direita = direita;
        }
    }
    
    /** Realiza a dinâmica do personagem, de acordo com as entradas do jogador,
     * sua ação atual e a interação com objetos e inimigos.
     * 
     * @param objetos ArrayList de objetos no cenário
     * @param inimigos ArrayList de inimigos no cenário
     * @throws InterruptedException 
     */
    public void atualizar(ArrayList<Objeto> objetos, ArrayList<Inimigo> inimigos) throws InterruptedException {
        if (estado > 0) {  // se personagem estiver vivo
            boolean colisão = false;
            desenho.x = x;
            
            //gravidade e contato com superfícies de plataformas, se não estiver pulando
            if ((estado != 4 && estado != 6) || ((estado == 4 || estado == 6) && ciclo >= MAXPULO)) { //se não estiver pulando
                desenho.y += velocidade;
                for (Objeto ob: objetos)
                    if (ob.getImagem().collided(desenho))
                        if (ob.getTipo() >= 1 && ob.getTipo() <= 3) { //objetos que podem ser pisados
                            colisão = true;
                            break;
                        }
                if (colisão) { //tem um chão abaixo do personagem
                    desenho.y = y;
                    if (estado == 4 || estado == 6){ // se estiver caindo, muda o estado
                        ciclo = 0;
                        if (estado == 4)
                            estado = 1;
                        else
                            estado = 5;
                    }
                }else{ //não tem um chão abaixo do personagem
                    y = desenho.y;
                    if (estado != 4 && estado != 6) { // se não estiver caindo, muda o estado
                        if (estado == 5)
                            estado = 6;
                        else
                            estado = 4;
                        ciclo = MAXPULO;
                    }
                }
            }else{ //se estiver pulando, tratar pulo
                if (ciclo < MAXPULO) {
                    ciclo++;
                    if (ciclo < MAXPULO - 10) { // manter no topo do pulo por um pequeno intervalo
                        desenho.y -= velocidade;
                        colisão = false;
                        for (Objeto ob: objetos)
                            if (ob.getImagem().collided(desenho))
                                if (ob.getTipo() >= 1 && ob.getTipo() <= 3) { //objetos que podem ser colididos
                                    colisão = true;
                                    break;
                                }
                        if (colisão) { //se colidiu a cabeça com um bloco, começa a cair
                            desenho.y = y;
                            ciclo = MAXPULO;
                        }else //senão, movimenta o pulo
                            y = desenho.y;
                    }
                }
            }
            
            if (ciclosoco > 0) {
                ciclosoco++;
                if (ciclosoco == 20){
                    if (estado == 5)
                        estado = 1;
                    else
                        estado = 4;
                }else
                    if (ciclosoco >= 30)
                        ciclosoco = 0;
            }
            
            if (soco) { //jogador apertou soco
                if (ciclosoco == 0) {
                    efeitosoco = true;
                    if (estado < 4) //chão
                        estado = 5;
                    else
                        estado = 6; //pulo
                    ciclosoco = 1;
                    new Sound("src//Sons//soco.wav").play(); //som para soco
                }
            }
            
            if (pulo) //jogador apertou pulo
                if (estado < 3 || estado == 5) { //se estiver no chão
                    ciclo = 0;
                    if (estado < 3)
                        estado = 4;
                    else
                        estado = 6;
                    new Sound("src//Sons//pulo.wav").play(); //som para pulo
                }

            //movimentação esquerda/direita/baixo/parado
            if (esquerda || direita) {
                if (esquerda){
                    if (direção == 2 && estado <= 4) //quando soca, não muda de direção
                        direção = 1;
                    desenho.x -= velocidade;
                }else{
                    if (direção == 1 && estado <= 4)
                        direção = 2;
                    desenho.x += velocidade;
                }
                colisão = false;
                for (Objeto ob: objetos) {
                    if (ob.getImagem().collided(desenho))
                        if (ob.getTipo() >= 1 && ob.getTipo() <= 3) { //objetos que podem esbarrados
                            colisão = true;
                            break;
                        } 
                }
                if (colisão)
                    desenho.x = x;
                else
                    x = desenho.x;
                if (estado < 4) { //ciclo da animação do personagem andando
                    if (estado != 2) {
                        estado = 2;
                        ciclo = 0;
                    }else
                        if (ciclo < 40)
                            ciclo++;
                        else
                            ciclo = 0;
                }
            }else
                if (baixo) {
                    if (estado <= 3)
                        estado = 3;
                }else
                    if (estado < 4) //parado
                        estado = 1;
            
            //verificar se acertou alguma coisa com soco
            if (estado > 4) {
                if (direção == 2)
                    mão.x = x + 30;
                else
                    mão.x = x - 16;
                mão.y = y + 16;
                
                //verificar se matou algum inimigo
                for (int i = 0; i < inimigos.size(); i++)
                    if (inimigos.get(i).getImagem().collided(mão)) {
                        inimigos.remove(i);
                        new Sound("src//Sons//inimigo.wav").play(); //som para matar inimigo
                    }
                
                //verificar se acertou algum objeto
                for (int i = 0; i < objetos.size(); i++) {
                    if (objetos.get(i).getImagem().collided(mão) //se acertou objeto
                            && (objetos.get(i).getTipo() == 2 || objetos.get(i).getTipo() == 3) //pedra ou baú
                            && efeitosoco) { //não destruiu outro objeto com soco
                        if (objetos.get(i).getTipo() == 2){ //se pedra, excluir objeto
                            objetos.remove(i);
                            new Sound("src//Sons//bloco.wav").play(); //som para destruir pedra
                            
                        }else{ //se baú, substituir objeto pedra por dinheiro
                            objetos.get(i).redefinir("src//Cenario//money.png", 4, 0);
                            new Sound("src//Sons//estrela.wav").play(); //som para abrir baú
                        }
                        efeitosoco = false;
                        break;
                    }
                }
                
            }

            //verificar se coletou algum item
            for (int i = 0; i < objetos.size(); i++) {
                if (objetos.get(i).getImagem().collided(desenho) //se ocorreu contato
                        && objetos.get(i).getTipo() == 4) {      //se o objeto é dinheiro
                    dinheiro += 100;
                    objetos.remove(i);
                    new Sound("src//Sons//item.wav").play(); //som para coletar item
                    break;
                }
            }

            //verificar se personagem morreu
            for (Inimigo in: inimigos) {
                if (in.getImagem().collided(desenho)) {
                    if (estado > 0) {
                        estado = 0;
                        ciclo = 0;
                        new Sound("src//Sons//contato.wav").play(); //som para contato com inimigo
                        Thread.sleep(1000);
                        new Sound("src//Sons//morte.wav").play(); //som para anjinho subindo
                    }
                    break;
                }
            }
            
        }else{ // se personagem morreu
            if (ciclo < 80)
                ciclo++;
            else
                ciclo = 0;
            if (y > -100)
                y -= velocidade/2;
        }
    }
    
    /** Exibe o personagem, de acordo com sua ação atual e seu ciclo de animação
     * (caso esteja andando).
     * 
     * @param deslocamento int de deslocamento de câmera em x
     * @throws InterruptedException 
     */
    public void exibir(int deslocamento) throws InterruptedException {
        
        //carregar animação
        switch (estado) {
            case 0: //morreu
                if (ciclo < 20)
                    desenho = animVar.get(0);
                else
                    if (ciclo < 40)
                        desenho = animVar.get(1);
                    else
                        if (ciclo < 60)
                            desenho = animVar.get(2);
                        else
                            desenho = animVar.get(1);
                break;
            case 1: //parado
                if (direção == 2)
                    desenho = animDir.get(0);
                else
                    desenho = animEsq.get(0);
                break;
            case 2: //andando
                if (direção == 2) {
                    if (ciclo < 10)
                        desenho = animDir.get(2);
                    else
                        if (ciclo < 20)
                            desenho = animDir.get(1);
                        else
                            if (ciclo < 30)
                                desenho = animDir.get(2);
                            else
                                desenho = animDir.get(3);
                }else{
                    if (ciclo < 10)
                        desenho = animEsq.get(2);
                    else
                        if (ciclo < 20)
                            desenho = animEsq.get(1);
                        else
                            if (ciclo < 30)
                                desenho = animEsq.get(2);
                            else
                                desenho = animEsq.get(3);
                }
                break;
            case 3: //abaixado
                if (direção == 2)
                    desenho = animDir.get(4);
                else
                    desenho = animEsq.get(4);
                break;
            case 4: //pulando
                if (direção == 2)
                    desenho = animDir.get(5);
                else
                    desenho = animEsq.get(5);
                break;
            case 5: case 6: //dando soco no chão ou no ar
                if (direção == 2) {
                    desenho = animDir.get(6);
                    mão = animDir.get(7);
                }else{
                    desenho = animEsq.get(6);
                    mão = animEsq.get(7);
                }
                break;
        }
        
        desenho.x = x + deslocamento;
        desenho.y = y;
        desenho.draw();
        
        if (estado > 4) { //se estiver dando soco, desenhar a mão
            if (direção == 2)
                mão.x = desenho.x + 30;
            else
                mão.x = desenho.x - 16;
            mão.y = desenho.y + 16;
            mão.draw();
        }
    }
}
