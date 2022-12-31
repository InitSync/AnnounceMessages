package net.announcemessages.plugin.managers;

import com.google.common.base.Preconditions;
import net.announcemessages.api.actions.ActionContext;
import net.announcemessages.api.actions.ActionExecutable;
import net.announcemessages.api.managers.ActionManager;
import net.announcemessages.plugin.AnnounceMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SimpleActionManager
implements ActionManager {
	private final AnnounceMessages plugin;
	private final Map<ActionContext, ActionExecutable> actions;
	
	public SimpleActionManager(AnnounceMessages plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The AnnounceMessages instance is null.");
		this.actions = new HashMap<>();
	}
	
	@Override
	public void register(ActionContext context, ActionExecutable executable) {
		Preconditions.checkNotNull(context, "The action context is null.");
		Preconditions.checkNotNull(executable, "The action executable model is null.");
		
		actions.put(context, executable);
	}
	
	@Override
	public void execute(Player player, String container) {
		Preconditions.checkNotNull(player, "The player is null.");
		Preconditions.checkArgument(!container.isEmpty(), "The container is null.");
		
		actions.get(ActionContext.valueOf(StringUtils.substringBetween(container, "[", "]").toUpperCase()))
			 .execute(plugin, player, container.contains(" ")
				  ? container.split(" ", 2)[1]
				  : "");
	}
	
	@Override
	public void unregister(ActionContext context) {
		actions.remove(context);
	}
	
	@Override
	public void unregisterAll() {
		actions.clear();
	}
}