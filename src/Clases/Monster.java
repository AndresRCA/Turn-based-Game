package Clases;

public class Monster extends Personaje {
    
    private int id;
    
    public Monster(String image, int id, int lvl, int max_hp, int max_mp, int str, int magic, int def, int number_of_skills){
        this.image = image;
        this.id = id;
        this.lvl = lvl;
        this.max_hp = max_hp;
        this.max_mp = max_mp;
        hp = max_hp;
        mp = max_mp;
        this.str = str;
        this.magic = magic;
        this.def = def;
        exp = 34 + 13*((lvl * 3)/2);
        this.number_of_skills = number_of_skills;
    }
    
    public int getId(){
        return id;
    }
    
    @Override
    public int attack(){
        if(lvl <= 2) return str + 3;
        else if(lvl > 2 && lvl < 6) return str + 5;
        else return str + 7; //meanwhile, this will stay like this
    }
    
    @Override
    public int skills(int choice, int enemy_def, boolean key){
        if(image.equals(Assets.Boss)){
            if(choice  == 1){
                return reality(); //returns damage
            }else return -4; //this could be impossible, for now I control the input range of the AI
        }else return -2; //normal attack in testChance    
    }
    
    public int reality(){
        if(mp < 13){
            return 0;
        }
        else{
            mp = (mp - 13 <= 0)? 0 : mp - 13;
            return magic + 10;
        }    
    }
    
    public boolean magicAttack(){ //my boolean answer for can he use a skill? (I wish the min mp required wouldn't depend on a value given by the programmer because he knows the minimum required)
        return (mp >= 13);
    }
    
    @Override
    public void imprimir(){
        System.out.println("Monster stats: HP: "+hp+"/"+max_hp+"         Strenght: "+str);
        System.out.println("Lvl "+lvl+"          MP: "+mp+"/"+max_mp+"         Magic: "+magic+"\n");
    }
}
