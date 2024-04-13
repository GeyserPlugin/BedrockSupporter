package ltd.rymc.bedrock.residence.forms.setting.sensitive;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.utils.InputUtils;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceNoPermissionForm;
import ltd.rymc.bedrock.residence.utils.facing.Facing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceExpandAndContractForm extends RCustomForm {
    private final ClaimedResidence claimedResidence;
    public ResidenceExpandAndContractForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!claimedResidence.isOwner(player) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }

        Language.Section sensitiveExpand = section("forms.manage.sensitive.expand");

        title(sensitiveExpand.text("title"));
        dropdown(String.format(sensitiveExpand.text("dropdown"), Facing.facing(player.getLocation().getYaw()).getName()), Facing.facingList());
        input(sensitiveExpand.text("input1"), sensitiveExpand.text("input2"));
        toggle(sensitiveExpand.text("toggle"));
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(1);

        if (!InputUtils.checkInput(input) || input.trim().contains(" ")) {
            sendPrevious();
            return;
        }

        String command = response.asToggle(2) ? "contract" : "expand";
        Location location = bukkitPlayer.getLocation();
        bukkitPlayer.teleport(Facing.translateLocation(location, Facing.facing(response.asDropdown(0))));
        Bukkit.dispatchCommand(bukkitPlayer, "res " + command + " " + claimedResidence.getName() + " " + input.trim());
        bukkitPlayer.teleport(location);

        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
