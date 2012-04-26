public class Tools {
    public Tools()
    {}
    
    public Field getRandomFreeField(Field[][] fields, int xMax, int yMax){
        
        java.util.Random rand1 = new java.util.Random();
        java.util.Random rand2 = new java.util.Random();
        
        int num1 = rand1.nextInt(10000) % 100;
        int num2 = rand2.nextInt(10000) % 100;
        
        while (fields[num1%xMax][num2%yMax].getState() != fieldState.cFree) {
            num1 = rand1.nextInt(10000) % 100;
            num2 = rand2.nextInt(10000) % 100;
        }
        
        return fields[num1%xMax][num2%yMax];
    }
    
    public Field[] CleanArray(Field[] array){
         int z = 0;
         int n=0;
         
         //sort
        for (int i = 0; i < array.length-1; i++) {
            z = i;
            do{
                array[z] = array[z+1];
                array[z+1]=null;
                z++;
            } while  (z<array.length-2);
        }
        
        //count
        for (int i = 0; i < array.length; i++) {
            if (array[i]==null) {
                n++;
            }
        }
        
        //clean
        Field[] newArray = new Field[array.length-(n)];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = array[i];
        }
        
        //return
        return newArray;
    }
    
    public Field[] getCross(Field[][] fields, Field loc){
        int[][] pos = new int[][]
        {
            {-1,0,1},
            {-1,0,1},
            {-1,0,1}
        };
        int index = 0;
        Field[] cross = new Field[5];
        
        for (int i = 1; i < 9; i+=2) {
            int x  = ((i % 3) - 1)-1;
            int y = ((int)(i/3))-1;
            
            try{cross[index] = fields[loc.getX()+pos[x][y]][loc.getY()+pos[y][x]];}
            catch(Exception e){cross[index] = null;}
            
            index++;
        }
        
        return CleanArray(cross);
    }
    
    public Field[] getSquare(Field[][] fields, Field loc){
        int[][] pos = new int[][]
        {
            {-1,0,1},
            {-1,0,1},
            {-1,0,1}
        };
        int index = 0;
        Field[] square = new Field[9];
        
        for (int i = 0; i < 9; i++) {
            int x  = ((i % 3) - 1);
            int y = ((int)(i/3));
            
            try{square[i] = fields[loc.getX()+pos[x][y]][loc.getY()+pos[y][x]];}
            catch(Exception e){}
            
            index++;
        }
        
        return CleanArray(square);
    }
    
    //<editor-fold defaultstate="collapsed" desc="comment">
    /*
     * public String getSpriteName(Field f){
     * switch(f.getState())
     * {
     * default:
     * return "sprites/button_11.gif";
     * case cFree:
     * return "sprites/button_11.gif";
     * case cMine:
     * return "sprites/button_11.gif";
     * case cFlagF:
     * return "sprites/button_10.gif";
     * case cFlagM:
     * return "sprites/button_10.gif";
     * case uMine:
     * return "sprites/button_9.gif";
     * case uFree:
     * switch(f.getNumber())
     * {
     * case 0:
     * return "sprites/button_0.gif";
     * case 1:
     * return "sprites/button_1.gif";
     * case 2:
     * return "sprites/button_2.gif";
     * case 3:
     * return "sprites/button_3.gif";
     * case 4:
     * return "sprites/button_4.gif";
     * case 5:
     * return "sprites/button_5.gif";
     * case 6:
     * return "sprites/button_6.gif";
     * case 7:
     * return "sprites/button_7.gif";
     * case 8:
     * return "sprites/button_8.gif";
     * default:
     * return "sprites/button_11.gif";
     * }
     * }
     * }
     */
    //</editor-fold>
    
    public Field[][] numbers(Field[][] fields, int xMax, int yMax){
        Field[] f ;
        int z;
        for (int i = 0; i < xMax; i++) {
            for (int j = 0; j < yMax; j++) {
                f = getSquare(fields, fields[i][j]);
                z=0;
                for (int k = 0; k < f.length; k++) {
                    try{
                        if (f[k].getState()==fieldState.cMine) {
                            z++;
                        }
                    }
                    catch(Exception e){}
                }
                fields[i][j].setNumber(z);
                if (fields[i][j].getState() == fieldState.cMine) {
                    fields[i][j].setNumber(9);
                }
                
            }
        }
        return fields;
    }
    public int number(Field[][] fields, Field loc){
        Field[] f =getSquare(fields, loc);
        int z = 0;
            for (int k = 0; k < f.length; k++) {
                try{
                    if (f[k].getState()==fieldState.cMine) {
                        z++;
                    }
                }
                catch(Exception e){}
            }
            
            if (loc.getState() == fieldState.cMine) {
                z=11;
            }
            if (loc.getState() == fieldState.cFree) {
                z=11;
            }
            if (loc.getState() == fieldState.cFlagM) {
                z=10;
            }
            if (loc.getState() == fieldState.cFlagF) {
                z=10;
            }
            if (loc.getState() == fieldState.uMine) {
                z=9;
            }
            
            return z;
        
    }
            
    public Field[][] mines(Field[][] fields, int xMax, int yMax, int mines){
        Field[][] f = fields;
        int z = mines;
        if (mines>xMax*yMax) {
            z=xMax*yMax;
        }
        while(z>0)
        {
            getRandomFreeField(f,xMax, yMax).setState(fieldState.cMine);
            
            z--;
        }
        return f;
    }
    
    public GGField[][] uncover(Field[][] fields, Field f, GGField[][] gf){
      switch(f.getState())
      {
          case cFree:
          case uFree:
              gf[f.getX()][f.getY()].setID(number(fields,fields[f.getX()][f.getY()]));
              break;
          case cMine:
          case uMine:
              gf[f.getX()][f.getY()].setID(9);
              break;
          default:
              break;
      }
      return gf;
  }
    public GGField[][] toggleFlag(Field[][] fields, Field f,GGField[][] gf){
      switch(f.getState())
      {
          default:
              break;
          case cFree:
              gf[f.getX()][f.getY()].setID(11);
              break;
          case cMine:
              gf[f.getX()][f.getY()].setID(11);
              break;
          case cFlagF:
              gf[f.getX()][f.getY()].setID(10);
              break;
          case cFlagM:
              gf[f.getX()][f.getY()].setID(12);
              break;
      }
      
      return gf;
      
  }
    
    public Field[][] uncover(Field[][] fields,Field f){
      switch(f.getState())
      {
          case cFree:
              f.setState(fieldState.uFree);
              break;
          case cMine:
              f.setState(fieldState.uMine);
              break;
          default:
              break;
      }
      
      fields[f.getX()][f.getY()] = f;
      return fields;
  }
    public Field[][] toggleFlag(Field[][] fields, Field f){
      switch(f.getState())
      {
          case cFree:
              f.setState(fieldState.cFlagF);
              break;
          case cMine:
              f.setState(fieldState.cFlagM);
              break;
          case cFlagF:
              f.setState(fieldState.cFlagM);
              break;
          case cFlagM:
              f.setState(fieldState.cMine);
              break;
          default:
              break;
      }
      fields[f.getX()][f.getY()] = f;
      
      return fields;
      
  }
    
}
