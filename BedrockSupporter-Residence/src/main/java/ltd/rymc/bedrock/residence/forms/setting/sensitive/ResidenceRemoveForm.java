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

public class ResidenceRemoveForm extends RCustomForm {

    private final ClaimedResidence claimedResidence;
    private static final Residence residence = Residence.getInstance();

    public ResidenceRemoveForm(Player player, RForm previousForm, ClaimedResidence claimedResidence, Language language) {
        super(player, previousForm, language);
        this.claimedResidence = claimedResidence;

        if (!claimedResidence.isOwner(player) && !player.isOp()) {
            new ResidenceNoPermissionForm(player, previousForm, language).send();
            return;
        }

        Language.Section sensitiveRemove = section("forms.manage.sensitive.remove");

        title(String.format(sensitiveRemove.text("title"), claimedResidence.getName()));
        input(sensitiveRemove.text("input1"), sensitiveRemove.text("input2"));
        input(sensitiveRemove.text("input3"), sensitiveRemove.text("input4"));
    }


    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        String input = response.asInput(0);

        if (InputUtils.checkInput(input,claimedResidence.getName())) {
            sendPrevious();
            return;
        }

        String title = text("forms.manage.sensitive.remove.title");

        new ResidenceConfirmForm(
                bukkitPlayer,
                previousForm,
                String.format(title, claimedResidence.getName()),
                () -> {

                    if (!claimedResidence.isOwner(bukkitPlayer) && !bukkitPlayer.isOp()) {
                        new ResidenceNoPermissionForm(bukkitPlayer, previousForm, language).send();
                        return;
                    }

                    residence.getResidenceManager().removeResidence(claimedResidence);
                }
                ,language
        ).send();
    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }

}
