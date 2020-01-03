# AlexKidd
Conteúdo do minicurso "Desenvolvimento de Jogos 2D" que ministrei na SECOMPP em 2019. Desenvolvido em Java 8, na IDE NetBeans 8.


## O que esse projeto faz?
Usando a biblioteca JPlay, criei essa engine de jogo. É um modelo de jogo 2D completo, contendo os elementos técnicos: loop infinito do jogo, FPS, hitbox com colisões, ciclo de animação de personagens, câmera e mapa de objetos.


## Como executar?
Basta abrir o projeto em uma versão do NetBeans/Java equivalente, adicionar a biblioteca JPlay.jar que está na pasta lib e executar.


## Mapa de objetos
Como esta é uma parte complexa, alheia ao código fonte, explicarei à parte neste tópico. 
A posição do Alex, os elementos do cenário e os inimigos são inicializados de acordo com o arquivo "mapa.pgm". 
Esse arquivo arquivo armazena uma imagem em tons de cinza, com valor de cada pixel variando de 0 a 255. 
Por comodidade, existe um arquivo "mapa.png" que pode ser alterado em qualquer editor de imagens, 
devendo ser usadas as respectivas cores para os elementos, para então salvar o "mapa.pgm" em MODO TEXTO (não binário!!!). 
Seguem os respectivos valores de cinza (arquivo pgm) e a cor entre parênteses (arquivo png) relacionados a cada elemento:
- 0 (preto) = solo
- 18 (azul) = item (dinheiro)
- 54 (vermelho) = nuvem (decorativo, que não colide com personagem)
- 73 (rosa) = posição inicial do Alex
- 146 (laranja) = pedras (que podem ser destruídas com soco)
- 182 (verde) = baú de item
- 237 (amarelo) = inimigo

Na figura abaixo, temos: no canto superior esquerdo está o arquivo png aberto em um editor de imagens, abaixo os objetos do cenário (item, nuvem, ...), à direita o arquivo pgm aberto em um editor de texto, e na parte de baixo, o cenário gerado.

![](/mapa_objetos.png)


## Informações técnicas
Para informações mais detalhadas, consultar javadoc em dist/javadoc/index.html.
