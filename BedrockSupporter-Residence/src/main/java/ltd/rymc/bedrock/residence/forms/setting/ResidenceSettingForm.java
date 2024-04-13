package ltd.rymc.bedrock.residence.forms.setting;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.forms.setting.sensitive.ResidenceSensitiveOperationForm;
import ltd.rymc.bedrock.residence.forms.setting.set.ResidencePlayerSetSelectForm;
import ltd.rymc.bedrock.residence.forms.setting.set.ResidenceSetForm;
import ltd.rymc.bedrock.residence.forms.setting.set.ResidenceTeleportSetForm;
import ltd.rymc.bedrock.residence.forms.setting.trust.ResidenceTrustedPlayerSettingForm;
import ltd.rymc.bedrock.residence.utils.ResidenceUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceSettingForm extends RSimpleForm {

    private final ClaimedResidence claimedResidence;

    public ResidenceSettingForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!ResidenceUtils.hasManagePermission(player, claimedResidence) && !player.isOp()) {
            new ResidenceNoPermissionForm(player,previousForm, language).send();
            return;
        }

        Language.Section manageMain = section("forms.manage.main");
        Language.Section buttons = manageMain.section("buttons");

        title(manageMain.text("title"));
        buttons(
                buttons.text("set"),
                buttons.text("player-set"),
                buttons.text("trusted-player"),
                buttons.text("teleport-set"),
                buttons.text("kick"),
                buttons.text("sensitive")
        );
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        int id = response.clickedButtonId();
        if (id == 0) new ResidenceSetForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 1) new ResidencePlayerSetSelectForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 2) new ResidenceTrustedPlayerSettingForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 3) new ResidenceTeleportSetForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 4) new ResidenceKickForm(bukkitPlayer,this, claimedResidence, language).send();
        else if (id == 5) new ResidenceSensitiveOperationForm(bukkitPlayer,this, claimedResidence, language).send();
    }

    @Override
    public void refresh() {
        content(String.format(text("forms.manage.main.content"), claimedResidence.getName()));
    }

    @Override
    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response) {
        sendPrevious();
    }
}
