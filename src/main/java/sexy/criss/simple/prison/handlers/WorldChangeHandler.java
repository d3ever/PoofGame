package sexy.criss.simple.prison.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import sexy.criss.simple.prison.utils.SexyEvent;

public class WorldChangeHandler extends SexyEvent {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

}
