package ltd.rymc.bedrock.residence.forms.select;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.selection.SelectionManager;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceManualSelectForm extends RCustomForm {
    private static final SelectionManager selectionManager = Residence.getInstance().getSelectionManager();
    public ResidenceManualSelectForm(Player player, RForm previousForm, Language language) {
        super(player, previousForm, language);

        Language.Section createManual = section("forms.create.manual");

        title(createManual.text("title"));
        input(createManual.text("input1"), createManual.text("input2"));
        input(createManual.text("input3"), createManual.text("input4"));
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input1 = response.asInput(0);
        String input2 = response.asInput(1);

        if (input1 == null || input2 == null) {
            sendPrevious();
            return;
        }
        Location loc1 = stringToBlockLoc(input1), loc2 = stringToBlockLoc(input2);

        if (loc1 == null || loc2 == null) {
            sendPrevious();
            return;
        }

        selectionManager.clearSelection(bukkitPlayer);
        selectionManager.getSelection(bukkitPlayer).setBaseLoc1(loc1);
        selectionManager.getSelection(bukkitPlayer).setBaseLoc2(loc2);
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }

    private Location stringToBlockLoc(String string) {
        String[] args = string.replace(" ", "").split(",");
        try {
            return new Location(bukkitPlayer.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
