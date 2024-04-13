package ltd.rymc.bedrock.residence.forms.setting.trust;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.residence.forms.setting.ResidenceNoPermissionForm;
import ltd.rymc.bedrock.residence.utils.ResidenceUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

import java.util.List;

public class ResidenceTrustedPlayerSettingForm extends RSimpleForm {

    private final ClaimedResidence claimedResidence;

    public ResidenceTrustedPlayerSettingForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!ResidenceUtils.hasManagePermission(player, claimedResidence) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }


        Language.Section trustedPlayerMain = section("forms.manage.trusted-player.main");

        title(trustedPlayerMain.text("title"));
        button(trustedPlayerMain.text("button"));
    }

    private String generateTrustedPlayerList(){
        StringBuilder stringBuilder = new StringBuilder();
        List<String> trustedPlayers = ResidenceUtils.getResTrustedPlayerString(claimedResidence);
        for (int i = 0, trustedPlayersSize = trustedPlayers.size(); i < trustedPlayersSize; i++) {
            String playerName = trustedPlayers.get(i);
            stringBuilder.append(playerName).append(i == trustedPlayersSize - 1 ? "" : ", ");
        }
        return stringBuilder.toString();
    }

    @Override
    public void refresh() {
        String content = text("forms.manage.trusted-player.main.content");
        content(String.format(content, claimedResidence.getName(), generateTrustedPlayerList()));
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        if (response.clickedButtonId() == 0) {
            new ResidenceTrustedPlayerChangeForm(bukkitPlayer,this, claimedResidence, language).send();
            return;
        }
        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response) {
        sendPrevious();
    }
}
