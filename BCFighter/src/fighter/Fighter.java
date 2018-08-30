package fighter;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "baran", category = Category.COMBAT, description = "GoblinSuicider", name = "BCFighter", version = 1.0)

public class Fighter extends AbstractScript {

    private Area killArea;
    private int state;

    public static final String SWORD = "Iron scimitar";
    public static final String STR_AMMY = "Amulet of strength";
    public static final String GOBLIN = "Goblin";
    public static final Filter<NPC> GOBLIN_FILTER = npc -> npc != null && npc.getName().equals(GOBLIN) && !npc.isHealthBarVisible();


    public Fighter() {
        state = 0;
        killArea = new Area(3243, 3241, 3259, 3231);
    }

    public void onStart() {


    }

    @Override
    public int onLoop() {

        if (state == 0) {
            attackGoblin();
        } else if (state == 1) {
            runToGoblins();
        }
        return Calculations.random(4000, 6000);
    }


    public void attackGoblin() {
        if (getLocalPlayer().isInCombat()) {
            //do nothing

        } else if (killArea.contains(getLocalPlayer())) {
            if (getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot()) && getEquipment().isSlotEmpty(EquipmentSlot.AMULET.getSlot())) {
                if (getInventory().contains(SWORD) && getInventory().contains(STR_AMMY)) {
                    getInventory().interact(SWORD, "Wield");
                    getInventory().interact(STR_AMMY, "Wear");
                } else {
                    stop();
                }
            } else {
                NPC goblin = getNpcs().closest(GOBLIN_FILTER);
                if (goblin != null) {
                    goblin.interact("Attack");
                }
            }
        } else {
            getWalking().walk(killArea.getRandomTile());
        }
    }

    public void runToGoblins() {

    }
}
