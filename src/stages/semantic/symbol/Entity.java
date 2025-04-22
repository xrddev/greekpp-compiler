package stages.semantic.symbol;

public abstract class Entity {
    String name;

    public Entity(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
