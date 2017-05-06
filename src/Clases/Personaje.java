package Clases;

import java.util.Random;


public abstract class Personaje{
    
    protected String image;
    protected int max_hp;
    protected int max_mp;
    protected int hp;
    protected int mp;
    protected int str;
    protected int magic;
    protected int def;
    protected int skill;
    protected int speed;
    protected int exp;
    protected int lvl;
    protected int number_of_skills;
    
    protected boolean defense_up;
    protected int defense_buff;
    protected int defense_enhance;
    protected boolean speed_up;
    protected int speed_buff;
    protected int speed_enhance;
    protected boolean strength_up;
    protected int strength_buff;
    protected int strength_enhance;
    
    protected boolean slow_heal_up;
    protected int slow_heal_buff;
    protected int slow_heal_supplied;
    protected int slow_heal_duration;
    
    Random rand = new Random();

    public String getImage() {
        return image;
    }

    public int getHp() {
        return hp;
    }
    
    public int getMp() {
        return mp;
    }

    public int getStr() {
        return str;
    }
    
    public int getDef() {
        return def;
    }
    
    public int getSpeed() {
        return speed;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public void setMp(int mp) {
        this.mp = mp;
    }

    public void setStr(int str) {
        this.str = str;
    }
    
    public void setDef(int def) {
        this.def = def;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public int getMax_mp() {
        return max_mp;
    }

    public int getNumber_of_skills() {
        return number_of_skills;
    }
    
    protected void addSpeedBuff(){
        speed_up = true;
        speed_buff = (lvl + 6) + speed_enhance;
        speed += speed_buff;
    }
    
    protected void addDefenseBuff(){
        defense_up = true;
        defense_buff = (lvl + 4) + defense_enhance;
        def += defense_buff;
    }
    
    protected void addStrengthBuff(){
        strength_up = true;
        strength_buff = (lvl + 4) + strength_enhance;
        str += strength_buff;
    }
    
    protected void addSlowHeal(){
        slow_heal_up = true;
        slow_heal_duration = 5;
        slow_heal_supplied = slow_heal_buff; //I'm keeping this here because this value may differ at some point between classes
    }
    
    public void deBuffGradually(){
        if((defense_buff - defense_enhance) > 0 && defense_up){
            def--;
            defense_buff--;
            if((defense_buff - defense_enhance) <= 0) defense_up = false;
        }
        
        if((strength_buff - strength_enhance) > 0 && strength_up){
            str--;
            strength_buff--;
            if((strength_buff - strength_enhance) <= 0) strength_up = false;
        }
        
        if((speed_buff - speed_enhance) > 0 && speed_up){
            speed--;
            speed_buff--;
            if((speed_buff - speed_enhance) <= 0) speed_up = false;
        }
        
        if(slow_heal_up){
            if(hp < max_hp){
                hp += slow_heal_supplied;
                if(hp > max_hp){
                    hp = max_hp;
                }
            }
            slow_heal_duration--;
            if(slow_heal_duration <= 0){
                slow_heal_up = false;
            }
        }
    }
    
    public void deBuff(){
        defense_up = false;
        def -= defense_buff;
        defense_buff = 0;
        
        speed_up = false;
        speed -= speed_buff;
        speed_buff = 0;
        
        strength_up = false;
        str -= strength_buff;
        strength_buff = 0;
        
        slow_heal_up = false;
        slow_heal_duration = 0;
    }
    
    public int testChance(int skill, int enemy_def){
        int recursion_stopper = 0;
        
        int damage = skills(skill,enemy_def,false);
        if(damage != 0 && damage != -7 && damage != -1){ //checks every non-attack skill and no mana situations (order: no mana, status effect can't be used, status effect already used)
            return damage;
        }else if(damage == 0 || damage == -7){
            int chance = rand.nextInt(number_of_skills) + 1;
            testChance(chance,enemy_def,++recursion_stopper);
        }else if(damage == -1){
            return -1; //reference for when effect is applied
        }      
        return -2; //it will never return this
    }
    
    private int testChance(int skill, int enemy_def, int recursion_stopper){
        int recursion_limit = recursion_stopper;
        if(recursion_limit >= 15){ //temporary solution to stop infinite recursion when mana is always insufficient
            return -2;
        }
        
        int damage = skills(skill,enemy_def,false);
        if(damage != 0 && damage != -7 && damage != -1){ //checks every non-attack skill and no mana situations (order: no mana, status effect can't be used, status effect already used)
            return damage;
        }else if(damage == 0 || damage == -7){
            int chance = rand.nextInt(number_of_skills) + 1;
            testChance(chance,enemy_def,++recursion_limit);
        }else if(damage == -1){
            return -1; //reference for when effect is applied
        }      
        return -2; //it will never return this
    }
    
    public abstract void imprimir();
    public abstract int attack();
    public abstract int skills(int choice, int enemy_def, boolean player); 
}


