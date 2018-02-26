import java.util.Iterator;
import java.util.NoSuchElementException;

class OrdnetLenkeliste<T extends Comparable<T>> extends Stabel<T>{
  private Node bak;

  @Override
  public void settInn(T element){
    Node nyNode = new Node(element);
    if(erTom()){
      foran = nyNode;
      bak = nyNode;
    } else if(element.compareTo(foran.data) < 0){
      foran.forrige = nyNode;
      nyNode.neste = foran;
      foran = nyNode;
    } else if(element.compareTo(bak.data) > 0){
      bak.neste = nyNode;
      nyNode.forrige= bak;
      bak = nyNode;
    } else {
      for(Node n = foran; n.neste != null; n = n.neste){
        if(element.compareTo(n.neste.data) < 0){
          nyNode.neste = n.neste;
          n.neste = nyNode;
          break;
        }
      }
    }
    teller++;
  }
}
