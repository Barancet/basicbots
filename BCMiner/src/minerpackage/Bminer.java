package minerpackage;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(author = "baran", category = Category.MINING, description = "Iron Mining", name = "BCMining", version = 1.0)
public class Bminer extends AbstractScript {
    private int state;
    private String ore;
    private GameObject rock;
    private int count;
    
    @Override
    public void onStart() {
        ore = "Tin ore";
        count = 0;
        state = 0;
    }

    @Override
    public int onLoop() {
        if (state == 0) {
            mine();
        } else if (state == 1) {
            drop();
        }
        return Calculations.random(2000, 3000);
    }

    @Override
    public void onExit() {
        super.onExit();
    }

//    public void mineTin() {
//        if (rock.getID() == (438)) {
//            mine();
//        }
//
//    }
    private void mine() {
        if (!getInventory().isFull()) {
            rock = getGameObjects().closest(f -> f.getName().contains("Rock"));
            if (rock != null) {
                if (rock.interact("Mine")) {
                    sleepUntil(() -> getInventory().count(ore) > count, Calculations.random(4000, 5000));
                    if (getInventory().count(ore) > count) {
                        count++;
                    }
                }
            }
        } else {
            state = 1;
        }
    }

    private void drop() {
        if (getInventory().contains(ore)) {
            getInventory().dropAllExcept(i -> i.getName().contains("pickaxe"));
        }
        else{
            state = 0;
        }
    }
}

