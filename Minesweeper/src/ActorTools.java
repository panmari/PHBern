import ch.aplu.jgamegrid.*;

public class ActorTools extends Actor{
    Tools tools;
    Field field;
    GGField gf;
    
    public ActorTools(GGField gf,Field f){
        this.field = f;
        this.gf = gf;
    }
    
    public void setSprite()
    {
        gf.setID(field.getNumber());
    }
}
