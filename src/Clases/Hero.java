package Clases;

public class Hero extends Player{
    
    private boolean wish = true;
    private boolean crush = true;
    private boolean strengthUp;
    private boolean judgement;
    
    public Hero(){ //65 points
        max_hp = 25;    
        hp = max_hp;
        max_mp = 17;
        mp = max_mp;
        str = 7;
        def = 4;
        magic = 5;
        skill = 4;
        speed = 3;
        exp = 0;
        max_exp = 100;
        lvl = 1;
        
        strengthUp = false;
        judgement = false;
        number_of_skills = 2;       
        
        speed_up = false;
        speed_buff = 0;
        speed_enhance = 0;
        strength_up = false;
        strength_buff = 0;
        strength_enhance = 0;
        defense_up = false;
        defense_buff = 0;
        defense_enhance = 0;
    }    
    
    @Override
    public int attack(){
        return str + 7;
    }
    
    @Override
    protected int howManySkills() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private int wish(boolean player){
        if(mp < 6 && !player){
            return 0;
        }
        if(player){
            if(mp < 9){
                return 0;
            }else{
                mp = (mp - 9 <= 0)? 0 : mp - 9;
                hp = max_hp;
                System.out.println("Heavens above have granted your celestial wish.");
                return -1;
            }    
        }else{//Enemy is "false", Player is "true"
            mp = (mp - 9 <= 0)? 0 : mp - 9;
            hp = max_hp;
            System.out.println("Heavens above have granted your celestial wish.");
            return -1;
        }
    }
    
    private int crush(){
        if(mp < 4){
            return 0;
        }
        else{
            mp = (mp - 4 <= 0)? 0 : mp - 4;
            return magic + 6;
        }   
    }
    
    private int strengthUp(boolean player){
        return 0;
    }
    
    private int judgement(){
        return 0;
    }
    
    @Override
    public void printSkills(){
        System.out.println("1.Wish (-9 mana)          2.Destroy (-4 mana)");
        if(strengthUp){
            System.out.println("3.Strength Up (-6 mana)   ");
        }
        if(judgement){
            System.out.print("4.Judgement (-10 mana)");
        }
    }
    
    /**
     * This function is used as a way for the program to interpret the choice and outcome of the skills that 
     * a character possesses.
     * @param choice Choice made by the user. 
     * <br>
     * @param enemy_def Needed for damage calculations for cases where an attack skill depends on it.
     * <br>
     * @param player Boolean value needed for testing the outcome of a choice made by the user (passed onto skills inside). 
     *            Use false for testing and use true for use.
     * <br>
     * @return <p>A unique number for two situations, an attack skill and a status effect skill. Since 
     * a <b>status effect</b> skill modifies the properties of a character and doesn't deal damage like an attack skill, 
     * these two have to be separated for the <b>battle phase</b> outcome.</p>
     *         <h3>What it returns:</h3>
     *          <ul>
     *              <li><b style="color:#42a0ce">-7</b>: if the choice made is the status effect skill</li>
     *              <li><b style="color:#42a0ce">*-{-1}</b>: (everything else except -1), if the choice made is the attack skill</li>
     *              <li><b style="color:#42a0ce">-1</b>: if the user makes a wrong choice</li>
     *          </ul>
     */
    @Override
    public int skills(int choice, int enemy_def, boolean player){
        if(choice == 1){
            return wish(player);
        }else if(choice  == 2){
            return crush(); //returns damage
        }else if(strengthUp && choice == 3){
            return strengthUp(player);
        }else if(judgement && choice == 4){
            return judgement();
        }else return -4; //returns a wrong choice
    }

    @Override
    public void lvlUp(){ //18 points distributed
        System.out.println("Level up!");
        
        int new_hp = rand.nextInt(6) + 1;
        System.out.println("Max HP: "+max_hp+" +"+new_hp);
        max_hp += new_hp;
        hp += new_hp;
        
        int new_mp = rand.nextInt(3) + 1;
        System.out.println("Max MP: "+max_mp+" +"+new_mp);
        max_mp += new_mp;
        mp += new_mp;
        
        int new_str = rand.nextInt(3) + 1;
        System.out.println("Strenght: "+str+" +"+new_str);
        str += new_str;
        
        int new_def = rand.nextInt(2) + 1;
        System.out.println("Defence: "+def+" +"+new_def);
        def += new_def;
        
        int new_magic = rand.nextInt(2) + 1;
        System.out.println("Magic: "+magic+" +"+new_magic);
        magic += new_magic;
        
        int new_skill = rand.nextInt(1) + 1;
        System.out.println("Skill: "+skill+" +"+new_skill);
        skill += new_skill;
        
        int new_speed = rand.nextInt(1) + 1;
        System.out.println("Speed: "+speed+" +"+new_speed);
        speed += new_speed;
        
        lvl++;
        max_exp += newMaxExp();
        
        System.out.println("");
        if(lvl == 3){
            strengthUp = true;
            System.out.println("New skill learned!!: Strength Up");
        }
        if(lvl == 4){
            judgement = true;
            System.out.println("New skill learned!!: Judgement");
        }
   }
    
    @Override
    public void imprimir(){
        System.out.println("Hero stats: HP: "+hp+"/"+max_hp+"         Strenght: "+str);
        System.out.println("Lvl "+lvl+"       MP: "+mp+"/"+max_mp+"         Magic: "+magic+"\n");
    }
}
