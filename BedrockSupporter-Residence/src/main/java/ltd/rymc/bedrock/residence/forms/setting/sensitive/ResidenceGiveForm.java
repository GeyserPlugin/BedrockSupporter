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

public class ResidenceGiveForm extends RCustomForm {

    private final ClaimedResidence claimedResidence;
    private static final Residence residence = Residence.getInstance();

    public ResidenceGiveForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!claimedResidence.isOwner(player) && !player.isOp()) {
            new ResidenceNoPermissionForm(player,previousForm, language).send();
            return;
        }

        Language.Section sensitiveGive = section("forms.manage.sensitive.give");

        title(String.format(sensitiveGive.text("title"), claimedResidence.getName()));
        input(sensitiveGive.text("input1"), sensitiveGive.text("input2"));
        input(sensitiveGive.text("input3"), sensitiveGive.text("input4"));
    }


    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(0);
        String input1 = response.asInput(1);

        if (InputUtils.checkInput(input,input1)) {
            sendPrevious();
            return;
        }

        String title = text("forms.manage.sensitive.give.title");

        new ResidenceConfirmForm(
                bukkitPlayer,
                previousForm,
                String.format(title, claimedResidence.getName()),
                () -> {

                    if (!claimedResidence.isOwner(bukkitPlayer) && !bukkitPlayer.isOp()) {
                        new ResidenceNoPermissionForm(bukkitPlayer,previousForm, language).send();
                        return;
                    }

                    residence.getResidenceManager().giveResidence(bukkitPlayer, input.trim(), claimedResidence, false, false);
                }
                ,language
        ).send();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
