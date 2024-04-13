package ltd.rymc.bedrock.residence.forms.setting;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ltd.rymc.bedrock.common.form.RCustomForm;
import ltd.rymc.bedrock.common.form.RForm;
import ltd.rymc.bedrock.common.language.Language;
import ltd.rymc.bedrock.common.utils.ArraysUtils;
import ltd.rymc.bedrock.residence.utils.ResidenceUtils;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.result.FormResponseResult;

import java.util.HashMap;

public class ResidenceSettingSelectForm extends RCustomForm {

    private static final Residence residence = Residence.getInstance();
    HashMap<String, ClaimedResidence> residenceMap;
    String[] names;

    public ResidenceSettingSelectForm(Player player, RForm previousForm, Language language) {
        super(player, previousForm, language);
        residenceMap = ResidenceUtils.getResidenceList(player);
        names = generateResidenceNames();

        Language.Section manageSelect = section("forms.manage.select");

        title(manageSelect.text("title"));
        dropdown(manageSelect.text("dropdown"), names);
    }

    private String[] generateResidenceNames(){
        String[] names = ArraysUtils.rotate(residenceMap.keySet().toArray(new String[0]),1);
        names[0] = text("forms.manage.select.choose");
        return names;
    }

    private ClaimedResidence getResidence(CustomFormResponse response){
        if (response.asDropdown(0) != 0) {
            return residenceMap.get(names[response.asDropdown(0)]);
        }

        ClaimedResidence claimedResidence = residence.getResidenceManager().getByLoc(bukkitPlayer);
        if (claimedResidence == null || (!ResidenceUtils.hasManagePermission(bukkitPlayer, claimedResidence) && !bukkitPlayer.isOp())) {
            return null;
        }

        return claimedResidence;

    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {
        ClaimedResidence residence = getResidence(response);

        if (residence == null){
            sendPrevious();
            return;
        }

        new ResidenceSettingForm(bukkitPlayer, previousForm, residence, language).send();

    }

    @Override
    public void onClosedOrInvalidResult(CustomForm form, FormResponseResult<CustomFormResponse> response) {
        sendPrevious();
    }
}
