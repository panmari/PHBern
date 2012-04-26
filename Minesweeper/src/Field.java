public class Field
{
    private Tools tools;
    private int x;
    private int y;
    
    private fieldState state;
    
    private int num;
    
    public Field(int x, int y){
        this.x = x;
        this.y = y;
        this.tools = new Tools();
        this.state = fieldState.cFree;
        
        this.num = 0;
        
    }
    public Field(int x, int y, fieldState state, int num, String spriteName){
        this.x = x;
        this.y = y;
        
        this.state = state;
        
        this.num = num;
    }
    
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y; 
    }
    
    public fieldState getState(){
        return this.state;
    }
    public void setState(fieldState state){
        this.state = state; 
    }
    
    public int getNumber(){
        return this.num;
    }
    public void setNumber(int n){
        this.num = n; 
    }
}