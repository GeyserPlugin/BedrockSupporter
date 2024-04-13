package ltd.rymc.bedrock.residence.forms.setting.sensitive;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.utils.InputUtils;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceNoPermissionForm;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceRenameForm extends RCustomForm {
    private final ClaimedResidence claimedResidence;
    public ResidenceRenameForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!claimedResidence.isOwner(player) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }

        Language.Section sensitiveRename = section("forms.manage.sensitive.rename");

        title(String.format(sensitiveRename.text("title"), claimedResidence.getName()));
        input(sensitiveRename.text("input1"), sensitiveRename.text("input2"));
        input(sensitiveRename.text("input3"), sensitiveRename.text("input4"));
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(0);
        String input1 = response.asInput(1);

        if (InputUtils.checkInput(input, input1)){
            sendPrevious();
            return;
        }

        Residence.getInstance().getResidenceManager().renameResidence(bukkitPlayer, claimedResidence.getName(), input.trim(), false);
        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
