import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        HashTable maylin = new HashTable(10,0.75);
        maylin.put(20, "lol");
        maylin.put(10, "twerk");
        maylin.put(10, "bro");
        maylin.put(30,"rally");
        maylin.put(40, "eek");
        System.out.println(maylin.isEmpty());
        System.out.println(maylin.get(30));
        System.out.println(maylin.size());
        System.out.println(maylin.get(10));
        System.out.println("\n"+maylin.remove(10));
        System.out.println(maylin.size());
        System.out.println(maylin.remove(20) +""+ maylin.remove(30));
        System.out.println(maylin.size());
        
        
        maylin.clear();
        maylin.clear();
        System.out.println("\n" + maylin.remove(20));
        try {
            System.out.println(maylin.get(20));
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        try {
            System.out.println(maylin.remove(null));
        } catch(Exception e) {
            System.out.println(e);
        }
        System.out.println(maylin.size());
        System.out.println(maylin.isEmpty());
        try {
            System.out.println(maylin.put(null, "lmao"));
        } catch (Exception e){
            System.out.println(e);
        }
        
        //put, clear, get, isEmpty, remove, size
        
        
    }

}
