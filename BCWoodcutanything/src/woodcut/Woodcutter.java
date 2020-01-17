package woodcut;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import util.Wood;

@ScriptManifest(author = "baran", category = Category.WOODCUTTING, description = "Pick Tree Cutter", name = "BCWoodcut", version = 1.0)
public class Woodcutter extends AbstractScript {
    private int state = -1;
    private Wood currentLog;
    private boolean drop = false;
    private Area safeArea;
    GameObject gO;
    private Tile t;

    public Woodcutter(){
        t = new Tile(3200, 3244);
    }
    @Override
    public int onLoop() {
        if (state == 0) {
            cut();
        } else if (state == 1) {
            bank();
        } else if (state == 2) {
            drop();
        }
        return Calculations.random(2000, 3700);
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onStart() {
        super.onStart();
        state = 0;
        safeArea = new Area(3201, 3250, 3208, 3238);
        currentLog = Wood.NORMAL;
    }
    
    private void cut() {
//        if(getLocalPlayer().isInCombat()){
//            getWalking().walk(safeArea.getRandomTile());
//        }
        if (!getInventory().isFull()) {
            gO = getGameObjects().closest(f -> f.getName().equals(currentLog.getTreeName()));
            if (getLocalPlayer().distance(gO) > 5) {
                getWalking().walk(gO);
                sleepUntil(() -> getLocalPlayer().isMoving()
                        || getLocalPlayer().distance(getClient().getDestination()) < 5, Calculations.random(2200, 3500));
            } else {
                if (!getLocalPlayer().isAnimating()) {

                    if (gO.interact("Chop down")) {
                        sleepUntil(() -> !gO.exists() || !getLocalPlayer().isAnimating(), Calculations.random(10000, 15000));
                    }
                }
            }

        } else {
            if (drop) {
                state = 2;
            } else {
                state = 1;
            }
        }
    }

    private void bank() {
        if (!getBank().isOpen() && getInventory().isFull()) {
            if (getLocalPlayer().distance(getBank().getClosestBankLocation().getCenter()) > 5) {
                if (getWalking().walk(getBank().getClosestBankLocation().getCenter())) {
                    sleepUntil(() -> !getLocalPlayer().isMoving() ||
                                    getLocalPlayer().distance(getClient().getDestination()) < 8,
                            Calculations.random(2300, 3000));
                    getBank().open();

                    getBank().depositAllExcept(f -> f.getName().contains("axe"));
                    getBank().close();
                    sleepUntil(() -> !getBank().isOpen(), Calculations.random(2000, 2900));
                }
            }
        }
        else{
            state = 0;
        }
    }

    private void drop() {
        if (getInventory().contains(currentLog.getLogName())) {
            getInventory().dropAll(currentLog.getLogName());
        } else {
            state = 0;
        }
    }
}
