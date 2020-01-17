package woodcutter;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(category = Category.WOODCUTTING, name = "BCOakcutter" , author = "Baran", version = 1.0 )
public class OakCutter extends AbstractScript {
    private int state;
    private Area oakArea;
    GameObject oakObject;
    private Tile t;
    
    public OakCutter() {
        state = 0;
        oakArea = new Area(3103, 3246, 3100,3241);
        t = new Tile(3102, 3242);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public int onLoop() {
        if (state == 0) {
            cut();
        } else if (state == 1) {
            bank();
        }
        return Calculations.random(2000,3400);
    }

    public void cut() {
        if(!getLocalPlayer().isAnimating()) {
            if(getInventory().isFull()){
                state = 1;
            }
            oakObject = getGameObjects().getTopObjectOnTile(t);
            if (oakObject != null) {
                oakObject.interact("Chop down");
            }
        }
    }

    public void bank() {
        if (!getBank().isOpen()) {
            if(getLocalPlayer().distance(getBank().getClosestBankLocation().getCenter()) > 5){
                if(getWalking().walk(getBank().getClosestBankLocation().getCenter())){
                    sleepUntil(()-> !getLocalPlayer().isMoving() ||
                                    getLocalPlayer().distance(getClient().getDestination()) < 8,
                            Calculations.random(3300,5100));
                    getBank().open();
                    getBank().depositAllExcept(f -> f.getName().contains("axe"));
                    getBank().close();
                    sleepUntil(() -> !getBank().isOpen(), Calculations.random(2000, 2900));
                    state = 0;
                }
            }
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }
}
