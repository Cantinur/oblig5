import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stabel<T> implements Liste<T>{

    protected Node foran;
    protected int teller = 0;

    public void settInn(T element){
      if (erTom()){
        foran = new Node(element);
      } else {
        Node n = new Node(element);
        n.neste = foran;
        foran = n;
      }
      teller++;
    }

    public int storrelse(){
      return teller;
    }

    public boolean erTom(){
      return foran == null;
    }

    public T fjern(){
      if (erTom()) return null;
      Node n = foran;
      foran = n.neste;
      teller--;
      return n.data;
    }

    public Iterator<T> iterator(){
      return new EgenIterator();
    }


    public class EgenIterator implements Iterator<T>{
      Node denne = foran;


      public T next() throws NoSuchElementException{
        try{
          T data = denne.data;
          denne = denne.neste;
          return data;

        }catch (NoSuchElementException e) {
          System.out.println("Her er det feil.");
          System.exit(0);
        }
        return null;
      }

      public void remove(){
        Node n = foran;
        foran = n.neste;
        teller--;
      }

      public boolean hasNext(){
        return denne != null;
      }
    }

    protected class Node{
      Node(T data){
        this.data = data;
      }
      T data;
      Node neste;
      Node forrige;
    }

}
