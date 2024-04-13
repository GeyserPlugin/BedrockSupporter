package ltd.rymc.bedrock.residence.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import ltd.rymc.bedrock.modules.ResidenceForm;
import ltd.rymc.bedrock.residence.forms.MainResidenceForm;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("rform|residenceform")
@Description("领地Form菜单")
@SuppressWarnings("unused")
public class ResidenceFormCommand extends BaseCommand {

    @Default
    public void form(CommandSender sender){
        if (!(sender instanceof Player)) {
            return;
        }

        new MainResidenceForm((Player) sender, null, ResidenceForm.getLanguage()).send();
    }

    @Subcommand("reload")
    @CommandPermission("brs.residence.reload")
    public void reload(CommandSender sender){
        ResidenceForm.getMainConfigManager().reloadConfig();
        sender.sendMessage(ResidenceForm.getLanguage().text("reload"));
    }
}
