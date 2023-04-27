package br.com.emhk.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

  private enum TipoComando {
    ZERAR, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA;
  };

  private static Memoria instancia = new Memoria();
  private final List<MemoriaObservador> observadores = new ArrayList<>();
  private String textoAtual = "";
  private String textoBuffer = "";
  private boolean substituir = false;
  private TipoComando ultimaOperacao = null;
  
  private Memoria() {}

  public static Memoria getInstancia() {
    return instancia;
  }

  public void adicionarObservador(MemoriaObservador observador) {
    observadores.add(observador);
  }

  public String getTextoAtual() {
    return textoAtual.isEmpty() ? "0" : textoAtual;
  }

  public void processarComando(String texto) {

    TipoComando tipoComando = detectarTipoComando(texto);
    if(tipoComando == null) return;

    if(tipoComando == TipoComando.ZERAR) {
      textoAtual = "";
      textoBuffer = "";
      substituir = false;
      ultimaOperacao = null;
    } else if (tipoComando == TipoComando.NUMERO || tipoComando == TipoComando.VIRGULA) {
      textoAtual = substituir ? texto : textoAtual + texto;
      substituir = false;
    } else {
      substituir = true;
      textoAtual = obterResultadoOperacao();
      textoBuffer = textoAtual;
      ultimaOperacao = tipoComando;
    }

    observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
  }

  private TipoComando detectarTipoComando(String texto) {
    if(textoAtual.isEmpty() && texto == "0") return null;
    try {
      Integer.parseInt(texto);
      return TipoComando.NUMERO;
    } catch (NumberFormatException e) {
      if("AC".equals(texto)) return TipoComando.ZERAR;
      if("/".equals(texto)) return TipoComando.DIV;
      if("*".equals(texto)) return TipoComando.MULT;
      if("-".equals(texto)) return TipoComando.SUB;
      if("=".equals(texto)) return TipoComando.IGUAL;
      if(",".equals(texto) && !textoAtual.contains(",")) return TipoComando.VIRGULA;
      if("+".equals(texto)) return TipoComando.SOMA;
    }
    return null;
  }

  private String obterResultadoOperacao() {
    if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) return textoAtual;
    double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
    double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
    double resultado = 0;
    if (ultimaOperacao == TipoComando.SOMA) resultado = numeroBuffer + numeroAtual;
    if (ultimaOperacao == TipoComando.SUB) resultado = numeroBuffer - numeroAtual;
    if (ultimaOperacao == TipoComando.MULT) resultado = numeroBuffer * numeroAtual;
    if (ultimaOperacao == TipoComando.DIV) resultado = numeroBuffer / numeroAtual;
    String resultadoString = Double.toString(resultado).replace(".", ",");
    boolean inteiro = resultadoString.endsWith(",0");
    return inteiro ? resultadoString.replace(",0", "") : resultadoString;
  }
}
