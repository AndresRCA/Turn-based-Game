package Clases;

public class Mage extends Player{
    //make a burning effect in deBuffGradually() and a healing effect.
    private boolean defenseUp = true;
    private boolean lighting = true;
    private boolean slowHeal; //A small heal every turn (+1 or +2), I probably need a public method for any effect like this
    private boolean fireBlow; //A small fire attack that burns the enemy every turn, needs a public method
    
    public Mage(){ //65 points
        max_hp = 19;    
        hp = max_hp;
        max_mp = 27;
        mp = max_mp;
        str = 3;
        def = 2;
        magic = 8;
        skill = 2;
        speed = 4;
        exp = 0;
        max_exp = 100;
        lvl = 1;
        
        slowHeal = false;
        fireBlow = false; 
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
        slow_heal_up = false;
        slow_heal_buff = 2;
    }
    
    @Override
    public int attack(){
        return str + 2;
    }
    
    @Override
    protected int howManySkills(){
        int number_of_total_skills = 0;
        if(defenseUp) number_of_total_skills++;
        if(lighting) number_of_total_skills++;
        if(slowHeal) number_of_total_skills++;
        if(fireBlow) number_of_total_skills++;
        return number_of_total_skills;
    }
    
    private int defenseUp(boolean player){
        if(player){            
            if(!defense_up){
                if(mp < 9){
                    return 0;
                }else{
                    mp = (mp - 9 <= 0)? 0 : mp - 9;
                    addDefenseBuff();
                    System.out.println("Defense up (+"+defense_buff+")!");
                    defense_up = true;
                    return -1;
                }
            }else{
                System.out.println("You already used it, wait "+defense_buff+" turn(s).");
                return -2;
            }           
        }else{           
            if(defense_up){
                return -7;
            }else{
                if(mp < 9){
                    return 0;
                }else{
                    mp = (mp - 9 <= 0)? 0 : mp - 9;
                    addDefenseBuff();
                    System.out.println("Defense up (+"+defense_buff+")!");
                    return -1;
                }
            }         
        }
    }
    
    private int lighting(){
        if(mp < 7){
            return 0;
        }
        else{
            mp = (mp - 7 <= 0)? 0 : mp - 7;
            return magic + 8;
        }  
    }
    
    private int slowHeal(boolean player){
        if(player){
            if(!slow_heal_up){
                if(mp < 5){
                    return 0;
                }else{
                    mp = (mp - 5 <= 0)? 0 : mp - 5;
                    addSlowHeal();
                    System.out.println("+2 hp for 5 turns.");
                    return -1;
                }
            }else{
                System.out.println("You already used it, wait "+slow_heal_duration+" turn(s).");
                return -2;
            }
        }else{
            if(slow_heal_up){
                return -7;
            }else{
                if(mp < 5){
                    return 0;
                }else{
                    mp = (mp - 5 <= 0)? 0 : mp - 5;
                    addSlowHeal();
                    System.out.println("+2 hp for 5 turns.");
                    return -1;
                }
            }
        }    
    }
    
    private int fireBlow(){ //How do I transfer a status effect?
        return 0;
    }
    
    @Override
    public void printSkills(){
        System.out.println("1.Defense up (-9 mana)    2.Lighting (-7 mana)");
        if(slowHeal){
            System.out.println("3.Slow Heal (-5 mana)     ");
        }
        if(fireBlow){
            System.out.print("4.Fire Blow (-10 mana)");
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
            return defenseUp(player);
        }else if(choice  == 2){
            return lighting(); //returns damage
        }else if(slowHeal && choice == 3){
            return slowHeal(player);
        }else if(fireBlow && choice == 4){
            return fireBlow();
        }else return -4;
    }
    
    @Override
    public void lvlUp(){ //18 points distributed
        System.out.println("Level up!");
        
        int new_hp = rand.nextInt(3) + 1;
        System.out.println("Max HP: "+max_hp+" +"+new_hp);
        max_hp += new_hp;
        hp += new_hp;
        
        int new_mp = rand.nextInt(6) + 1;
        System.out.println("Max MP: "+max_mp+" +"+new_mp);
        max_mp += new_mp;
        mp += new_mp;
        
        int new_str = rand.nextInt(1) + 1;
        System.out.println("Strenght: "+str+" +"+new_str);
        str += new_str;
        
        int new_def = rand.nextInt(1) + 1;
        System.out.println("Defence: "+def+" +"+new_def);
        def += new_def;
        
        int new_magic = rand.nextInt(3) + 1;
        System.out.println("Magic: "+magic+" +"+new_magic);
        magic += new_magic;
        
        int new_skill = rand.nextInt(2) + 1;
        System.out.println("Skill: "+skill+" +"+new_skill);
        skill += new_skill;
        
        int new_speed = rand.nextInt(2) + 1;
        System.out.println("Speed: "+speed+" +"+new_speed);
        speed += new_speed;
        
        defense_enhance += 2;
        
        lvl++;
        max_exp += newMaxExp();
        
        System.out.println("");
        if(lvl == 3){
            slowHeal = true;
            System.out.println("New skill learned!!: Slow Heal");
        }
        if(lvl == 4){
            fireBlow = true;
            System.out.println("New skill learned!!: Fire Blow");
        }
    }
    
    @Override
    public void imprimir(){
        System.out.println("Mage stats: HP: "+hp+"/"+max_hp+"         Strenght: "+str);
        System.out.println("Lvl "+lvl+"       MP: "+mp+"/"+max_mp+"         Magic: "+magic+"\n");
    }
}
