package heyblack.repeatersound.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ServerCloseCallback 
{
    Event<ServerCloseCallback> EVENT = EventFactory.createArrayBacked(
            ServerCloseCallback.class,
            (listeners) -> () -> 
            {
                for (ServerCloseCallback listener : listeners) 
                {
                    listener.saveConfig();
                }
            });

    void saveConfig();
}
