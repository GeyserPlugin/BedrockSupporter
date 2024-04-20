package ltd.rymc.bedrock.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import ltd.rymc.bedrock.common.module.Module;
import ltd.rymc.bedrock.plugin.BedrockSupporter;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandAlias("brs|bedrock|bedrocksupporter")
@Description("Bedrock Supporter Main Command")
@SuppressWarnings("unused")
public class MainCommand extends BaseCommand {

    @Subcommand("modules")
    @CommandPermission("brs.modules.list")
    public void modules(CommandSender sender){
        List<Module> modules = BedrockSupporter.getModuleManager().getModules();
        sender.sendMessage("Modules (" + modules.size() + "): ");
        for (Module module : modules) {
            sender.sendMessage(module.getInfoString());
        }
    }

    @Subcommand("reload")
    @CommandPermission("brs.reload")
    public void reload(CommandSender sender){
        BedrockSupporter.getInstance().reload();
    }
}
