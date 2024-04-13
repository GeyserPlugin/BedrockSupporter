package ltd.rymc.bedrock.residence.forms.setting;

import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceNoPermissionForm extends RSimpleForm {
    public ResidenceNoPermissionForm(Player player, RForm previousForm, Language language) {
        super(player, previousForm, language);


        Language.Section noPermission = section("forms.manage.no-permission");

        title(noPermission.text("title"));
        content(noPermission.text("content"));
        button(noPermission.text("button"));
    }

    @Override
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        sendPrevious();
    }

    @Override
    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response) {
        sendPrevious();
    }
}
