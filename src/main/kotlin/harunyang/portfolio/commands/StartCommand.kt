package harunyang.portfolio.commands

import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.*
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import java.util.*
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class StartCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val spawnableMobs = listOf(
        // 적대적
        EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER,
        EntityType.CREEPER, EntityType.ENDERMAN, EntityType.WITCH,
        EntityType.HUSK, EntityType.STRAY, EntityType.DROWNED,
        EntityType.SLIME, EntityType.PILLAGER, EntityType.VINDICATOR,
        EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.MAGMA_CUBE,
        EntityType.GUARDIAN, EntityType.SHULKER, EntityType.RAVAGER,
        EntityType.WARDEN, EntityType.ENDERMITE, EntityType.SILVERFISH,
        EntityType.CAVE_SPIDER, EntityType.BOGGED, EntityType.VEX,
        EntityType.BLAZE, EntityType.WITHER_SKELETON, EntityType.ZOGLIN,
        EntityType.HOGLIN, EntityType.ELDER_GUARDIAN, EntityType.GHAST,
        EntityType.BREEZE, EntityType.PHANTOM, EntityType.EVOKER,
        EntityType.ZOMBIE_VILLAGER,
        // 중립적
        EntityType.IRON_GOLEM, EntityType.POLAR_BEAR, EntityType.ZOMBIFIED_PIGLIN,
        EntityType.LLAMA, EntityType.WOLF
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = sender as? Player ?: return false

        val center = player.location
        val radius = 3.0
        val angleOffset = Math.PI * 2 / 3

        val groupId = UUID.randomUUID().toString()

        repeat(3) { i ->
            val angle = angleOffset * i
            val spawnLoc = center.clone().add(
                radius * cos(angle),
                0.0,
                radius * sin(angle)
            )

            val type = spawnableMobs.random()
            val entity = center.world.spawnEntity(spawnLoc, type) as LivingEntity
            val number = Random.nextInt(1, 11)

            entity.setAI(false)

            val equipment = entity.equipment
            equipment?.helmet = ItemStack(Material.STONE_BUTTON)

            entity.isInvulnerable = true
            entity.customName = "§e$number"
            entity.isCustomNameVisible = true

            val direction = player.location.toVector().subtract(spawnLoc.toVector()).normalize()
            val yaw = Math.toDegrees(Math.atan2(-direction.x, direction.z)).toFloat()

            entity.teleport(entity.location.apply {
                this.yaw = yaw
                this.pitch = 0f
            })

            val container = entity.persistentDataContainer
            container.set(NamespacedKey(plugin, "spawn_count"), PersistentDataType.INTEGER, number)
            container.set(NamespacedKey(plugin, "mob_type"), PersistentDataType.STRING, type.name)
            container.set(NamespacedKey(plugin, "group_id"), PersistentDataType.STRING, groupId)
        }

        player.sendMessage("§a몬스터를 성공적으로 소환했습니다.")
        return true
    }
}
