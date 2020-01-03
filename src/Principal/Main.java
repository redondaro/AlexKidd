/*       UNESP Câmpus de Presidente Prudente
         SECOMPP - Semana da Computação 2019
       Minicurso:  Desenvolvimento de Jogos 2D

   Alex Kidd
   Desenvolvido por: Luiz F. P. Redondaro           */

package Principal;

import Cenario.Jogo;

/** Esta é a classe principal, que executa o jogo. Carrega os elementos do jogo e
 * cria o loop infinito de atualizar e exibir o jogo, em um ciclo dentro de 60 FPS.
 * 
 * @author Luiz Fernando Perez Redondaro
 */
public class Main {
    
    /** Execução.
     * 
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        
        //carregar elementos do jogo
        Jogo jogo = new Jogo();
        
        //elementos de exibição de fps
        int fps = 0, contfps = 0;  //número de quadros exibidos
        long tempoFPSini = System.currentTimeMillis(), //tempo inicial para exibir fps
             tempoCiclo = - 1000; //tempo inicial para ciclo
        
        while (true) {
            
            //contador de FPS
            if (System.currentTimeMillis() >= tempoFPSini + 1000) {
                //System.out.println(tempoatu- tempoini); //testar a quantidade de segundos passados
                tempoFPSini = System.currentTimeMillis();
                fps = contfps;
                contfps = 0;
            }
            
            //permite que o jogo seja executado em um ciclo de 60 FPS
            //fórmula: 1000ms(1 segundo) / 60FPS (desejado) = ~17
            //então, a cada 17 milissegundos que o jogo deve ser atualizado
            if (System.currentTimeMillis() >= tempoCiclo + 17) { //garantir máximo de 60 repetições/min
                tempoCiclo = System.currentTimeMillis();
                
                jogo.atualizar(); //atualizar movimentações, alex e inimigos
                
                contfps++; //contar número de iterações para fps
                
                jogo.exibir(fps); //exibir cenário, alex e inimigos
            }
        }
    }
}
