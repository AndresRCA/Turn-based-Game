package Clases;

public abstract class Player extends Personaje implements Mechanics{
    
    protected int max_exp;
    
    public void hpPotion(){
        System.out.println("+5 hp recovered");
        hp = (hp + 5 >= max_hp)? max_hp : hp + 5;
    }
   
    public void mpPotion(){
        System.out.println("+5 mp recovered");
        mp = (mp + 5 >= max_mp)? max_mp : mp + 5;
    }
    
    public int newMaxExp(){
        return max_exp*3/2;
    }

    public int getMax_exp() {
        return max_exp;
    }
    
    @Override
    public void imprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int attack() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int skills(int choice, int enemy_def, boolean player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void lvlUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printSkills() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected abstract int howManySkills();
}
