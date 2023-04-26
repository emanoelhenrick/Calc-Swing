package br.com.emhk.calc.visao;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class Botao extends JButton {

  public Botao(String texto, Color cor) {
    setText(texto);
    setOpaque(true);
    setFont(new Font("arial", Font.PLAIN, 25));
    setBackground(cor);
    setForeground(Color.WHITE);
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }
}
