package lumien.randomthings.util;

import lumien.randomthings.RandomThings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtil
{
	public static boolean isPlayerOnline(String username)
	{
		return RandomThings.proxy.isPlayerOnline(username);
	}

	public static void teleportPlayerToDimension(EntityPlayerMP player, int dimension)
	{
		MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimension);

		player.removeExperienceLevel(0);
		player.setPlayerHealthUpdated();
	}

	public static boolean removeExperiencerFromPlayer(EntityPlayer player, int amount)
	{
		int currentExperience = player.experienceTotal;

		if (amount > currentExperience)
		{
			return false;
		}
		else
		{
			player.experienceTotal -= amount;
			while (amount > 0)
			{
				int amountInBar = (int) (Math.floor(player.experience * player.xpBarCap()));

				if (amountInBar >= amount)
				{
					player.experience -= 1F / player.xpBarCap() * amount;
					amount = 0;
				}
				else
				{
					amount -= amountInBar;
					player.experienceLevel -= 1;
					player.experience = 1;
				}
			}

			return true;
		}
	}
}
