package ltd.rymc.bedrock.residence.forms.setting.sensitive;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceNoPermissionForm;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceSensitiveOperationForm extends RSimpleForm {
    private final ClaimedResidence claimedResidence;
    public ResidenceSensitiveOperationForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!claimedResidence.isOwner(player) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }

        Language.Section sensitiveMain = section("forms.manage.sensitive.main");
        Language.Section sensitiveMainButtons = sensitiveMain.section("buttons");

        title(sensitiveMain.text("title"));
        content(String.format(sensitiveMain.text("content"), claimedResidence.getName()));
        buttons(
                sensitiveMainButtons.text("rename"),
                sensitiveMainButtons.text("expand"),
                sensitiveMainButtons.text("give"),
                sensitiveMainButtons.text("remove")
        );
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        int id = response.clickedButtonId();
        if (id == 0) new ResidenceRenameForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 1) new ResidenceExpandAndContractForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 2) new ResidenceGiveForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 3) new ResidenceRemoveForm(bukkitPlayer,this, claimedResidence, language).send();
    }

    @Override
    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response) {
        sendPrevious();
    }
}
