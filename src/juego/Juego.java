package juego;

//Important notes:
//*For enemy AI return values: 0 means no mana, -1 means stat buff used, -2 means the enemy ran out of mana for every skill,
//so it will use a physical attack,-7 means stat buff can't be used yet
//*For User: 0 means no mana, -1 means stat buff used (or healing effect), -2 means stat buff can't be used but wastes a turn nonetheless,
//-4 means wrong choice by user
//
//*I want interfaces for skill sets but I don't want to make the methods public, interfaces are on standby. 

//Things to add later:
//1. A back button when choosing to attack or use a skill
//2. Implementation of the skill speed
//3. An exp multiplier, for both gaining exp and assigning exp to monsters corresponding their lvl

//Small notes, seriously, fuck Timer and TimerTask, Thread. solves my problem and only requires one line of code

import Clases.Assets;
import Clases.Events;
import Clases.Hero;
import Clases.Mage;
import Clases.Monster;
import Clases.Personaje;
import Clases.Player;
import Clases.Thief;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Juego {
    
    /**
     * A function that must be used when you enter a battle.
     * @param andrew A character, can be a Hero, Mage or Thief.
     * <br>
     * @param monster A monster to battle with, its constructor consists of:
     * <ol>
     *      <li><b style="color:#42a0ce">Id</b></li>
     *      <li><b style="color:#42a0ce">Level</b></li>
     *      <li><b style="color:#42a0ce">Max HP</b></li>
     *      <li><b style="color:#42a0ce">Max MP</b></li>
     *      <li><b style="color:#42a0ce">Strength</b></li>
     *      <li><b style="color:#42a0ce">Magic</b></li>
     *      <li><b style="color:#42a0ce">Defense</b></li>
     * </ol>
     */
    private static void battlePhase(Player andrew, Personaje monster){
        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        
        int option, damage;
        System.out.println("You encounter an enemy!");
        while(monster.getHp() > 0 && andrew.getHp() > 0){
            monster.imprimir();
            System.out.println(monster.getImage());
            
            andrew.imprimir();
            andrew.deBuffGradually();
            
            System.out.println("1.Hit           2.Use health potion");
            System.out.println("3.Skills        4.Use mp potion");
            option = input.nextInt();
            
            System.out.println("Your Turn:");
            switch(option){
                case 1:
                    if(rand.nextInt(3) != 0){
                        if((andrew.attack() - monster.getDef()) >= 0){
                            System.out.println("You dealt "+(andrew.attack() - monster.getDef())+" damage!");
                            monster.setHp(monster.getHp() - (andrew.attack() - monster.getDef()));
                        }    
                    }
                    else System.out.println("Your attack missed!");
                    break;
                    
                case 2:
                    andrew.hpPotion();
                    break;
                    
                case 3:
                    andrew.printSkills();
                    option = input.nextInt();
                    
                    damage = andrew.skills(option, monster.getDef(), true);
                    
                    if(damage == -4){
                        System.out.println("You entered a wrong number");
                    }else if(damage == 0){
                        System.out.println("Not enough mana.");
                    }else if(damage != -1 && damage != -2){
                        System.out.println("You dealt "+damage+" special damage!");
                        monster.setHp(monster.getHp() - damage);
                    }
                    break;
                    
                case 4:
                    andrew.mpPotion();
            }
            /*---------------Monster turn------------------*/
            if(monster.getHp() > 0){
                System.out.println("Monster Turn: ");
                switch(rand.nextInt(3)){
                    case 0:
                        System.out.println("The monster missed his attack, Now's your chance!");
                        break;
                    
                    case 1:
                        if((monster.attack() - andrew.getDef()) < 0){
                            System.out.println("The monster dealt 0 damage!");
                        }else{
                            andrew.setHp(andrew.getHp() - (monster.attack() - andrew.getDef()));
                            System.out.println("The monster dealt "+(monster.attack() - andrew.getDef())+" damage!");
                        }    
                        break;
                        
                    case 2:
                        if(monster.getNumber_of_skills() > 0){
                            int monster_choice = rand.nextInt(monster.getNumber_of_skills()) + 1;

                            damage = monster.testChance(monster_choice,andrew.getDef());
                            if(damage != -1){
                                if(damage == -2){
                                    if((monster.attack() - andrew.getDef()) < 0){
                                        System.out.println("The monster dealt 0 damage!");
                                    }else{
                                        andrew.setHp(andrew.getHp() - (monster.attack() - andrew.getDef()));
                                        System.out.println("The monster dealt "+(monster.attack() - andrew.getDef())+" damage!");
                                    }
                                }else{
                                    if(damage <= 0){
                                        System.out.println("The monster dealt 0 special damage!");
                                    }else{
                                        andrew.setHp(andrew.getHp() - damage);
                                        System.out.println("The monster dealt "+damage+" special damage!");
                                    }
                                }
                            }
                        }else{
                            if((monster.attack() - andrew.getDef()) <= 0){
                                System.out.println("The monster dealt 0 damage!");
                            }else{
                                andrew.setHp(andrew.getHp() - (monster.attack() - andrew.getDef()));
                                System.out.println("The monster dealt "+(monster.attack() - andrew.getDef())+" damage!");
                            }
                        }     
                }
            }
            /*-----------------------------------------------*/
            else{
                System.out.println("You have killed the monster!\n");
                andrew.deBuff();
                
                if((andrew.getExp() + monster.getExp()) >= andrew.getMax_exp()){
                    int last_exp_capacity = andrew.getMax_exp();
                    
                    System.out.println("Exp: "+andrew.getExp()+" (+"+monster.getExp()+")");
                    andrew.setExp(andrew.getExp() + monster.getExp());
                    //Should I ditch the current exp: *exp/max_exp* and just leave it as current exp: *exp*?
                    System.out.println("Current Exp: "+(andrew.getExp() - last_exp_capacity)+"/"+(last_exp_capacity + andrew.newMaxExp())+"\n");
                    
                    andrew.lvlUp();
                    
                    andrew.setExp(andrew.getExp() - last_exp_capacity);
                    System.out.println("Next to level up: "+(andrew.getMax_exp() - andrew.getExp())+"\n");
                }else{
                    System.out.println("Exp: "+andrew.getExp()+" (+"+monster.getExp()+")");                   
                    andrew.setExp(andrew.getExp() + monster.getExp());
                    //Should I ditch the current exp: *exp/max_exp* and just leave it as current exp: *exp*?
                    System.out.println("Current Exp: "+andrew.getExp()+"/"+andrew.getMax_exp());
                    System.out.println("Next to level up: "+(andrew.getMax_exp() - andrew.getExp())+"\n");
                }
            }            
        } 
    }
        
    /**
     * A function that controls the aspect of in-game dialogue. This dialogue has a beginning and an end that 
     * must be specified.
     * @param dialogue The string array containing the game dialogue.
     * <br>
     * @param start The index used for the start of the dialogue, dialogue[start].
     * <br>
     * @param end The index for the end of the dialogue, dialogue[end-1].
     * <br>
     * @throws InterruptedException 
     */
    private static void dialogue(String[] dialogue, int start, int end) throws InterruptedException{
        Scanner input = new Scanner(System.in);
        for(int i = start; i < end; i++){                                       
            String[] characters = dialogue[i].split("");
            for(int j = 0; j < characters.length; j++){
                if(".".equals(characters[j]) && j != characters.length - 1){ //it gets slower when it gets to dots, but not if it's the last one
                    System.out.print(characters[j]);
                    Thread.sleep(650);
                }else{
                    System.out.print(characters[j]);
                    Thread.sleep(0); //If you want to get the typing effect, set it to 270, for no effect set it to 0. 
                }
            }
            System.out.print(" ►");
            input.nextLine();                    
        }
    }
    
    /**
     * A function that contains every possible event happening inside the game, such as random enemy encounters, dialogue, 
     * etc.
     * @param andrew A character.
     * <br>
     * @param area A 2D array containing the images(<i>strings</i>) used for the areas in the game.
     * <br>
     * @param row The row in area[][], area[<i>row</i>][column].
     * <br>
     * @param column The column in area[][], area[row][<i>column</i>].
     * <br>
     * @param dialogue The string array containing the game dialogue.
     * <br>
     * @throws InterruptedException 
     */
    public static void events(Player andrew, String area[][], int row, int column, String dialogue[]) throws InterruptedException{
        Random rand = new Random();
        
        if(area[row][column].equals(Assets.Capital) && Events.Capital_Dialogue){
            Events.Capital_Dialogue = false;                 
            dialogue(dialogue,0,2);
        }
            
        //Random generated encounter
        if(area[row][column].equals(Assets.Forest) && Events.ready_to_fight/*just for now && (rand.nextInt(2) == 1)*/){
            battlePhase(andrew,new Monster(Assets.Bug, 0, rand.nextInt(2)+1, 20, 0, 3, 0, 1, 0));
            System.out.println(area[row][column]); //this is to show the map after the battle is over, instead of just the options
        }
        //Random generated encounter
        if(area[row][column].equals(Assets.Dead_Tree) && (Events.Dead_Tree_Dialogue == false) && (Events.Last_Boss_Fight == false) && Events.ready_to_fight && (rand.nextInt(2) == 1)){
            battlePhase(andrew,new Monster(Assets.Head, 1, rand.nextInt(5)+3, 27, 0, 7, 0, 3, 0));
            System.out.println(area[row][column]);
        }
            
        if(area[row][column].equals(Assets.Dead_Tree) && Events.Dead_Tree_Dialogue){
            Events.Dead_Tree_Dialogue = false;
            Events.Return_to_Capital = true;
                
            dialogue(dialogue,2,5);
                
            //Dead_Tree fight here
            battlePhase(andrew,new Monster(Assets.Head, 1, 4, 27, 0, 7, 0, 3, 0));
            System.out.println(area[row][column]);
                
            dialogue(dialogue,5,8);
        }
            
        if(area[row][column].equals(Assets.Capital) && Events.Return_to_Capital){
            Events.Return_to_Capital = false;
            /*There used to be a puzzle here but it's very stupid so I don't think I'll add it*/
            /*Scanner input = new Scanner(System.in);
            boolean puzzle_not_solved = true;
            String options;
                
            dialogue(dialogue,8,12);
               
            while(puzzle_not_solved){
                boolean first = false, second = false, third = false;
                System.out.println("Enter your keys (separate them with spaces)");
                options = input.nextLine();
                String[] keys;
                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(options);
                if(matcher.find()){
                    keys = options.split(" ");
                        
                    for(int i = 0; i < keys.length; i++){
                        if(keys[i] != null){
                            switch (keys[i]){
                                case "1":
                                    first = true;
                                    break;
                                case "2":
                                    second = true;
                                    break;
                                case "3":
                                    third = true;
                                    break;
                            }
                        }    
                    }
                    
                    puzzle(first,second,third); 
                    dialogue(dialogue,12,13);
                    System.out.println("Enter your answer (separate with spaces)");
                    options = input.nextLine();
                    String[] answer;
                    matcher = pattern.matcher(options);
                        
                    if(matcher.find()){
                        answer = options.split(" ");
                        if(answer.length == 4){
                            if("star".equals(answer[0]) && "moon".equals(answer[1]) && "sun".equals(answer[2]) && "dust".equals(answer[3])){
                                puzzle_not_solved = false;
                            }else System.out.println("You may have entered the wrong order, try again.");
                        }else System.out.println("You either answered with too much words or too few.");
                    }else{System.out.println("Your answer doesn't contain spaces.");}
                        
                }else{System.out.println("Wrong pick, the keys contains spaces.");}
            }*/
            Events.Last_Boss_Fight = true;
            System.out.println(area[row][column]);
            System.out.println("There used to be a puzzle here but now there isn't, anyway head to the Dead Tree.");
        }
        
        if(area[row][column].equals(Assets.Dead_Tree) && Events.Last_Boss_Fight){
            Events.Last_Boss_Fight = false;
            //Enemy object here, I'll make it a thief
            Thief boss = new Thief(54,30,10,5,11,4,15);
            battlePhase(andrew,boss);
            System.out.println(area[row][column]);
        }
    }
    
    //Answer: star, moon, sun, dust.
    public static void puzzle(boolean first, boolean second, boolean third) throws InterruptedException{
        if(first && second){
            if(third){
                System.out.println("When the sun sets, all that remains is dust.");
            }else{
                System.out.println("Dust marks the end of your journey.");
            }
        }else System.out.println("Hint 1: this puzzle contains 4 words as the answer");
        
        if(first && third){
            if(second){
                System.out.println("The moon comes before the sun.");
            }else{
                System.out.println("The sun comes after the star.");
            }
        }else System.out.println("Hint 2: the answers to this puzzle are about space");
        
        if(second && third){
            if(first){
                System.out.println("The stars are the beginning of all.");
            }else{
                System.out.println("Solution (sshh don't tell anyone): star moon sun dust");
            }
        }else System.out.println("Hint 3: think about the order of things.");
        
        System.out.println("");
    }
    
    public static void showMap(String map[][], int row, int column){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                map[i][j] = "☐";
            }
        }
        map[row][column] = "■";
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) throws InterruptedException{        
        int option;
        int row = 0, column = 0;
        String movement;
        Scanner input = new Scanner(System.in);
        
        String[] dialogue = new String[13];
        //Capital
        dialogue[0] = "I'm finally leaving the capital... I wonder what awaits me";
        dialogue[1] = "hopefully not death.";
        //Dead Tree
        dialogue[2] = "This place doesn't look too good...";
        dialogue[3] = "I should probably return to the capital and inform the people of the dangers coming from the forest";
        dialogue[4] = "wait... what is that..?";
        //After Dead Tree fight
        dialogue[5] = "What the fuck was that!? was that a head??";
        dialogue[6] = "I really, reaaally have to get out of this place";
        dialogue[7] = "...I regret leaving home...";
        //Return to the Capital
        dialogue[8] = "Before you stands a great challenge, you must test your mind...";
        dialogue[9] = "what will be your answer I wonder... We'll soon see.";
        dialogue[10] = "Before you lies 3 keys, one of them is labeled as 1 the other two are labeleded respectively as 2 and 3";
        dialogue[11] = "enter which ones you want to pick";
        dialogue[12] = "So, what is your answer?";
        
        String[][] map = new String[2][2];
        String[][] area = new String[2][2];
        area[0][0] = Assets.Capital;
        area[0][1] = Assets.Forest;
        area[1][0] = Assets.Grim_Reaper; 
        area[1][1] = Assets.Dead_Tree;
        
        Player andrew;    
        System.out.println("Choose your class:");
        System.out.println("1.Hero\n2.Mage\n3.Thief");
        option = input.nextInt();
        switch (option) {
            case 1:
                andrew = new Hero();
                break;
            case 2:
                andrew = new Mage();
                break;
            case 3:
                andrew = new Thief();
                break;
            default:
                andrew = new Hero();
                System.out.println("You'll be a Hero");
                break;
        }
        input.nextLine(); //To clean the buffer...?

        //This is a test loop for navigation
        while(andrew.getHp() > 0){
            System.out.println(area[row][column]);
            
/*------------Events that occur after you arrive somewhere------------------------------------------*/
            
            events(andrew,area,row,column,dialogue);
            
            //Conditions for death, if I do them here the player won't get a chance to get to the menu
            if(area[row][column].equals(Assets.Grim_Reaper)){
                break;
            }
            if(andrew.getHp() <= 0){
                System.out.println("You died.");
                break;
            }

            Events.ready_to_fight = true; //the only way a fight will happen is if you enter an area, it doesn't happen if you check the map or mess up your movement

/*---------------------------------------------------------------------------------------------*/  
           
            System.out.println("1.Show map                 2.Move");
            System.out.println("3.Check stats              4.Drink health potion");
            System.out.println("5.Drink magic potion");
            option = input.nextInt();
            if(option != 2) Events.ready_to_fight = false;
            switch(option){
                case 1:
                    input.nextLine();
                    showMap(map,row,column);
                    System.out.print("...►");
                    input.nextLine();
                    break;
                
                case 2:
                    input.nextLine();
                    System.out.println("move left, right, up or down by typing it");
                    movement = input.nextLine();
                    if("left".equals(movement) && column > 0){column -= 1;}
                    else if("right".equals(movement) && column < 1){column += 1;} //I could change 2 to the length, I have to test it
                    else if("up".equals(movement) && row > 0){row -= 1;}
                    else if("down".equals(movement) && row < 1){row += 1;} //I could change 2 to the length, I have to test it
                    else{
                        System.out.println("You can't move in that direction");
                        Events.ready_to_fight = false;
                    }
                    break;
                    
                case 3:
                    input.nextLine();
                    System.out.println("Andrew:");
                    System.out.println("HP: "+andrew.getHp()+"/"+andrew.getMax_hp());
                    System.out.println("MP: "+andrew.getMp()+"/"+andrew.getMax_mp());
                    System.out.println("Lvl "+andrew.getLvl()+", Next to level up: "+(andrew.getMax_exp() - andrew.getExp())+"\n");
                    System.out.println("...►");
                    input.nextLine();
                    
                    break;
                    
                case 4:
                    input.nextLine();
                    andrew.hpPotion();
                    System.out.println("HP: "+andrew.getHp()+"/"+andrew.getMax_hp());
                    System.out.println("MP: "+andrew.getMp()+"/"+andrew.getMax_mp());
                    System.out.println("...►");
                    input.nextLine();
                    break;
                    
                case 5:
                    input.nextLine();
                    andrew.mpPotion();
                    System.out.println("HP: "+andrew.getHp()+"/"+andrew.getMax_hp());
                    System.out.println("MP: "+andrew.getMp()+"/"+andrew.getMax_mp());
                    System.out.println("...►");
                    input.nextLine();
                    break;
                                       
                default: System.out.println("That's not an option.");    
            }
        }
    }
    
}