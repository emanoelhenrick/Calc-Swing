package br.com.emhk.calc.modelo;

@FunctionalInterface
public interface MemoriaObservador {
  
  public void valorAlterado(String novoValor);
}
