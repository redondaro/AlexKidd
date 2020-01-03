/*       UNESP Câmpus de Presidente Prudente
         SECOMPP - Semana da Computação 2019
       Minicurso:  Desenvolvimento de Jogos 2D

   Alex Kidd
   Desenvolvido por: Luiz F. P. Redondaro           */

package Cenario;

import Alex.Alex;
import Inimigo.Inimigo;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import jplay.Keyboard;
import jplay.Window;

/** Classe responsável pela dinâmica do jogo. Faz a inicialização dos elementos
 * e suas imagens, atualização das entradas do jogador e as posições dos
 * elementos, e a exibição dos elementos na área visível.
 * 
 * @author Luiz Fernando Perez Redondaro
 */
public class Jogo {
    
    /** Janela principal. */
    private Window janela;
    
    /** Capturar teclas. */
    private Keyboard teclado;
    
    /** Exibição de dica. */
    private boolean dica;
    
    /** Deslocamento para a tela acompanhar personagem. */
    private int cameraX;
    
    /** ArrayList de objetos no mapa. */
    private ArrayList<Objeto> objetos;
    
    /** ArrayList de inimigos no mapa. */
    private ArrayList<Inimigo> inimigos;
    
    /** Personagem Alex. */
    private Alex alex;
    
    /** Número de linhas e colunas no mapa. */
    private int numLinhas = 0, numColunas = 0;
    
    /** variáveis de entrada de movimentação. */
    boolean esquerda, direita, baixo, pulo, soco;
    
    /** Obter pontuação do jogador.
     * 
     * @return int pontos
     */
    public int getPontos() {
        return alex.getDinheiro();
    }
    
    /** Construtor, que inicializa janela e outros elementos do jogo, carregando
     * objetos, inimigos e a posição inicial do Alex no arquivo mapa.pgm. */
    public Jogo() {
        janela = new Window(640,480);
        janela.setTitle("Alex Kidd - SECCOMP 2019");
        teclado = janela.getKeyboard();
        
        dica = true;
        
        objetos = new ArrayList<Objeto>();
        inimigos = new ArrayList<Inimigo>();
        
        //abrir arquivo .pgm e carregar elementos do jogo
        try{
            Scanner scan = new Scanner(new File("src/Cenario/mapa.pgm"));
            String linha = "";
            boolean comentário = true;
            do {
                linha = scan.nextLine();    //tipo de arquivo
                if (linha.charAt(0) != '#')
                    comentário = false;
            } while (comentário);
            do {
                if (scan.hasNextInt()) {    //dimensões da imagem
                    numColunas = scan.nextInt();
                    while (scan.hasNext() && !scan.hasNextInt())
                        scan.next();
                    numLinhas = scan.nextInt();
                    comentário = false;
                    while (scan.hasNext() && !scan.hasNextInt())
                        scan.next();
                    scan.nextInt();
                }else{
                    scan.nextLine();
                    comentário = true;
                }
            } while (comentário);
            int contx = 0, conty = 0, valor;
            int superfície[] = new int[numColunas]; //armazena superfícies
            for (int i = 0; i < numColunas; i++)
                superfície[i] = -1;
            while (scan.hasNext() && conty < numLinhas) { //dados da imagem
                while (scan.hasNext() && !scan.hasNextInt())
                    scan.next();
                valor = scan.nextInt();
                
                /* inserir objetos, de acordo com a cor no mapa
                        0 preto    - solo
                       18 azul     - dinheiro
                       54 vermelho - nuvem
                       73 rosa     - posição inicial do Alex
                      146 laranja  - pedras
                      182 verde    - baú de item
                      237 amarelo  - inimigo         */
                if (valor < 200 && (valor < 60 || valor >= 80)) { // objetos do cenário, não sendo o alex
                    Objeto novo = null;
                    if (valor < 10) { // preto: solo
                        if (conty > 0 && superfície[contx] < 0) {
                            novo = new Objeto("src//Cenario//solo.png", contx*32, conty*32, 1, 1); //é superfície
                            superfície[contx] = conty;
                        }else{
                            if (conty <= 0)
                                superfície[contx] = 0;
                            novo = new Objeto("src//Cenario//rocha.png", contx*32, conty*32, 1, 0); //não é superfície
                        }
                    }else
                        if (valor < 20) // azul: dinheiro
                            novo = new Objeto("src//Cenario//money.png", contx*32, conty*32, 4, 0);
                        else
                            if (valor < 60) // vermelho: nuvem de cenário, intocável
                                novo = new Objeto("src//Cenario//nuvem.png", contx*32, conty*32, 0, 0);
                            else
                                if (valor < 150) //laranja: pedras que pode ser destruídas
                                    novo = new Objeto("src//Cenario//pedra.png", contx*32, conty*32, 2, 1);
                                else
                                    if (valor < 190) //verde: baú de item
                                        novo = new Objeto("src//Cenario//star.png", contx*32, conty*32, 3, 1);
                    objetos.add(novo);
                }else{
                    if (valor >= 60 && valor < 80) //alex
                        alex = new Alex(contx*32, conty*32);
                    else
                        if (valor < 250) // inimigo
                            inimigos.add(new Inimigo(contx*32, conty*32));
                }
                contx++;
                if (contx >= numColunas) {
                    contx = 0;
                    conty++;
                }
            }
            scan.close();
        }catch(FileNotFoundException | NoSuchElementException ex) {
            JOptionPane.showMessageDialog(null, "Arquivo \"mapa.pgm\" inválido!", "Ops", 0);
            System.exit(0);
        }
    }
    
