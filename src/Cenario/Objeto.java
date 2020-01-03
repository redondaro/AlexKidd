/*       UNESP Câmpus de Presidente Prudente
         SECOMPP - Semana da Computação 2019
       Minicurso:  Desenvolvimento de Jogos 2D

   Alex Kidd
   Desenvolvido por: Luiz F. P. Redondaro           */

package Cenario;

import jplay.Sprite;

/** Classe de objetos do jogo, podendo ser: nuvem (decorativo, não tem interação
 * com o jogador), item (dinheiro), pedra (destrutível), rocha (plataforma),
 * solo (plataforma) ou baú com item.
 * 
 * @author Luiz Fernando Perez Redondaro
 */
public class Objeto {
    
    /** Tipo de objeto e condição (usado para baú de item). */
    private int tipo, condição;
    
    /** Posição do objeto, em (x,y). */
    private double x, y;
    
    /** Figura do objeto. */
    private Sprite imagem;
    
    /** Construtor, definindo atributos do objeto.
     * 
     * @param filename Arquivo com a imagem do objeto
     * @param x Posição em X
     * @param y Posição em Y
     * @param tipo Tipo de objeto
     * @param condição Condição do objeto
     */
    public Objeto(String filename, int x, int y, int tipo, int condição) {
        imagem = new Sprite(filename, 1);
        this.x = x;
        imagem.y = this.y = y;
        this.tipo = tipo;
        this.condição = condição;
    }
    
    /** Redefinir atributos do objeto.
     * 
     * @param filename Arquivo com a imagem do objeto
     * @param tipo Tipo de objeto
     * @param condição Condição do objeto
     */
    public void redefinir(String filename, int tipo, int condição) {
        imagem = new Sprite(filename, 1);
        imagem.y = y;
        this.tipo = tipo;
        this.condição = condição;
    }
    
    /** Obter tipo de objeto.
     * 
     * @return int tipo
     */
    public int getTipo() {
        return tipo;
    }
    
    /** Definir tipo de objeto.
     * 
     * @param tipo int
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    /** Obter condição do objeto.
     * 
     * @return int condição
     */
    public int getCondição() {
        return condição;
    }
    
    /** Definir condição do objeto.
     * 
     * @param condição int
     */
    public void setCondição(int condição) {
        this.condição = condição;
    }
    
    /** Obter posição do objeto em x.
     * 
     * @return double x
     */
    public double getX() {
        return x;
    }
    
    /** Obter posição do objeto em y.
     * 
     * @return double y
     */
    public double getY() {
        return y;
    }
    
    /** Obter imagem do objeto.
     * 
     * @return Sprite imagem
     */
    public Sprite getImagem() {
        return imagem;
    }
    
    /** Exibe o objeto. Adiciona deslocamento para exibir na posição correta da
     * tela, exibe e retoma o valor original.
     * 
     * @param deslocamentoX Deslocamento em x
     */
    public void exibir(int deslocamentoX) {
        imagem.x = this.x + deslocamentoX;
        imagem.draw();
        imagem.x = this.x;
    }
}
