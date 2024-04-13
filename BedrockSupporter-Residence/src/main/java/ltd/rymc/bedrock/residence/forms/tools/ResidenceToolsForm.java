package ltd.rymc.bedrock.residence.forms.tools;

import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.form.RSimpleForm;
import ltd.rymc.bedrock.common.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

public class ResidenceToolsForm extends RSimpleForm {
    public ResidenceToolsForm(Player player, RForm previousForm, Language language) {
        super(player, previousForm, language);

        Language.Section toolMain = section("forms.tool.main");
        Language.Section buttons = toolMain.section("buttons");

        title(toolMain.text("title"));
        content(toolMain.text("content"));
        buttons(
                buttons.text("check"),
                buttons.text("query")
        );
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        int id = response.clickedButtonId();
        if (id == 0) Bukkit.dispatchCommand(bukkitPlayer, "res show");
        else if (id == 1) new ResidenceInfoForm(bukkitPlayer,this, language).send();
    }

    @Override
    public void onClosedOrInvalidResult(SimpleForm form, FormResponseResult<SimpleFormResponse> response) {
        sendPrevious();
    }
}
