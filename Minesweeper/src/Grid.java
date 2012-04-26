public class Grid {
    Tools tools;
    private Field[][] fields;
    private int xMax;
    private int yMax;
    private int mines;
    
    public Grid(int xMax, int yMax, int mines){
        this.xMax = xMax;
        this.yMax = yMax;
        this.tools = new Tools();
        this.mines = mines;
        this.fields = new Field[xMax][yMax];
        
        for(int i = 0;i<xMax;i++)
        {
            for(int j = 0;j<yMax;j++)
            {
                this.fields[i][j] = new Field(i,j);
            }
        }    
        this.fields = tools.mines(this.fields, this.xMax, this.yMax, this.mines);
        this.fields = tools.numbers(fields, this.xMax, this.yMax);
    }
    /*
    public Field[][] doMines()
    {
       return  tools.mines(fields, xMax, yMax, mines);
    }
    
    public Field[][] doNumbers()
    {
        return tools.numbers(fields, xMax, yMax);
    }
    */
    
    public int getXMax(){
        return this.xMax;
    }
    public int getYMax(){
        return this.yMax;
    }
    
    public Field[][] getGrid(){
        return fields;
    }
}