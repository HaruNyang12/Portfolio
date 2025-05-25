package harunyang.portfolio.listener

import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class MonsterClickListener(private val plugin: JavaPlugin) : Listener {

    private val keyCount = NamespacedKey(plugin, "spawn_count")
    private val keyType = NamespacedKey(plugin, "mob_type")
    private val keyGroup = NamespacedKey(plugin, "group_id")

    @EventHandler
    fun onMonsterRightClick(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked as? LivingEntity ?: return
        val container = entity.persistentDataContainer

        val count = container.get(keyCount, PersistentDataType.INTEGER) ?: return
        val mobName = container.get(keyType, PersistentDataType.STRING) ?: return
        val groupId = container.get(keyGroup, PersistentDataType.STRING) ?: return

        val type = try {
            EntityType.valueOf(mobName)
        } catch (e: IllegalArgumentException) {
            return
        }

        val loc = entity.location

        val nearby = entity.location.getNearbyEntities(10.0, 10.0, 10.0)
        nearby.filterIsInstance<LivingEntity>().forEach { other ->
            val otherGroupId = other.persistentDataContainer.get(keyGroup, PersistentDataType.STRING)
            if (otherGroupId == groupId) {
                other.remove()
            }
        }

        repeat(count) {
            val spawned = loc.world.spawnEntity(
                loc.clone().add(0.5 - Math.random(), 0.0, 0.5 - Math.random()),
                type
            ) as? LivingEntity ?: return@repeat

            spawned.equipment?.helmet = ItemStack(Material.STONE_BUTTON)
        }
    }
}
