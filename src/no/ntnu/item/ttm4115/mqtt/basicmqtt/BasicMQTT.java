package no.ntnu.item.ttm4115.mqtt.basicmqtt;

import java.nio.charset.Charset;

import no.ntnu.item.arctis.runtime.Block;

import com.bitreactive.library.mqtt.MQTTConfigParam;
import com.bitreactive.library.mqtt.mqtt.MQTT.Message;

/**
 * Composed MQTT block that uses Reactive Blocks "instance parameters" instead of static properties.
 * This allows for using the block without writing overhead Java code.
 * The block uses hardcoded values and is intended to be used in the TTM4115 subject at NTNU.
 * http://www.item.ntnu.no/academics/courses/ttm4115/
 * 
 * Known problem: not possible to set QoS.
 * 
 * @author Y퓊n de Jong <git@yorn.priv.no>
 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
 */
public class BasicMQTT extends Block {

	/**
	 * Base topic without group number;
	 * group number as int, followed by slash, must be appended
	 */
	private static final String BASE_TOPIC = "ntnu/item/ttm4115/group";
	
	/** Charset constant for encoding/decoding MQTT messages */
	private static final Charset UTF8;
	static {
		Charset utf8 = null;
		try {
			utf8 = Charset.forName("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (utf8 == null) {
			utf8 = Charset.defaultCharset();
		}
		UTF8 = utf8;
	}
	
	/** Group number as provided through ReactiveBlocks UI */
	private final int group;
	/** Effective group number, either from UI or AdvancedConfiguration */
	public int effectiveGroup;

	/**
	 * Datatype used for advanced configuration.
	 * Required in generic enclosing blocks.
	 * 
	 * Students are not required to use this;
	 * they are allowed to hardcode their group number,
	 * and use the normal "subscribe" pin.
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 */
	public static class AdvancedConfiguration {
		public final String topic;
		public int group;

		public AdvancedConfiguration(String topic, int group) {
			this.topic = topic;
			this.group = group;
		}
	}
	
	/**
	 * Generate a MQTT config object from instance variables,
	 * and set the group number.
	 * 
	 * This function is a workaround to a ReactiveBlocks limitation,
	 * which prevents blocks from inheriting instance parameters.
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 * 
	 * @return Generated MQTTConfigParam instance
	 */
	public MQTTConfigParam generateConfigAndGroup(AdvancedConfiguration conf) {
		effectiveGroup = conf.group;
		return generateConfig(conf.topic);
	}
	
	/**
	 * Generate a MQTT config object from instance variables.
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 * @return Generated MQTTConfigParam instance
	 */
	public MQTTConfigParam generateConfig(String subscribeTopics) {
		if (effectiveGroup == 0) {
			effectiveGroup = group;
		}
		if (effectiveGroup == 0) {
			throw new IllegalStateException("Rightclick the TaxiMQTT block and select \"Parameters and Generics\"");
		}
		String topicBase = BASE_TOPIC+effectiveGroup;
		String clientId = MQTTConfigParam.generateUUID(); // not UUID, UID.
		MQTTConfigParam config = new MQTTConfigParam("dev.bitreactive.com", 1883, clientId);
		String[] topics = subscribeTopics.split(",");
		for(String topic : topics) {
			logInfo("Started listening on topic "+topicBase+'/'+topic);
			if (!topic.equals("")) {
				config.addSubscribeTopic(topicBase+"/"+topic);
			}
		}
		// Default topic should never be used, as ESM prohibits it.
		config.setDefaultPublishTopic(topicBase+"/");
		return config;
	}

	/**
	 * Create a message from a String and set a topic which is prefixed with a group-specific.
	 * The topic should have been set during the same round-to-completion step,
	 * but this *may* be omitted.
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 * 
	 * @param content	Acutual payload of the message
	 * @param topic	The topic the message is sent to
	 * @return Message with prefixed topic
	 */
	public Message createMessage(String content, String topic) {
		topic = BASE_TOPIC+effectiveGroup+"/"+topic;
		logDebug("Send message to topic \""+topic+"\": \""+content+"\"");
		return new Message(content.getBytes(UTF8), topic);
	}
	
	/**
	 * Split the topic from a Message and truncate away the prefix
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 * 
	 * @param message The message
	 * @return String short topic if prefix matches, long topic if prefix doesn't match
	 */
	public String messageToTruncatedTopic(Message message) {
		String topicBase = BASE_TOPIC+effectiveGroup+"/";
		if (message.getTopic().startsWith(topicBase)) {
			return message.getTopic().substring(topicBase.length());
		}
		logWarn("Received message on unexpected topic \""+message.getTopic()+"\"");
		return message.getTopic();
	}

	/**
	 * Split the payload from a Message and convert it to a String
	 * 
	 * @copyright 2014 Y퓊n de Jong <git@yorn.priv.no>
	 * 
	 * @param message The message
	 * @return String The payload of the Message, UTF8 encoded
	 */
	public String messageToData(Message message) {
		return new String(message.getPayload(), UTF8);
	}

	// Do not edit this constructor.
	public BasicMQTT(int group) {
	    this.group = group;
	}

}
