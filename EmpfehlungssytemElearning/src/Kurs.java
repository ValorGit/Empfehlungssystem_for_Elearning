public class Kurs {

    private String name;

    public Kurs(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return getName();
    }
    
}
