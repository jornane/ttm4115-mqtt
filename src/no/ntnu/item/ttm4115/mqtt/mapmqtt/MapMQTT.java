package no.ntnu.item.ttm4115.mqtt.mapmqtt;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.mqtt.basicmqtt.BasicMQTT.AdvancedConfiguration;

public class MapMQTT extends Block {

	// Instance parameter. Edit only in overview page.
	public final int group;
	public static final String TOPIC = "map";
	/**
	 * Method to generate an advanced configuration object.
	 * The object will contain both the topic and the group number.
	 * Group number should be provided as instance parameter,
	 * topic should come from a pin.
	 * 
	 * Students are not required to copy, create or use a method like this;
	 * students are allowed to hardcode the group number,
	 * and just use the simple pin with the String type.
	 * 
	 * @copyright 2014 Y¿rn de Jong <git@yorn.priv.no>
	 * 
	 * @param topic The name of the topic to subscribe to
	 * 
	 * @return AdvancedConfiguration configuration object to be used in <code>BasicMQTT</code>
	 */
	public AdvancedConfiguration createConfig() {
		return new AdvancedConfiguration(TOPIC, group);
	}

	// Do not edit this constructor.
	public MapMQTT(int group) {
	    this.group = group;
	}

}
