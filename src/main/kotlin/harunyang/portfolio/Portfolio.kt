package harunyang.portfolio

import org.bukkit.plugin.java.JavaPlugin
import harunyang.portfolio.listener.MonsterClickListener
import harunyang.portfolio.commands.StartCommand

class Portfolio : JavaPlugin() {

    override fun onEnable() {
        getCommand("시작")?.setExecutor(StartCommand(this))
        server.pluginManager.registerEvents(MonsterClickListener(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
