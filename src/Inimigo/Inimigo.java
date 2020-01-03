/*       UNESP Câmpus de Presidente Prudente
         SECOMPP - Semana da Computação 2019
       Minicurso:  Desenvolvimento de Jogos 2D

   Alex Kidd
   Desenvolvido por: Luiz F. P. Redondaro           */

package Inimigo;

import Cenario.Objeto;
import java.util.ArrayList;
import jplay.Sprite;

/** Classe do personagem inimigo do Alex, que fica voando em uma dada direção
 * horizontal. Quando encosta em um objeto, muda de direção.
 * 
 * @author Luiz Fernando Perez Redondaro
 */
public class Inimigo {
    
    private ArrayList<Sprite> animEsq, animDir;
    
    /** Movimentação auxiliar (x) e velocidade do movimento. */
    private double x,
                   velocidade;
    
    /** Direção do personagem (1-esq, 2-dir) e ciclo de animação. */
    private int    direção,
                   ciclo;
    
    /** Imagem atual da animação do personagem. */
    private Sprite desenho;
    
    /** Obter posição no eixo x.
     * 
     * @return double x
     */
    public double getX() {
        return x;
    }
    
    /** Obter Sprite do personagem.
     * 
     * @return Sprite
     */
    public Sprite getImagem() {
        return desenho;
    }
    
    /** Método contrutor, que inicializa a animação e as posições iniciais.
     * 
     * @param x Posição no eixo x, que será variada com o movimento do personagem
     * @param y Posição no eixo y, que não será alterada
     */
    public Inimigo(int x, int y) {
        animEsq = new ArrayList<Sprite>();
        animDir = new ArrayList<Sprite>();
        
        //carregando elementos
        animEsq.add(new Sprite("src//Inimigo//inimigo1.png", 1));
        animEsq.add(new Sprite("src//Inimigo//inimigo2.png", 1));
        animDir.add(new Sprite("src//Inimigo//inimigo1_dir.png", 1));
        animDir.add(new Sprite("src//Inimigo//inimigo2_dir.png", 1));
        
        desenho = new Sprite("src//Inimigo//inimigo1.png", 1); //imagem categórica, para verificar colisões
        
        //acertando valores
        this.x = desenho.x = x;
        desenho.y = y;
        direção = 1;
        velocidade = 1.5;
        ciclo = 0;
        
        //acertando eixo y de todas as animações
        animEsq.get(0).y = animEsq.get(1).y = animDir.get(0).y = animDir.get(1).y = desenho.y = y;
    }
    
    /** Atualiza posição x do personagem, de acordo com a posição, mudando de
     * direção se colidir com algum objeto.
     * 
     * @param objetos Objetos que o personagem pode colidir.
     */
    public void atualizar(ArrayList<Objeto> objetos) {
        
        // movimentação de teste, para verificar colisão
        if (direção == 1)
            desenho.x -= velocidade;
        else
            desenho.x += velocidade;
        
        boolean colidiu = false; //verificar colisões com objetos do cenário
        for (Objeto obj: objetos) {
            if (obj.getTipo() == 1 || (obj.getCondição() >= 1)) { //se for superfície ou objeto paupável
                if (desenho.collided(obj.getImagem())) {
                    colidiu = true;
                    break;
                }
            }
        }
        if (colidiu) { //se colidiu, mudar direção do movimento
            if (direção == 1)
                direção = 2;
            else
                direção = 1;
        }
        
        //atualizando velocidade
        if (direção == 1)
            this.x -= velocidade;
        else
            this.x += velocidade;
        desenho.x = this.x;
    }
    
    /** Exibir personagem, caso esteja na parte visível da tela.
     * 
     * @param deslocamento Área visível da tela, onde se encontra o personagem Alex
     */
    public void exibir(int deslocamento) {
        //exibindo, apenas se estiver na parte visível da tela
        if (Math.abs(this.x + deslocamento) <= 640) {
            ciclo++;
            if (ciclo >= 500)
                ciclo = 0;
            if (direção == 1) { //esquerda
                if (ciclo < 250) {
                    animEsq.get(0).x = this.x + deslocamento;
                    animEsq.get(0).draw();
                }else{
                    animEsq.get(1).x = this.x + deslocamento;
                    animEsq.get(1).draw();
                }
            }else{ //direita
                if (ciclo < 250) {
                    animDir.get(0).x = this.x + deslocamento;
                    animDir.get(0).draw();
                }else{
                    animDir.get(1).x = this.x + deslocamento;
                    animDir.get(1).draw();
                }
            }
        }
    }
}
