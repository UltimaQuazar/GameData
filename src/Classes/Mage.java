package Classes;
import combat_usage.*;

/**
 * Created by Parzival on 4/19/2016.
 */

public class Mage extends SpellCaster implements Base_Stat{
    public SpellCaster me = new SpellCaster();
    @Override
    public void set_stat() {
        strength = rng.rng_maker(16, 6);
        dex = rng.rng_maker(14, 6);
        con = rng.rng_maker(14, 8);
        wisdom = rng.rng_maker(18, 6);
        charm = rng.rng_maker(16,6);
        intel = rng.rng_maker(20,10);
    }

    @Override
    public int get_dmg(String weapon) {
        boolean sp;
        int i;
        for(i = 0; i < me.book.getNumSpells(); i++) {
            if (weapon == book.getSpell(i).getType()){
                sp = true;
                break;
            }
        }
        /*if(sp = true){

        }*/
        return 0;
    }

    @Override
    public int get_dodge() {
        return 0;
    }

    @Override
    public int get_gud() {
        return 0;
    }
}