    /** Atualiza entrada dos controles, a câmera e a movimentação dos personagens.
     * 
     * @throws InterruptedException 
     */
    public void atualizar() throws InterruptedException {
        
        //se apertar ESC, encerra o jogo
        if (teclado.keyDown(Keyboard.ESCAPE_KEY))
            System.exit(0);

        //se apertar A, exibe/esconde dicas
        if (teclado.keyDown(Keyboard.A_KEY))
            dica = !dica;
        
        //ler movimentação
        esquerda = teclado.keyDown(Keyboard.LEFT_KEY);
        direita = teclado.keyDown(Keyboard.RIGHT_KEY);
        baixo = teclado.keyDown(Keyboard.DOWN_KEY);
        pulo = teclado.keyDown(Keyboard.UP_KEY) || teclado.keyDown(Keyboard.D_KEY);
        soco = teclado.keyDown(Keyboard.S_KEY);
        
        cameraX = 305 - ((int)alex.getX());
        
        //ajustando o deslocamento/câmera para os limites do cenário
        if (cameraX > 0)
            cameraX = 0;
        else
            if (cameraX < 640 - numColunas*32)
                cameraX = 640 - numColunas*32;
        
        //atualizar controles e movimentar Alex
        alex.controle(esquerda, direita, baixo, pulo, soco); //apenas passa as entradas para alex
        alex.atualizar(objetos, inimigos);
        
        for(Inimigo in: inimigos) //movimentar inimigos
            in.atualizar(objetos);
    }
    
    /** Exibe todos os elementos do jogo: cenário, objetos, inimigos, alex e texto.
     * 
     * @param fps Valor de FPS
     * @throws InterruptedException 
     */
    public void exibir(int fps) throws InterruptedException {
        janela.clear(Color.BLUE); //apagar tela com a cor de fundo
        
        //exibir objetos e cenário
        for(Objeto obj: objetos)
            //if (Math.abs(obj.getX() - alex.getX()) <= 640) //imprime apenas objetos que estejam no campo de visão
                obj.exibir(cameraX);
        
        for(Inimigo in: inimigos) //exibir inimigos
            in.exibir(cameraX);
        
        alex.exibir(cameraX); //exibir alex
        
        if (dica) { //exibir textos de dicas
            janela.drawText("Alex Kidd in Miracle World - SECOMPP 2019 Edition"
                    +"          R$: " + alex.getDinheiro() + ",00"
                    +"          FPS: " + fps, 10, 20, Color.white, new Font("Default", 0, 14));
            janela.drawText("Setas ← ou →: movimento     S: soco     ↑ ou D: pulo"
                    +"     A: exibe/oculta dicas     Esc: sair", 10, 40, Color.white, new Font("Default", 0, 14));
        }
        
        janela.update(); //atualizar desenho na janela
    }
}
